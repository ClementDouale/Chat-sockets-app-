import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

/* Chargés de dev : Elias et Clément */

public class ServeurChat {
    /* Classe gérant la création de threads clients (front serv)
    * authentication : tableau de Hashmap permettant de lister les utilisateurs connectés
    * port : port à écouter, par défaut 10080
    */
    public static HashMap<Socket, String> authentication;
    private static Integer port = 10080;

    // Getters et setters
    public static HashMap<Socket, String> getAuthentication() {
        return authentication;
    }

    public static void setAuthentication(HashMap<Socket, String> authentication) {
        ServeurChat.authentication = authentication;
    }

    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
        ServeurChat.port = port;
    }

    // Méthode principale
    public static void main(String[] args) throws IOException {
        // Message de lancement du serveur
        System.out.println("Initialisation du serveur");

        // Initialisation de la Hashmap (ne se fait pas par défaut)
        HashMap<Socket, String> authentication = new HashMap<Socket, String>();

        // Ouverture du ServerSocket d'écoute
        ServerSocket conn = new ServerSocket(port);

        // Boucle infinie gérant les connexions utilisateurs (un tour de boucle par client)
        while(true){
            // Attente et accception d'une demande de connexion client
            Socket client = conn.accept();

            System.out.println("Creation du thread client pour un nouvel utilisateur");
            // Création du thread pour le client
            Thread tClient = new Thread(new MessageReceptor(client, authentication));
            System.out.println("Fin de création du thread, lancement du thread");

            // Lancement du Thread pour le client
            tClient.start();
            System.out.println("Thread fonctionnel");

            // Affichage de la taille du tableau (nombre d'utilisateurs) dans le terminal du serveur
            System.out.println("Utilisateurs connectés : " + authentication.size());
        }
    }
}
