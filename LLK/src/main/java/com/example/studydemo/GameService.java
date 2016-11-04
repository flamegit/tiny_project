package com.example.studydemo;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import  static com.example.studydemo.Constants.COLUMN;
import  static com.example.studydemo.Constants.ROW;
import  static com.example.studydemo.Constants.NUM;


public class GameService {
	interface GameCallback{
		void onScoreChange(int score);
		void onUpdateTime(int time);
		void onSucess();
		void onFailed();
	}

    private int mCount=0;
	private int mTime=0;
	private Timer mTimer;
	private Gamelogic mLogic;

	private GameCallback mCallback;

	public GameService(GameCallback callback){
		mLogic=new Gamelogic(NUM,ROW,COLUMN);
		mCallback=callback;
	}
	private int calculateScore(int num){
        return num *100;
	}

	public void start(){
		TimerTask task=new TimerTask() {
			@Override
			public void run() {
				if(mTime==30){
					mCallback.onFailed();
					cancel();
				}
				else{
					mTime++;
					mCallback.onUpdateTime(mTime);
				}
			}
		};
		mTimer=new Timer();
		mTimer.schedule(task,10,1000);
	}

	public int[] fillBoard(){
		return mLogic.fillBoard();
	}

	public void pause(){
		mTimer.cancel();
	}

	public List<Integer> isLinked(int p1,int p2){
		List<Integer> line=mLogic.isLinked(p1,p2);
		if(line!=null){
			mCount++;
			mCallback.onScoreChange(calculateScore(mCount));
			if(mCount==50){
				mCallback.onSucess();
			}
		}
		return line;
	}
}
