package com.example.studydemo;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class FailDialogFragment extends DialogFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

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
				Intent intent=new Intent(getActivity(),MainActivity.class);
				startActivity(intent);
			}
			
		});
		return view;
	}

	

}
