package capstone_back.utils.dto;

import capstone_back.domain.Message;
import lombok.Data;

@Data
public class MessageReturnForm {
    private Long message_id;
    private String title;
    private String content;
    private String sender;
    private String board_title;

    public MessageReturnForm createMessageReadDto(Message message) {
        this.message_id = message.getId();
        this.title = message.getTitle();
        this.board_title = message.getBoardtitle();
        this.content = message.getContent();
        this.sender = message.getSender().getEmail();
        return this;
    }
}
