package com.example.studydemo;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import  static com.example.studydemo.Constants.COLUMN;
import  static com.example.studydemo.Constants.ROW;
import  static com.example.studydemo.Constants.NUM;


public class GameService implements GameContract.Presenter{

    private int mCount=0;
	private int mTime=0;
	private Timer mTimer;
	private Gamelogic mLogic;
	private GameContract.View mView;
	private int mChapter;

	public GameService(GameContract.View view){
		mLogic=new Gamelogic(NUM,ROW,COLUMN);
	    mView=view;
		mView.setPresenter(this);
	}


	private int calculateScore(int num){
        return num *100;
	}

	@Override
	public void start(){
		TimerTask task=new TimerTask() {
			@Override
			public void run() {
				if(mTime==30){
					mView.failed();
					cancel();
				}
				else{
					mTime++;
					mView.updateTime(mTime);
				}
			}
		};
		mTimer=new Timer();
		mTimer.schedule(task,10,1000);
	}

	@Override
	public void nextChapter() {

	}
	@Override
	public int[] fillBoard(){
		return mLogic.fillBoard();
	}

	@Override
	public void pause(){
		mTimer.cancel();
	}

	@Override
	public void arrange(int p1,int p2){
		mLogic.arrange(p1,p2);
	}

	@Override
	public List<Integer> isLinked(int p1,int p2){
		List<Integer> line=mLogic.isLinked(p1,p2);
		if(line!=null){
			mCount++;
			mView.updateScore(calculateScore(mCount));
			if(mCount==50){
				mView.succeed();
			}
		}
		return line;
	}
}
