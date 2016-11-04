package com.example.studydemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Administrator on 2016/4/8.
 */
public class Gamelogic {

    private final static int UP = 0;
    private final static int DOWN = 1;
    private final static int LEFT = 2;
    private final static int RIGHT = 3;
    private int ROW;
    private int COLUMN;
    private int NUM;

    private int[] mBoard;

    public Gamelogic(int num,int row,int column){
        NUM=num;
        ROW=row;
        COLUMN=column;
    }

    private int[] generateBoard() {
        Random ran = new Random();
        int tmp[] = new int[(ROW - 2) * (COLUMN - 2)];
        for (int i = 0; i < tmp.length; i += 2) {
            tmp[i] = ran.nextInt(NUM - 1) + 1;
            tmp[i + 1] = tmp[i];
        }
        return tmp;
    }

    private void shuffleBoard(int[] board) {
        Random ran = new Random();
        int j, tmp;
        for (int i = 0; i < board.length; i++) {
            j = ran.nextInt(board.length);
            if (i != j) {
                tmp = board[i];
                board[i] = board[j];
                board[j] = tmp;
            }
        }
    }

    public int[] fillBoard() {
        int[] tmp = generateBoard();
        shuffleBoard(tmp);
        mBoard=new int[ROW*COLUMN];
        int r, c;
        for (int i = 0; i < tmp.length; i++) {
            r = row(i, COLUMN - 2);
            c = column(i, COLUMN - 2);
            mBoard[num(r + 1, c + 1)] = tmp[i];
        }
      return mBoard;
    }



    public List<Integer> isLinked(int p1, int p2) {
        if (mBoard[p1] != mBoard[p2]|| p1==p2)
            return null;
        Set<Integer> set = new TreeSet<Integer>();
        List<Integer> points = new ArrayList<Integer>(4);
        points.add(p2);
        int m = isLinked(p1, p2, set, 2, points);
        if (m == -1)
            return null;
        points.add(p1);
        return points;
    }

    private int isLinked(int p1, int p2, Set<Integer> s, int c,
                         List<Integer> list) {
        if (c == 0) {
            for (int i = 0; i < 4; i++) {
                if (getLinkedItem(p1, i) == p2)
                    return p1;
            }
            return -1;
        }
        //判断是否直接相连
        if (isLinked(p1, p2, s, 0, list) == p1) {
            return p1;
        }

        List<Integer> path1 = getPath(p1, s);
        for (int i = 0; i < path1.size(); i++) {
            int m1 = path1.get(i);
            int m2 = isLinked(m1, p2, s, c - 1, list);
            if (m2 != -1) {
                list.add(m1);
                return m1;
            }
        }
        return -1;
    }

    private List<Integer> getPath(int p, Set<Integer> s) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            getPathdItem(p, i, list, s);
        }
        return list;
    }

    private int getPathdItem(int p, int d, List<Integer> list, Set<Integer> s) {
        int np = next(p, d);
        if (s.contains(np))
            return -1;
        while (true) {
            if (np != -1 && mBoard[np] == 0) {
                list.add(np);
                s.add(np);
                np = next(np, d);
            } else {
                return np;
            }
        }
    }

    private int getLinkedItem(int p, int d) {
        int np = next(p, d);
        while (true) {
            if (np != -1 && mBoard[np] == 0) {
                np = next(np, d);
            } else {
                return np;
            }
        }
    }

    private int next(int p, int d) {
        int r = row(p,COLUMN);
        int c = column(p,COLUMN);
        switch (d) {
            case UP:
                r -= 1;
                break;
            case DOWN:
                r += 1;
                break;
            case LEFT:
                c -= 1;
                break;
            case RIGHT:
                c += 1;
                break;
        }
        if (r < 0 || r > ROW - 1 || c < 0 || c > COLUMN - 1) {
            return -1;
        }
        return num(r, c);
    }

    private int row(int num, int c) {
        return num / c;
    }

    private int column(int num, int c) {
        return num % c;
    }

    private int num(int r, int c) {
        return r * COLUMN + c;
    }

}
