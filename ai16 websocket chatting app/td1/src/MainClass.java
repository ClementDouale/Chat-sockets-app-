import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainClass {
    public static void main(String[] args){

    }

    public void exercice3() throws IOException {
        // Serveur
        Socket client = new Socket("localhost", 7000);
        System.out.println("connecté ...");
        OutputStream out = client.getOutputStream();
        out.write("Bonjour serveur ".getBytes(StandardCharsets.UTF_8));
    }
}