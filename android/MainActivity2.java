package com.example.gomagic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity2 extends AppCompatActivity {

    private final int BUTTON_LEFT_DOWN = 0;
    private final int BUTTON_LEFT_UP = 1;
    private final int BUTTON_RIGHT = 2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //var doc = "SDsds";

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);

        RelativeLayout layout = findViewById(R.id.mouse_touch);
        layout.setOnTouchListener((v, event) -> {
            onTouch(event);
            return true;
        });

        /*SpannableString ss = new SpannableString("ahmed");
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 2, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView ttt = findViewById(R.id.ttt);
        ttt.setText(ss);*/
    }

    private void setTimeout(Runnable run) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                run.run();
            }
        };
        timer.schedule(timerTask, 100);
    }


    private void onTouch(MotionEvent event) {
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    System.out.println(event.getX() + "|" + event.getY());
                    final Socket send = new Socket(getIntent().getStringExtra("addr"), 4040);
                    switch (event.getPointerCount()) {
                        case 1:
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_MOVE: send.getOutputStream().write((event.getX() + "|" + event.getY()).getBytes()); break;
                                case MotionEvent.ACTION_DOWN: send.getOutputStream().write((BUTTON_LEFT_DOWN + "").getBytes()); break;
                                case MotionEvent.ACTION_UP:   send.getOutputStream().write((BUTTON_LEFT_UP + "").getBytes()); break;
                            }
                            break;
                        case 2:
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                case MotionEvent.ACTION_UP:
                                    send.getOutputStream().write((BUTTON_RIGHT + "").getBytes());
                                    break;
                            }
                            break;
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }).start();
    }

    // we need to disable back button
    @Override public void onBackPressed() {}
}
