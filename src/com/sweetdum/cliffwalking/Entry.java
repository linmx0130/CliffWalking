package com.sweetdum.cliffwalking;

import com.sweetdum.cliffwalking.ai.Human;
import com.sweetdum.cliffwalking.ai.IPlayer;
import com.sweetdum.cliffwalking.ai.ITrainablePlayer;
import com.sweetdum.cliffwalking.ai.MonteCarloAI;
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
        Governor game = new Governor(4,10);
        //humanEntry(game);
        ITrainablePlayer player = new MonteCarloAI(0.99, 0.99, -14,10000, game);
        player.train();
    }
}
