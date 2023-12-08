import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {
    public static void main(String[] args) throws IOException {
        ServerSocket conn = new ServerSocket(10080);
        Socket comm = conn.accept();

        DataInputStream ins = new DataInputStream(comm.getInputStream());
        System.out.println("le client a dit "+ins.readUTF());

        DataOutputStream outs = new DataOutputStream(comm.getOutputStream());
        outs.writeUTF("voici un message du serveur");

        ins.close();
        outs.close();
        comm.close();
    }

}
