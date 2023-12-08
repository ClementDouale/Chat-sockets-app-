import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class AllumetteClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket allu = new Socket("localhost", 10081);
        if(!allu.isClosed()){
            System.out.println("connexion réussie");
        }
        Scanner sc=new Scanner(System.in);
        DataInputStream ins = new DataInputStream(allu.getInputStream());
        System.out.println("le serveur a dit " + ins.readUTF());

        String outValue = new String("");
        // DEFINITION DES PARAMETRES
        while(!outValue.contains("piocher")) {
            outValue = ins.readUTF();
            System.out.println("le serveur a dit " + outValue);

            if((outValue.contains("retirer")) || (outValue.contains("disposées")) || (outValue.contains("commence"))){
                DataOutputStream outs = new DataOutputStream(allu.getOutputStream());
                outs.writeInt(sc.nextInt());
            }

        }

        // JEU
        do {
            if(outValue.contains("piocher")) {
                DataOutputStream outs = new DataOutputStream(allu.getOutputStream());
                outs.writeInt(sc.nextInt());
            }

            outValue = ins.readUTF();
            System.out.println("le serveur : " + outValue);

        }
        while(outValue!="END");
        //ins.close();
        //allu.close();
    }
}
