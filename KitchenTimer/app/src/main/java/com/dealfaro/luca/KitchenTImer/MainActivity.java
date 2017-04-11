package com.dealfaro.luca.KitchenTImer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static final private String LOG_TAG = "test2017app1";

    // Counter for the number of seconds.
    private int seconds = 0;
    private int startCount = 0;

    // 3 distinct, most recent used # of seconds.
    private int[] recSeconds = new int[3];

    // Countdown timer.
    private CountDownTimer timer = null;

    private boolean toRecord = false;


    // One second.  We use Mickey Mouse time.
    private static final int ONE_SECOND_IN_MILLIS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        recSeconds[0] = 0;
        recSeconds[1] = 0;
        recSeconds[2] = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayTime();
    }

    public void onClickPlus(View v) {
        seconds += 60;
        toRecord = true; //Becomes true s.t. immediately after, records a time
        displayTime();
    };

    public void onClickMinus(View v) {
        seconds = Math.max(0, seconds - 60);
        toRecord = true; //Becomes true s.t. immediately after, records a time
        displayTime();
    };
    // This button and the next to deal with clicks on the "recent" buttons,
    // and starts the timer with the displayed times.
    public void onClickR1(View v) {
        seconds = recSeconds[0];
        displayTime();
        onClickStart(null);
    }
    public void onClickR2(View v) {
        seconds = recSeconds[1];
        displayTime();
        onClickStart(null);
    }
    public void onClickR3(View v) {
        seconds = recSeconds[2];
        displayTime();
        onClickStart(null);
    }
    public void onReset(View v) {
        seconds = 0;
        cancelTimer();
        displayTime();
    }
    // This function is called every time someone presses start and had
    // immediately before pressed + or -, to save this time as a recent
    // time (if it's not there).
    public void saveRecentTimes(int m, int s){
        boolean doesContain = false;
        for (int i = 0; i <= 2; i++) {
            if (seconds == recSeconds[i])
                doesContain = true;
        }
        if (doesContain == false) {
            recSeconds[startCount % 3] = seconds;
            if (startCount % 3 == 0) {
                TextView r1 = (TextView) findViewById(R.id.button_r1);
                r1.setText(String.format("%d:%02d", m, s));
                displayTime();
            }
            if (startCount % 3 == 1) {
                TextView r2 = (TextView) findViewById(R.id.button_r2);
                r2.setText(String.format("%d:%02d", m, s));
            }
            if (startCount % 3 == 2) {
                TextView r3 = (TextView) findViewById(R.id.button_r3);
                r3.setText(String.format("%d:%02d", m, s));
            }
            startCount++;
        }
    }

    public void onClickStart(View v) {
        if (seconds == 0) {
            cancelTimer();
        }
        if (timer == null) {
            // We create a new timer.
            timer = new CountDownTimer(seconds * ONE_SECOND_IN_MILLIS, ONE_SECOND_IN_MILLIS) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //Log.d(LOG_TAG, "Tick at " + millisUntilFinished);
                    seconds = Math.max(0, seconds - 1);
                    displayTime();
                }

                @Override
                public void onFinish() {
                    seconds = 0;
                    timer = null;
                    displayTime();
                }
            };
            timer.start();

            int m = seconds / 60;
            int s = seconds % 60;
            Log.d(LOG_TAG, "trying to click => " + m + ":" + s);
            //Extra credit: only activates immediately after + or -
            if (toRecord==true) {
                saveRecentTimes(m,s);
            }
            toRecord = false;

        }
    }

    public void onClickStop(View v) {
        cancelTimer();
        displayTime();
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    // Updates the time display.
    private void displayTime() {
        Log.d(LOG_TAG, "Displaying time " + seconds);
        TextView v = (TextView) findViewById(R.id.display);
        int m = seconds / 60;
        int s = seconds % 60;
        v.setText(String.format("%d:%02d", m, s));
        // Manages the buttons.
        Button stopButton = (Button) findViewById(R.id.button_stop);
        Button startButton = (Button) findViewById(R.id.button_start);
        startButton.setEnabled(timer == null && seconds > 0);
        stopButton.setEnabled(timer != null && seconds > 0);
    }

}
