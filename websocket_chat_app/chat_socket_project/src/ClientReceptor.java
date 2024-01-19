import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/* Chargés de dev : Pierre-Louis et Clément */

public class ClientReceptor extends Thread{
    /* Classe permettant de gérer les messages entrants d'un utilisateur
    * server : socket liant l'utilisateur au serveur
    */
    Socket server;

    // Constructeur de classe
    public ClientReceptor(Socket server){
        this.server=server;
    }

    // Point d'entrée du Thread (classe étendue) qu'on override pour executer
    @Override
    public void run(){
        try {
            // Création d'un point de lecture du socket
            DataInputStream ins = new DataInputStream(server.getInputStream());

            // Boucle infinie gérant les messages entrants
            while(true){
                // Affichage du message sur le terminal de l'utilisateur
                System.out.println(ins.readUTF());
            }
        } catch (IOException e) {
            System.out.println("Deconnexion du client");
        }
    }
}
