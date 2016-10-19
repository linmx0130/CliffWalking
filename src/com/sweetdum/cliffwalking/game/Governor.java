package com.sweetdum.cliffwalking.game;

import javafx.geometry.Pos;

import static com.sweetdum.cliffwalking.game.Action.*;

/**
 * Created by Mengxiao Lin on 2016/10/19.
 * The governor of the game.
 * It will receive action from AI and create new state and reward.
 */
public class Governor {
    private int height, width;
    private Position startState, endState;
    private Position currentState;
    private boolean episodeEnd = false;
    public Governor(int height, int width) {
        this.height = height;
        this.width = width;
        this.startState = new Position(0,0);
        this.endState = new Position(width-1,0);
        startEpisode();;
    }
    public void startEpisode(){
        currentState=this.startState;
        this.episodeEnd = false;
    }
    public int receiveAction(Action action){
        Position newPos = currentState.clone();
        switch (action) {
            case UP:
                newPos.setY(newPos.getY() + 1);
                break;
            case DOWN:
                newPos.setY(newPos.getY() - 1);
                break;
            case LEFT:
                newPos.setX(newPos.getX() - 1);
                break;
            case RIGHT:
                newPos.setX(newPos.getX() + 1);
                break;
        }
        if (outsideMap(newPos)){
            newPos = startState.clone();
            currentState= newPos;
        }
        if (isInCliff(newPos)) {
            currentState = startState.clone();
            episodeEnd = true;
            return -500;
        }else{
            currentState = newPos;
            episodeEnd = currentState.equals(endState);
            return -1;
        }
    }

    public Position getCurrentState() {
        return currentState;
    }

    public boolean outsideMap(Position pos){
        return (pos.getX()>=width || pos.getX() <0 || pos.getY()>=height || pos.getY()<0);
    }
    private boolean isInCliff(Position pos){
        if (pos.getX()!=0 && pos.getX() != width -1){
            if (pos.getY() == 0){
                return true;
            }
        }
        return false;
    }

    public boolean isEpisodeEnd() {
        return episodeEnd;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
