package test.DM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import test.Users.User;
import test.Users.UserRepository;

import java.util.List;

@RestController
public class ChatController2 {

    @Autowired
    DMRepository msgRepo;

    @Autowired
    UserRepository userRepository;
    
    @GetMapping("/chat/getDirectMessageHistory/{user1}/{user2}")
    String getDirectChatHistory(@PathVariable String user1, @PathVariable String user2) {
        User user = userRepository.findByUsername(user1);
        User otherUser = userRepository.findByUsername(user2);

        if (user != null && userRepository.findAll().contains(user)
                && otherUser != null && userRepository.findAll().contains(otherUser)) {
            return getDMHistory(user, otherUser);
        }
        else {
            if (user == null)
                System.out.println("UserProfile is NULL");
            if (otherUser == null)
                System.out.println("OtherUser is NULL");
            if (user != null && !userRepository.findAll().contains(user))
                System.out.println("User:" + user.getUsername() + "not found!");
            if (otherUser != null && !userRepository.findAll().contains(otherUser))
                System.out.println("User:" + otherUser.getUsername() + "not found!");
        }
        return null;
    }

    private String getDMHistory(User user, User otherUser) {
        List<Message2> directMessages = msgRepo.findMessagesByMessageType(MessageType.DIRECT_MESSAGE);

        StringBuilder sb = new StringBuilder();
        for (Message2 message : directMessages){
            if ((message.getUsername().equals(user.getUsername()) && message.getToWhom().equals(otherUser.getUsername()))
                    || (message.getUsername().equals(otherUser.getUsername()) && message.getToWhom().equals(user.getUsername())))
                sb.append("[DM] " + message.getUsername() + ": " + message.getContent() + "\n");

        }
        return sb.toString();
    }
}