package com.example.studydemo;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

public class FailDialogFragment extends DialogFragment{

	interface CallBack{
		void nextChapter();
		void retry();
		void exit();
	}

	private CallBack mCallBack;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallBack=(CallBack)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onStart() {
		super.onStart();
		Dialog dialog = getDialog();
		if (dialog != null) {
			DisplayMetrics dm = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view=inflater.inflate(R.layout.fail_dialog, null);
		View exit=view.findViewById(R.id.exit);
		View start=view.findViewById(R.id.start);
		exit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
			
		});
		
		start.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				getDialog().cancel();
				mCallBack.nextChapter();
				//Intent intent=new Intent(getActivity(),MainActivity.class);
				//startActivity(intent);
			}
			
		});
		return view;
	}

	

}
