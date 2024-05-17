package test.DM;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import test.Users.UserRepository;
import test.Users.User;

@RestController      
@ServerEndpoint(value = "/userchat/{username1}/{username2}")
public class ChatController {

    private static DMRepository msgRepo;

    private static UserRepository userRepository;

    @Autowired
    public void setMessageRepository(DMRepository repo) {
        msgRepo = repo; 
    }

    @Autowired
    public void setUserRepository(UserRepository repo) {
        userRepository = repo;
    }

    private static Map<Session, String> sessionUserMap = new Hashtable<>();
    private static Map<String, Session> userSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("username1") String username, @PathParam("username2") String destUsername) throws IOException {
        String message = "";
        if (userRepository.findByUsername(username) != null && userRepository.findByUsername(destUsername)!=null) {

            logger.info("Entered into Open");

            sessionUserMap.put(session, username);
            userSessionMap.put(username, session);
            sendMessageToParticularUser(username, getChatHistory());
        } 
    }


    @OnMessage
    public void onMessage(Session session, String message, @PathParam("username2") String destUsername) throws IOException {

        logger.info("Entered into Message: Got Message:" + message);
        String username = sessionUserMap.get(session);
            if (destUsername != null && userRepository.findByUsername(username) != null && userRepository.findByUsername(destUsername) != null) {
                sendMessageToParticularUser(destUsername, "[DM] " + username + ": " + message);
                sendMessageToParticularUser(username, "[DM] " + username + ": " + message);
                msgRepo.save(new Message2(username, destUsername, message, MessageType.DIRECT_MESSAGE));
                return;
            }
    }


    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");
        
        String username = sessionUserMap.get(session);
        sessionUserMap.remove(session);
        userSessionMap.remove(username);

        String message = username + " disconnected";
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
    	
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }


    private void sendMessageToParticularUser(String userName, String message) {
        try {
            if (userSessionMap.get(userName) != null){
                userSessionMap.get(userName).getBasicRemote().sendText(message);
            }
        }
        catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }


    private void broadcast(String message) {
        sessionUserMap.forEach((session, user) -> {
            try {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }

        });

    }

    private String getChatHistory() {
        List<Message2> messages = msgRepo.findAll();

        StringBuilder sb = new StringBuilder();
        if(messages != null && messages.size() != 0) {
            for (Message2 message : messages) {
                if (message.getMsgType() != MessageType.DIRECT_MESSAGE)
                    sb.append(message.getUsername() + ": " + message.getContent() + "\n");
            }
        }
        return sb.toString();
    }
}
