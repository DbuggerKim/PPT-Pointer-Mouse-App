package network;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class net {
	
	public static void mouseMoveMecro(int x, int y) {
        try {
            Robot robot = new Robot();
            robot.mouseMove(x, y);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
	
	public static void leftclickMouse() {
	    try {
	        Robot robot = new Robot();
	        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static void rightclickMouse() {
	    try {
	        Robot robot = new Robot();
	        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
	        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
    public static void main(String[] args) {
    	
    	int portNumber = 4444;

        try {
            System.out.println("서버를 시작합니다...");
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("포트 " + portNumber + "에서 요청 대기중...");

            while(true) {
                Socket socket = serverSocket.accept();
                InetAddress clientHost = socket.getLocalAddress();
                int clientPort = socket.getPort();
                System.out.println("클라이언트 연결됨. 호스트 : " + clientHost + ", 포트 : " + clientPort);

                ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
                Object obj = instream.readObject();
                System.out.println("클라이언트로부터 받은 데이터 : " + obj);
                
                if(obj.equals("LB")) leftclickMouse();
                if(obj.equals("RB")) rightclickMouse();
                if(!obj.equals("LB") && !obj.equals("RB")) { //TEXT 입력이 아닌 FLOAT형 이라면 좌표값
	                String OBJ=String.valueOf(obj);
	                String []arr=OBJ.split(" ");
	                float mx=Float.parseFloat(arr[0]);
	                float my=Float.parseFloat(arr[arr.length-1]);
                  //안드로이드 폰과 PC 스크린 배율에 알맞게 
	                mx*=5.3;
	                my*=1.5;
	                System.out.println(mx+" "+my);
	                mouseMoveMecro((int)mx, (int)my);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
