package com.example.mouse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    String addr;
    String str;
    String response;
    EditText addressInput;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addressInput = findViewById(R.id.addressInput);
        Button connectButton = findViewById(R.id.connectButton);
        Button nextButton = findViewById(R.id.nextButton);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addr = addressInput.getText().toString().trim(); //IP는 UI상에서 직접 입력
                str = "-987.345 -3049.234";
                SocketThread thread = new SocketThread(addr, str);
                thread.start();
                Toast.makeText(MainActivity.this, "NEXT 버튼을 클릭해주세요", Toast.LENGTH_SHORT).show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input1 = addr;
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                intent.putExtra("text1", input1);
                startActivity(intent);
            }
        });
    }

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
                        Toast.makeText(MainActivity.this, "서버 응답 : " + response, Toast.LENGTH_LONG).show();
                    }
                });
                socket.close();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
