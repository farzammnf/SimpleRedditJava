import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientTest {
    static Socket socket;
    static DataInputStream dis;
    static DataOutputStream dos;

    public static void writer(String write) {
        if (write != null && !write.isEmpty()) {
            try {
                dos.writeUTF(write);
                System.out.println("write: " + write);
            } catch (IOException e) {
                try {
                    dis.close();
                    dos.close();
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                e.printStackTrace();
            }
            return;
        }
        System.out.println("invalid write");
    }
    public static void main(String[] args) throws IOException {
        socket = new Socket("localhost",1122);
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
        Scanner input = new Scanner(System.in);
        String command = input.next();
        System.out.println(command);
        writer(command);
        String validOrNot = dis.readUTF();
        System.out.println(validOrNot);

    }
}
