package com.sweetdum.cliffwalking.ai;

import static com.sweetdum.cliffwalking.game.Action.*;

import com.sweetdum.cliffwalking.game.Action;
import com.sweetdum.cliffwalking.game.Governor;
import com.sweetdum.cliffwalking.game.Position;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mengxiao Lin on 2016/10/20.
 * Implementation of On-policy first-visit MC control AI with eps-soft policies
 */
public class MonteCarloAI implements ITrainablePlayer {
    private double[][][] q;
    private double[][][] pi;
    private double eps;
    private Random random;
    private double epsDecayRate;
    private double target;
    private int batchSize = 10000;
    private Governor governor;

    /**
     * MonteCarloAI is the basic reinforcement AI for such a problem.
     * @param eps the eps-greedy constant
     * @param epsDecayRate the decay rate of eps
     * @param target the score target of the train process
     * @param batchSize after how many iteration should the AI update eps
     * @param governor the game governor
     */
    public MonteCarloAI(double eps, double epsDecayRate, double target, int batchSize, Governor governor) {
        this.eps = eps;
        this.epsDecayRate = epsDecayRate;
        this.target =target;
        this.batchSize = batchSize;
        q = new double[governor.getWidth()][governor.getHeight()][4];
        pi = new double[governor.getWidth()][governor.getHeight()][4];
        for (int x=0;x<governor.getWidth();++x){
            for (int y=0;y<governor.getHeight();++y){
                for (int k = 0;k<4;++k){
                    pi[x][y][k]=0.25;
                }
            }
        }
        random = new Random();
        this.governor = governor;
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
        int width = governor.getWidth();
        int height = governor.getHeight();
        double returnSum[][][] = new double[width][height][4];
        double returnTimes[][][] = new double[width][height][4];
        int count = 0;
        double batchAvgReturn =0.0;

        while (true){
            //generate episode from pi
            governor.startEpisode();
            ArrayList<Position> states =new ArrayList<>();
            ArrayList<Action> actions = new ArrayList<>();
            ArrayList<Integer> returns = new ArrayList<>();
            states.add(governor.getCurrentState());
            int totalReturn = 0;
            while (!governor.isEpisodeEnd()){
                Action action = play(governor);
                actions.add(action);
                int currentReturn = governor.receiveAction(action);
                returns.add(currentReturn);
                totalReturn+=currentReturn;
                states.add(governor.getCurrentState());
            }
            //accumulate returns
            double[] returnsAcc = new double[returns.size()];
            returnsAcc[returns.size()-1]=returns.get(returns.size()-1);
            for (int i=returns.size()-2;i>=0;--i){
                returnsAcc[i]= returnsAcc[i+1]+returns.get(i);
            }
            boolean visit[][][] = new boolean[width][height][4];
            //update returnSum
            for (int i=0;i<returnsAcc.length;++i){
                int x = states.get(i).getX();
                int y = states.get(i).getY();
                int a = actionId(actions.get(i));
                if (!visit[x][y][a]) {
                    returnSum[x][y][a] += returnsAcc[i];
                    returnTimes[x][y][a] += 1;
                    visit[x][y][a]=true;
                }
            }
            //update q and pi
            for (int x=0;x<width;++x) {
                for (int y = 0; y < height; ++y) {
                    for (int k = 0; k < 4; ++k) {
                        if (returnTimes[x][y][k] != 0) {
                            q[x][y][k] = returnSum[x][y][k] / returnTimes[x][y][k];
                        }
                        pi[x][y][k] = eps / 4;
                    }
                    int bestAction = 0;
                    for (int k = 1; k < 4; ++k) {
                        if (q[x][y][k] > q[x][y][bestAction]) bestAction = k;
                    }
                    pi[x][y][bestAction] = 1 - eps + eps / 4;
                }
            }
            count ++;
            batchAvgReturn+=totalReturn;
            if (count % batchSize == 0) {
                batchAvgReturn/=batchSize;
                System.out.println("Iter="+(count-batchSize)+" to "+count+", avg total return="+batchAvgReturn);
                eps *= epsDecayRate;
                if (batchAvgReturn> target) break;
            }



        }
    }

    @Override
    public Action play(Governor governor) {
        Position currentState = governor.getCurrentState();
        double chooseP = random.nextDouble();
        for (int i=0;i<4;++i){
            chooseP -= pi[currentState.getX()][currentState.getY()][i];
            if (chooseP<=0){
                return idAction(i);
            }
        }
        return null;
    }
}
