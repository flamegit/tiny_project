package com.flame.airplanemode;


import android.animation.AnimatorInflater;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.flame.airplanemode.Utils.FINISH_HOUR;
import static com.flame.airplanemode.Utils.FINISH_MINUTE;
import static com.flame.airplanemode.Utils.START_HOUR;
import static com.flame.airplanemode.Utils.START_MINUTE;
import static com.flame.airplanemode.Utils.SWITCH;


import static android.provider.Settings.Global.AIRPLANE_MODE_ON;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.start_button)
    Button startButton;
    @BindView(R.id.finish_button)
    Button finishButton;
    @BindView(R.id.time_layout)
    View layout;
    @BindView(R.id.time_picker)
    TimePicker timePicker;
    @BindView(R.id.ok)
    Button okButton;
    @BindView(R.id.cancel)
    Button cancelButton;
    @BindView(R.id.start_time)
    TextView startView;
    @BindView(R.id.finish_time)
    TextView finishView;
    @BindView(R.id.switch_view)
    Switch switchView;
    int mIndex;
    int mStartH, mStartM, mFinishH, mFinishM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        timePicker.setIs24HourView(true);
        initView();
    }

    private void initView() {
        mStartH = getPreferences(MODE_PRIVATE).getInt(START_HOUR, 22);
        mStartM = getPreferences(MODE_PRIVATE).getInt(START_MINUTE, 30);
        mFinishH = getPreferences(MODE_PRIVATE).getInt(FINISH_HOUR, 8);
        mFinishM = getPreferences(MODE_PRIVATE).getInt(FINISH_MINUTE, 30);
        boolean isChecked = getPreferences(MODE_PRIVATE).getBoolean(SWITCH, false);
        startView.setText(mStartH + ":" + mStartM);
        finishView.setText(mFinishH + ":" + mFinishM);
        switchView.setChecked(isChecked);
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getPreferences(MODE_PRIVATE).edit()
                        .putBoolean(SWITCH, isChecked)
                        .apply();
                if (isChecked) {
                    Utils.scheduleAirPlane(MainActivity.this, mStartH, mStartM, mFinishH, mFinishM);
                    enableReceiver(true);
                } else {
                    Utils.disable(MainActivity.this);
                    enableReceiver(false);
                }

            }
        });
    }

    @OnClick(R.id.start_button)
    public void setStartTime(View view) {
        Animation show = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        layout.startAnimation(show);
        layout.setVisibility(View.VISIBLE);
        mIndex = 0;
    }

    @OnClick(R.id.finish_button)
    public void setFinishTime(View view) {
        Animation show = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        layout.startAnimation(show);
        layout.setVisibility(View.VISIBLE);
        mIndex = 1;
    }

    @OnClick(R.id.ok)
    public void setTime(View view) {
        Animation hide = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        if (mIndex == 0) {
            mStartH = timePicker.getCurrentHour();
            mStartM = timePicker.getCurrentMinute();
            getPreferences(MODE_PRIVATE).edit()
                    .putInt(START_HOUR, mStartH)
                    .putInt(START_MINUTE, mFinishM)
                    .apply();
            startView.setText(mStartH + ":" + mStartM);
        }
        if (mIndex == 1) {
            mFinishH = timePicker.getCurrentHour();
            mFinishM = timePicker.getCurrentMinute();
            getPreferences(MODE_PRIVATE).edit()
                    .putInt(FINISH_HOUR, mFinishH)
                    .putInt(FINISH_MINUTE, mFinishM)
                    .apply();
            finishView.setText(mFinishH + ":" + mFinishM);
        }

        if (switchView.isChecked()) {
            Utils.scheduleAirPlane(this, mStartH, mStartM, mFinishH, mFinishM);
        }
        layout.startAnimation(hide);
        layout.setVisibility(View.GONE);
    }

    @OnClick(R.id.cancel)
    public void cancel(View view) {
        Animation hide = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        layout.startAnimation(hide);
        layout.setVisibility(View.GONE);
    }

    private void enableReceiver(boolean enable){
        ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = getPackageManager();
        if(enable){
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }else {
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
