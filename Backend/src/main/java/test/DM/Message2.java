package test.DM;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_messages")
public class Message2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String username;

    @Column(length = 100)
    private String toWhom;

    @Lob
    private String content;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();

    @Column(name = "Message_Type")
    private MessageType messageType;


    public Message2() {};

    public Message2(String userName, String content, MessageType messageType) {
        this.username = username;
        this.toWhom = null;
        this.content = content;
        this.messageType = messageType;
    }

    public Message2(String userName, String toWhom, String content, MessageType messageType) {
        this.username = userName;
        this.toWhom = toWhom;
        this.content = content;
        this.messageType = messageType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToWhom() {
        return toWhom;
    }

    public void setToWhom(String toWhom) {
        this.toWhom = toWhom;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public MessageType getMsgType() {
        return messageType;
    }

    public void setMsgType(MessageType msgType) {
        this.messageType = msgType;
    }
}