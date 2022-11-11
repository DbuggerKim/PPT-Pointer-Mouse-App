package com.example.mouse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SubActivity extends AppCompatActivity {

    View view1;
    ScrollView scrollView1;
    TextView textView1;
    String response;
    Handler handler = new Handler();

    private void printString(String s) {
        textView1.append(s + "\n");
        scrollView1.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        String addr;
        String str;
        view1 = findViewById(R.id.view1);
        scrollView1 = findViewById(R.id.scrollView1);
        textView1 = findViewById(R.id.textView1);
        findViewById(R.id.btn2).setOnClickListener(leftclick);
        findViewById(R.id.btn3).setOnClickListener(rightclick);

        view1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                float curX = event.getX();
                float curY = event.getY();

                Intent intent = getIntent();
                String text1 = intent.getStringExtra("text1");
                String text2 = String.valueOf(curX) + " " + String.valueOf(curY);
                SubActivity.SocketThread thread = new SubActivity.SocketThread(text1, text2);
                thread.start();

                if (action == event.ACTION_DOWN) {
                    printString("손가락 눌림 : " + curX + ", " + curY);
                } else if (action == event.ACTION_MOVE) {
                    printString("손가락 움직임 : " + curX + ", " + curY);
                } else if (action == event.ACTION_UP) {
                    printString("손가락 뗌 : " + curX + ", " + curY);
                }
                return true;
            }
        });
    }

    Button.OnClickListener leftclick = new Button.OnClickListener(){
        public void onClick(View v) {
            Intent intent = getIntent();
            String text1 = intent.getStringExtra("text1");
            String text2 = "LB";
            SubActivity.SocketThread thread = new SubActivity.SocketThread(text1, text2);
            thread.start();
            printString("왼클릭 누름");
        }
    };

    Button.OnClickListener rightclick = new Button.OnClickListener(){
        public void onClick(View v) {
            Intent intent = getIntent();
            String text1 = intent.getStringExtra("text1");
            String text2 = "RB";
            SubActivity.SocketThread thread = new SubActivity.SocketThread(text1, text2);
            thread.start();
            printString("우클릭 누름");
        }
    };

    class SocketThread extends Thread {
        String host;
        String data;
        public SocketThread(String host, String data){
            this.host = host;
            this.data = data;
        }
        @Override
        public void run() {
            try{
                int port = 4444;
                Socket socket = new Socket(host, port);
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
                outstream.writeObject(data);
                outstream.flush();
                ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
                response = (String) instream.readObject();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SubActivity.this, "서버 응답 : " + response, Toast.LENGTH_LONG).show();
                    }
                });
                socket.close();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
