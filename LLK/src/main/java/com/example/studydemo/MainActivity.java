package com.example.studydemo;



import android.app.Activity;
import android.app.DialogFragment;

import android.app.Fragment;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.io.IOException;

public class MainActivity extends Activity implements GameContract.View{
	View menu;
	GameContract.Presenter mPresenter;
	TextSwitcher mScoreView;
	ProgressBar mProgressBar;
	GridImageView mBoard;
	MediaPlayer mPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		initView();
		initPlayer();
		mPresenter=new GameService(this);
	}
	private void initView(){
		menu=findViewById(R.id.menu);
		mBoard=(GridImageView)findViewById(R.id.board);
		mProgressBar =(ProgressBar)findViewById(R.id.progressbar);
		mScoreView=(TextSwitcher)findViewById(R.id.score);
		mScoreView.setFactory(new ViewSwitcher.ViewFactory() {
			@Override
			public View makeView() {
				TextView view=new TextView(getApplicationContext());
				view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
				view.setTextSize(35);
				view.setTextColor(Color.WHITE);
				view.setGravity(Gravity.CENTER);
				return view;
			}
		});
	}
	private void initPlayer(){
		mPlayer=new MediaPlayer();
		try {
			AssetFileDescriptor assetFileDescritor = getAssets().openFd("Dream.mp3");
			mPlayer.setDataSource(assetFileDescritor.getFileDescriptor());
			mPlayer.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mPlayer.start();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void startGame(View view){
		menu.setVisibility(View.GONE);
		mPresenter.start();
	}

	private void showDialog(){
		DialogFragment dialog=new FailDialogFragment();
		dialog.show(getFragmentManager(), "fail");
		dialog.setCancelable(false);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mPlayer.stop();
	}
	@Override
	public void setPresenter(GameContract.Presenter p) {
		mBoard.setPresenter(p);
	}

	@Override
	public void startGame() {

	}

	@Override
	public void updateScore(int score) {
		mScoreView.setText(score+"");
	}

	@Override
	public void updateTime(int time) {
		mProgressBar.setProgress(time);
	}

	@Override
	public void succeed() {
	}

	@Override
	public void failed() {
		showDialog();
	}

}
