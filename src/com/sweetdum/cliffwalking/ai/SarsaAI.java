package com.sweetdum.cliffwalking.ai;

import com.sweetdum.cliffwalking.game.Action;
import com.sweetdum.cliffwalking.game.Governor;
import com.sweetdum.cliffwalking.game.Position;

import java.util.Random;

import static com.sweetdum.cliffwalking.game.Action.*;

/**
 * Sarsa AI: an on-policy TD control algorithm.
 * Created by Mengxiao Lin on 2016/10/20.
 */
public class SarsaAI implements ITrainablePlayer{
    private double q[][][];
    private double eps,epsDecayRate, gamma, learnRate;
    private Governor governor;
    private Random random;
    private int batchSize;
    /**
     * Sarsa AI is an on-policy TD control algorithm.
     * Q(S,A) <- Q(S,A) + learnRate*(R + gamma*Q(S',A') - Q(S,A))
     * @param eps the constant of eps-greedy policy
     * @param epsDecayRate the decay rate of the eps
     * @param gamma in the expression
     * @param learnRate in the expression
     * @param batchSize after how many iteration should the AI update eps
     * @param governor the game governor
     */
    public SarsaAI(double eps, double epsDecayRate, double gamma, double learnRate, int batchSize, Governor governor) {
        this.governor = governor;
        this.eps = eps;
        this.epsDecayRate = epsDecayRate;
        this.gamma = gamma;
        this.learnRate = learnRate;
        this.q = new double[governor.getWidth()][governor.getHeight()][4];
        this.random = new Random();
        this.batchSize = batchSize;
    }

    private int actionId(Action a){
        switch (a){
            case UP:
                return 0;
            case RIGHT:
                return 1;
            case DOWN:
                return 2;
            case LEFT:
                return 3;
        }
        return 100;
    }
    private Action idAction(int id){
        switch (id){
            case 0: return UP;
            case 1: return RIGHT;
            case 2: return DOWN;
            case 3: return LEFT;
        }
        return null;
    }
    @Override
    public void train() {
        int iterCount = 0;
        double totalReturn = 0;
        double maxTotalReturnVisited = -1e10;
        int notLargerThanMaxTimes = 0;
        while(true){
            iterCount ++;
            double returnSum = 0.0;
            governor.startEpisode();
            Action a1  = this.play(governor);
            Position s1 = governor.getCurrentState();
            while (!governor.isEpisodeEnd()){
                int returnValue = governor.receiveAction(a1);
                returnSum += returnValue;
                Position s2 = governor.getCurrentState();
                Action a2 = this.play(governor);
                q[s1.getX()][s1.getY()][actionId(a1)] += learnRate *
                        (returnValue +
                                gamma * q[s2.getX()][s2.getY()][actionId(a2)] -
                                q[s1.getX()][s1.getY()][actionId(a1)]);
                s1 = s2;
                a1 = a2;
            }
            totalReturn +=returnSum;

            if (iterCount % batchSize ==0){
                totalReturn /= batchSize;
                eps *= epsDecayRate;
                System.out.println("Iter "+(iterCount-batchSize)+" ~ "+iterCount+": avg return = "+ totalReturn);
                if (totalReturn > maxTotalReturnVisited){
                    maxTotalReturnVisited = totalReturn;
                    notLargerThanMaxTimes = 0;
                }else{
                    notLargerThanMaxTimes++;
                    if (notLargerThanMaxTimes >=10) return ;
                }
                totalReturn = 0;
            }
        }
    }

    @Override
    public Action play(Governor governor) {
        Position currentState = governor.getCurrentState();
        int x = currentState.getX();
        int y = currentState.getY();
        //eps-greedy
        if (random.nextDouble()<eps){
            int v = random.nextInt()%4;
            if (v<0) v= v+4;
            return idAction(v);
        }
        int bestAction= 0;
        for (int i=1;i<4;++i){
            if (q[x][y][i] > q[x][y][bestAction]){
                bestAction = i;
            }
        }
        return idAction(bestAction);
    }
}
