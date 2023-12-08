import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket comm = new Socket("localhost", 10080);
        if(!comm.isClosed()){
            System.out.println("connexion réussie");
        }
        DataOutputStream outs = new DataOutputStream(comm.getOutputStream());
        outs.writeUTF("voici un message");

        DataInputStream ins =  new DataInputStream(comm.getInputStream());
        System.out.println("le serveur a dit "+ins.readUTF());

        ins.close();
        outs.close();
        comm.close();
    }
}
