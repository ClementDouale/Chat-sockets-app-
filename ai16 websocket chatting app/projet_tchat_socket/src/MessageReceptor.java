import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

/* Chargés de dev : Elias et Clément */

public class MessageReceptor extends Thread{
    /* Classe permettant de gérer les messages côté serveur, sous forme de Threads (un par client)
    * client : socket du client
    * pseudo : pseudo du client
    * endConnection : message permettant au client de se déconnecter
    * authentication : pointeur vers le tableau stocké dans la classe parente du Thread, qui contient
    * la Hashmap avec tous les utilisateurs
    */
    private Socket client;
    private String pseudo;
    private String endConnection = "EXIT";
    private static HashMap<Socket, String> authentication;

    // Getters et setters
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    // Constructeur de classe
    public MessageReceptor(Socket client, HashMap<Socket, String> authentication){
        this.client=client;
        this.pseudo="anonymous";
        this.authentication=authentication;
    }

    // Point d'entrée du Thread (classe étendue) qu'on override pour executer
    @Override
    public void run(){
        try{
            // Inscription par défaut du client, sous le pseudo "anonymous" et non annoncé aux autres utilisateurs
            authentication.put(client,pseudo);

            // Variables d'entrées / sorties avec le client
            DataOutputStream outs = new DataOutputStream(client.getOutputStream());
            DataInputStream ins =  new DataInputStream(client.getInputStream());

            // On demande à l'utilsiateur d'entrer son pseudo
            outs.writeUTF("Entrez votre pseudo");
            setPseudo(ins.readUTF());

            // Diffusion à tous les utilisateurs de la présence d'une nouvelle personne
            diffuseMessage(pseudo, pseudo+" a rejoint la conversation \n ----------------------", true);
            // Mise à jour du pseudo
            authentication.put(client,pseudo);

            while(true){
                // Gestion de l'envoie des messages par l'utilisateur
                diffuseMessage(pseudo, ins.readUTF(), false);
            }
        }catch (IOException ex){
            // Si on est dans l'exception alors l'utilisateur s'est déconnecté violemment
            try {
                diffuseMessage(pseudo, "EXIT", false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void diffuseMessage(String pseudo, String message, Boolean firstMessage) throws IOException {
        // Fonction de diffusion des messages à tous les utilisateurs, si la liste n'est pas vide
        if (!authentication.isEmpty()){
            // Mise en forme par défaut du message
            if (message.equals(endConnection)){
                // Si le message demande une déconnexion, on le supprime de la liste et on l'annonce aux utilisateurs
                authentication.remove(client);
                message = pseudo + " a quitte la conversation.";
            } else if (firstMessage != true){
                // Si ce n'est pas le message de bienvenue, on fixe le format "pseudo: message"
                message = pseudo + ": " + message;
            }
            // On parcourt la liste des utilisateurs connectés
            for (HashMap.Entry<Socket, String> entry : authentication.entrySet()) {
                Socket client = entry.getKey();
                String pseudonyme = entry.getValue();

                // On n'envoie pas le message au client envoyant le message, sauf si c'est celui de bienvenue
                if(client != this.client || firstMessage == true){
                    // On ouvre un outputstream du client et on lui envoie le message
                    DataOutputStream outs = new DataOutputStream(client.getOutputStream());
                    outs.writeUTF(message);
                }
            }
        }
    }
}
