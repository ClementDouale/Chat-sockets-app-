package com.example.ai16_project_chat.websocket;

import org.springframework.stereotype.Component;
import java.util.Hashtable;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

@ServerEndpoint(value="/chatserver/{channelName}/{username}", configurator=ChatServer.EndpointConfigurator.class)
@Component
public class ChatServer {


    private static ChatServer singleton = new ChatServer();

    private ChatServer() {
    }

    /**
     * Acquisition de notre unique instance ChatServer
     */
    public static ChatServer getInstance() {
        return ChatServer.singleton;
    }

    /**
     * On maintient toutes les sessions utilisateurs dans une collection.
     */
    private Hashtable<String, Session> sessions = new Hashtable<>();

    /**
     * Cette méthode est déclenchée à chaque connexion d'un utilisateur.
     */
    @OnOpen
    public void open(Session session, @PathParam("channelName") Integer channelName, @PathParam("username") String username) {
        sendMessage( ">>> " + username + " a rejoint le chat.", channelName);
        session.getUserProperties().put( "channelName", channelName);
        sessions.put( session.getId(), session );
    }

    /**
     * Cette méthode est déclenchée à chaque déconnexion d'un utilisateur.
     */
    @OnClose
    public void close(Session session, @PathParam("username") String username) {
        Integer channelName = (Integer) session.getUserProperties().get( "channelName" );
        sessions.remove( session.getId() );
        sendMessage(">>> " + username + " a quitté le chat." , channelName);
    }

    /**
     * Cette méthode est déclenchée en cas d'erreur de communication.
     */
    @OnError
    public void onError(Throwable error) {
        System.out.println( "Error: " + error.getMessage() );
    }

    /**
     * Cette méthode est déclenchée à chaque réception d'un message utilisateur.
     */
    @OnMessage
    public void handleMessage(String message, Session session, @PathParam("username") String username) {
        Integer channelName = (Integer) session.getUserProperties().get( "channelName" );
        String fullMessage = username +": " + message;

        sendMessage( fullMessage , channelName);
    }

    /**
     * Une méthode privée, spécifique à notre exemple.
     * Elle permet l'envoie d'un message aux participants de la discussion.
     */
    private void sendMessage(String fullMessage, Integer channel) {

        // Affichage sur la console du server Web.
        System.out.println( fullMessage );

        for( Session session : sessions.values() ) {
        try {
            Integer userChannel = (Integer) session.getUserProperties().get("channelName");
            // vérification pour envoyer le message uniquement sur le channel sur lequel on se situe
            if(channel==userChannel) {
                session.getBasicRemote().sendText(fullMessage);
            }
            } catch (Exception exception) {
                System.out.println("ERROR: cannot send message to " + session.getId());
            }
        }
    }

    /**
     * Permet de ne pas avoir une instance différente par client.
     * ChatServer est donc gérer en "singleton" et le configurateur utilise ce singleton.
     */
    public static class EndpointConfigurator extends ServerEndpointConfig.Configurator {
        @Override
        @SuppressWarnings("unchecked")
        public <T> T getEndpointInstance(Class<T> endpointClass) {
            return (T) ChatServer.getInstance();
        }
    }
}
