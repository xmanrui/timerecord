

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class socketClient {

    private Socket socket;
    private SocketAddress address;

    public socketClient() {
        try {
            socket = new Socket();
            address = new InetSocketAddress("127.0.0.1", 5020);
            socket.connect(address, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void talk() {

        try {

            //使用DataInputStream封装输入流
            InputStream os = new DataInputStream(System.in);
            byte[] bytes = { 0x03,0x02,0x01};
            byte [] b = new byte[1];
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.write(bytes);
            while (-1 != os.read(b)) {
                dos.write(b); // 发送给客户端
            }

            dos.flush();
            dos.close();
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
        client.talk();
    }

}

