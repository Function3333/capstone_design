package capstone_back.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String boardtitle;

    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Account receiver;

    public void setSender(Account sender) {
        this.sender = sender;
        sender.getSendMessageList().add(this);
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
        receiver.getReceiveMessageList().add(this);
    }

    public Message createMessage(String title, String content, String boardtitle) {
        this.title = title;
        this.content = content;
        this.boardtitle = boardtitle;
        return this;
    }

}

