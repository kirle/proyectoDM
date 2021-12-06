package com.example.proyectodm.iu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.proyectodm.R;
import com.example.proyectodm.core.DBManager;

import java.util.Locale;

public class RoundTimer extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 60000;

    private TextView txt_timer;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer timer;

    private Boolean mTimerRunning = false;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_timer);

        this.db = DBManager.getInstance(this.getApplicationContext());

        txt_timer = (TextView) findViewById(R.id.txt_timer);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        Button mButtonCorrect = (Button) findViewById(R.id.button_correct);


        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_menu);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTimerRunning){
                    pauseTimer();
                }else{
                    startTimer();
                }
            }
        });

        mButtonCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                Intent myIntent2 = new Intent(RoundTimer.this, Instructions.class);
                ActivityOptions.makeSceneTransitionAnimation(RoundTimer.this).toBundle();

                RoundTimer.this.startActivity(myIntent2);

            }
        });

        updateCountDownText();

    }
    public void playSound(){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.changesound);
        mediaPlayer.start();
    }

    private void startTimer(){
        timer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("Start");
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer(){
        timer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer(){
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private int calculatePoints(){
        String aux = txt_timer.getText().toString().substring(2);
        int actualSeconds = Integer.parseInt(aux);
        int originalSeconds = (int) START_TIME_IN_MILLIS/1000;
        int points = originalSeconds - actualSeconds;
        return points;
    }

    private void updateCountDownText(){
        int minutes = (int) mTimeLeftInMillis / 1000 / 60;
        int seconds = (int) mTimeLeftInMillis / 1000 % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        txt_timer.setText(timeLeftFormatted);
    }
}