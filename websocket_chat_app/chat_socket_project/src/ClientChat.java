import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/* Chargés de dev : Pierre-Louis */

public class ClientChat {
    // Classe permettant l'instanciation d'un client
    public static void main(String[] args) throws IOException {
        // Connexion au serveur
        Socket server = new Socket ("localhost", 10080);
        Scanner sc = new Scanner(System.in);

        // Création d'un thread qui se chargera de la gestion des messages entrants
        Thread threadReceptor = new Thread(new ClientReceptor(server));
        threadReceptor.start();

        // Ouverture d'un outputstream pour les messages sortants
        DataOutputStream outs = new DataOutputStream(server.getOutputStream());

        // Boucle infinie de gestion des messages sortants
        while(true){
            String message = sc.nextLine();
            outs.writeUTF(message);
            // Si le client envoie EXIT, il veut quitter le tchat
            if(message.equals("EXIT")) break;
        }
        // Desactivation du Thread gérant les messages entrants
        threadReceptor.interrupt();
        // Fermeture du socket avec le serveur
        server.close();
    }
}
