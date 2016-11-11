package com.example.studydemo;

import java.util.List;

/**
 * Created by Administrator on 2016/11/10.
 */

public interface GameContract {

    interface View{
        void setPresenter(Presenter p);
        void startGame();
        void updateScore(int score);
        void updateTime(int time);
        void succeed();
        void failed();
        void setGameBoard(int[] board);
    }
    interface Presenter{
        void start();
        void nextChapter();
        void pause();
        List<Integer> isLinked(int p1, int p2);
        void arrange(int p1,int p2);
        int[] fillBoard();
    }

}
