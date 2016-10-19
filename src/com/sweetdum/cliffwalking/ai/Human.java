package com.sweetdum.cliffwalking.ai;

import com.sweetdum.cliffwalking.game.Action;
import com.sweetdum.cliffwalking.game.Governor;

import java.util.Scanner;

/**
 * Created by Mengxiao Lin on 2016/10/20.
 */
public class Human implements IPlayer {
    @Override
    public Action play(Governor governor) {
        System.out.print("Choose action(WASD):");
        Scanner cin = new Scanner(System.in);
        String s = cin.nextLine().trim();
        switch (Character.toUpperCase(s.charAt(0))){
            case 'W': return Action.UP;
            case 'A': return Action.LEFT;
            case 'S': return Action.DOWN;
            case 'D': return Action.RIGHT;
        }
        return null;
    }
}
