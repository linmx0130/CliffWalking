package com.sweetdum.cliffwalking;

import com.sweetdum.cliffwalking.ai.*;
import com.sweetdum.cliffwalking.game.Governor;

/**
 * Created by Mengxiao Lin on 2016/10/20.
 */
public class Entry {
    private static void humanEntry(Governor game){
        IPlayer player = new Human();
        game.startEpisode();
        int totalReturn = 0;
        while(!game.isEpisodeEnd()){
            int returnValue = game.receiveAction(player.play(game));
            totalReturn += returnValue;
            System.out.println(game.getCurrentState());
            System.out.println("Return = "+totalReturn);
        }
    }
    public static void main(String args[]){
        Governor game = new Governor(3,6);
        //humanEntry(game);
        //ITrainablePlayer player = new SarsaAI(0.01,0.5,0.1,0.1,100,game);
        ITrainablePlayer player = new QlearningAI(0.01,0.5,0.1,0.1,100,game);
        //ITrainablePlayer player = new MonteCarloAI(0.3, 0.5,0.1,10000,game);
        player.train();
        System.out.println("Training finished!");
    }
}
