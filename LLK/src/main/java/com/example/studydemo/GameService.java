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
	private Context mContext;
	MediaPlayer mPlayer;

	public GameService(GameCallback callback, Context context){
		mLogic=new Gamelogic(NUM,ROW,COLUMN);
		mPlayer=new MediaPlayer();
		mCallback=callback;
		mContext=context;
	}

	private void initPlayer(){
        mPlayer.reset();
		try {
			AssetFileDescriptor assetFileDescritor = mContext.getAssets().openFd("Dream.mp3");
			mPlayer.setDataSource(assetFileDescritor.getFileDescriptor());
			mPlayer.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mPlayer.start();
	}
	private int calculateScore(int num){
        return num *100;
	}

	public void start(){
		initPlayer();
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

	public void arrange(int p1,int p2){
		mLogic.arrange(p1,p2);
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
