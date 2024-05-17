package test.Chat;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import test.Users.User;
import test.Users.UserRepository;

@Controller
@ServerEndpoint(value = "/chat/{userID}/{roomID}")
public class ChatSocket {
    private static MessageRepository msgRepo; 
    private static UserRepository  userRepository;

	@Autowired
	public void setMessageRepository(MessageRepository repo) {
		msgRepo = repo;  
	}

    @Autowired
	public void setUserRepository(UserRepository repo) {
		userRepository = repo;  
	}

    private static Map<Integer, ArrayList<Session>> roomIdSessionMap = new Hashtable<>();
    private static Map<Session, User> sessionUserMap = new Hashtable<>();
    private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

    @OnOpen
	public void onOpen(Session session, @PathParam("userID") Long userID, @PathParam("roomID") Integer roomID) 
      throws IOException {

	    logger.info("Entered into Open");
        User user = userRepository.findById(userID).get();
        if(!roomIdSessionMap.containsKey(roomID)){
            roomIdSessionMap.put(roomID, new ArrayList<Session>());
        }
        ArrayList<Session> sesList = roomIdSessionMap.get(roomID);
        sesList.add(session);
        sessionUserMap.put(session, user);

		//Send chat history to the newly connected user
		List<Message> history = msgRepo.findByRoomIDOrderBySentAsc(roomID);
        try {
            ObjectMapper mapper = new ObjectMapper();
            session.getBasicRemote().sendText(mapper.writeValueAsString(history));
        } 
        catch (Exception e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
		
	}

    @OnMessage
	public void onMessage(Session session, String message, @PathParam("roomID") Integer roomID) throws IOException {
		logger.info("Entered into Message: Got Message:" + message);
		User user = sessionUserMap.get(session);
        ArrayList<Session> sesList = roomIdSessionMap.get(roomID);
        Message msg = new Message(user, message, roomID, user.getUsername());
        ObjectMapper mapper = new ObjectMapper();
        String msgJSON = mapper.writeValueAsString(msg);
        msgRepo.save(msg);
        for(Session s : sesList){
            try {
                s.getBasicRemote().sendText(msgJSON);
            } 
            catch (Exception e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }
        }
		
	}

    @OnClose
	public void onClose(Session session, @PathParam("roomID") Integer roomID) throws IOException {
		logger.info("Entered into Close");

		sessionUserMap.remove(session);
        ArrayList<Session> sesList = roomIdSessionMap.get(roomID);
		sesList.remove(session);

	}

    @OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}


}
