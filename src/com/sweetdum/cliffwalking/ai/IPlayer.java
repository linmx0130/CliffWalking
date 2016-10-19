package com.sweetdum.cliffwalking.ai;

import com.sweetdum.cliffwalking.game.Action;
import com.sweetdum.cliffwalking.game.Governor;

/**
 * Created by Mengxiao Lin on 2016/10/20.
 */
public interface IPlayer {
    /**
     * ask AI for a action
     * @param governor the game AI
     * @return the action to be conducted
     */
    Action play(Governor governor);
}
