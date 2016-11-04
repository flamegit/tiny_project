package com.example.studydemo;



import android.app.Activity;
import android.app.DialogFragment;

import android.app.Fragment;
import android.graphics.Color;
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

public class MainActivity extends Activity implements GameService.GameCallback{

	View menu;
	GameService mService;
	TextSwitcher mScoreView;
	ProgressBar mProgressBar;
	GridImageView mBoard;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		menu=findViewById(R.id.menu);
		mBoard=(GridImageView)findViewById(R.id.board);
		mProgressBar =(ProgressBar)findViewById(R.id.progressbar);
		mScoreView=(TextSwitcher)findViewById(R.id.score);
		View view=findViewById(R.id.text);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("fff","dfdfdsfdddd");
			}
		});
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
		mService=new GameService(this);
	}

	public void click(View view){
		Log.d("fff","dfdfdsfdddd");
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
		mBoard.setService(mService);
		mBoard.invalidate();
		menu.setVisibility(View.GONE);
		mService.start();

	}

	private void showDialog(){
		DialogFragment dialog=new FailDialogFragment();
		dialog.show(getFragmentManager(), "fail");
		dialog.setCancelable(false);
	}

	@Override
	public void onScoreChange(int score) {
		mScoreView.setText(score+"");
	}

	@Override
	public void onUpdateTime(int time) {
		mProgressBar.setProgress(time);
	}

	@Override
	public void onSucess() {
	}

	@Override
	public void onFailed() {
		showDialog();
	}

}
