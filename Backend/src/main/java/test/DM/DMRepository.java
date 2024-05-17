package test.DM;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DMRepository extends JpaRepository<Message2, Long>{
    List<Message2> findMessagesByMessageType(MessageType messageType);
}