package com.ian.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Timer timer;
    private TextView tv;
    private Button bt1, bt2;
    private ListView lview;
    private UIHandler handler;
    private int intHSec;
    private boolean isStart;
    private LinkedList<HashMap<String, String>> data;
    private SimpleAdapter adapter;
    private int[] to = {R.id.lapdata};
    private String[] from = {"lapdata"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.tv);
        bt1 = (Button)findViewById(R.id.bt1);
        bt2 = (Button)findViewById(R.id.bt2);
        lview = (ListView)findViewById(R.id.lv);

        handler = new UIHandler();

        timer = new Timer();
        timer.schedule(new MyTask(), 0, 10);

        isStart = false;
        intHSec = 0;


        data =
                new LinkedList<HashMap<String, String>>();
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            if (isStart){
                intHSec++;
                handler.sendEmptyMessage(0);
            }
        }
    }

    public void doResetOrLap(View v){
        if (isStart){
            addLap();
        }else{
            doReset();
        }
    }

    private void addLap(){
        HashMap<String,String> aLap = new HashMap<String, String>();
        aLap.put(from[0], toClock());
//		data.add(aLap);
        data.add(0,aLap);
        adapter = new SimpleAdapter(this, data, R.layout.item, from, to);
        lview.setAdapter(adapter);
    }

    private void doReset(){
        intHSec = 0;
        tv.setText(toClock());
    }

    public void doStartOrStop(View v){
        if (isStart){
            bt2.setText("Start");
            bt1.setText("Reset");
        }else{
            bt2.setText("Stop");
            bt1.setText("Lap");
        }
        isStart = !isStart;

    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            tv.setText(toClock());
        }
    }

    private String toClock(){
        int th = intHSec % 100;
        int tsec = intHSec / 100;

        int hh = tsec / (60*60);
        int mm = (tsec - hh*60*60) / 60;
        int ss = tsec % 60;

        return hh + ":" + (mm<10?"0"+mm:mm) + ":" +
                (ss<10?"0"+ss:ss) + "." + (th<10?"0"+th:th);

    }
}