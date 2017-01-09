

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.io.*;
import java.util.*;

import protocol.Tool.AcceptByteArry;
import protocol.Tool.SendData;

public class socketClient {

    private Socket socket;
    private SocketAddress address;
    SendThread  sendTh;
    AcceptThread acceptTh;
    public class SendThread extends Thread {
        private Socket socketObj;
        private Vector<SendData> sendeVector = new Vector<>(); // Vector是线程安全的

        public  SendThread(Socket obj)
        {
            socketObj = obj;
        }
        public void AddSendData(SendData obj)
        {
            sendeVector.add(obj);
        }
        public void run()
        {
            while(true)
            {
                if(sendeVector.isEmpty())
                {
                    continue;
                }

                try{
                    int dataIndex = sendeVector.size() - 1;
                    SendData data = sendeVector.get(dataIndex);
                    DataOutputStream dos = new DataOutputStream(socketObj.getOutputStream());
                    dos.write(data.msgPackage);
                    dos.flush();
                    sendeVector.remove(dataIndex);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

            }
        }
    }

    public class AcceptThread extends Thread {
        private Socket socketObj;
        private AcceptByteArry acceptBytesObj;
        private byte[] acceptedData;
        public AcceptThread(Socket obj)
        {
            socketObj = obj;
        }
        public void run()
        {
            while(true)
            {
                try{
                    BufferedInputStream bis = new BufferedInputStream(
                            socket.getInputStream());

                    DataInputStream dis = new DataInputStream(bis);
                    byte[] bytes = new byte[1]; // 一次读取一个byte

                    while (dis.read(bytes) != -1) {
                        System.out.println(bytes[0]); // for test
                        acceptedData = acceptBytesObj.push(bytes[0]);
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

    public void DoingSend()
    {
        sendTh = new SendThread(socket);
        sendTh.start();
    }

    public void DoingAccept()
    {
        acceptTh = new AcceptThread(socket);
        acceptTh.start();
    }

    public void Test() {
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
        client.DoingSend();
        client.DoingAccept();
    }

}

