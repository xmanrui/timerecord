

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.io.*;

public class socketClient {

    private Socket socket;
    private SocketAddress address;
    SendThread  sendTh;
    AcceptThread acceptTh;
    public class SendThread extends Thread {
        private Socket socketObj;
        public  SendThread(Socket obj)
        {
            socketObj = obj;
        }
        public void run()
        {
            while(true)
            {
                try{
                    byte[] bytes = {0x03, 0x02, 0x01};
                    DataOutputStream dos = new DataOutputStream(socketObj.getOutputStream());
                    dos.write(bytes);
                    dos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

            }
        }
    }

    public class AcceptThread extends Thread {
        private Socket socketObj;
        public AcceptThread(Socket obj)
        {
            socketObj = obj;
        }
        public void run()
        {
            while(true)
            {
                try{
                    // 装饰流BufferedReader封装输入流（接收客户端的流）
                    BufferedInputStream bis = new BufferedInputStream(
                            socket.getInputStream());

                    DataInputStream dis = new DataInputStream(bis);
                    byte[] bytes = new byte[1]; // 一次读取一个byte
                    String ret = "";
                    while (dis.read(bytes) != -1) {
                        System.out.println(bytes[0]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }


    public socketClient() {
        try {
            socket = new Socket();
            address = new InetSocketAddress("127.0.0.1", 5020);
            socket.connect(address, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void TestSend()
    {
        sendTh = new SendThread(socket);
        sendTh.start();
    }

    public void TestAccept()
    {
        acceptTh = new AcceptThread(socket);
        acceptTh.start();
    }

    public SendThread SendThJoin()
    {
        return sendTh;
    }

    public void talk() {
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());

            //使用DataInputStream封装输入流
            InputStream os = new DataInputStream(System.in);
            byte[] bytes = {0x03, 0x02, 0x01};
            byte [] b = new byte[1];
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.write(bytes);
            while (-1 != os.read(b)) {
                dos.write(b); // 发送给客户端
            }

            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
    }

    public static void main(String[] args) {
        socketClient client = new socketClient();
        client.TestSend();
        client.TestAccept();
    }

}

