package capstone_back.service;

import capstone_back.domain.Account;
import capstone_back.domain.Message;
import capstone_back.repository.AccountRepository;
import capstone_back.utils.dto.MessageReturnForm;
import capstone_back.jwt.JwtService;
import capstone_back.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;

    @Transactional
    public void sendMessage(Account sender, Account receiver, Message message) {
        message.setSender(sender);
        message.setReceiver(receiver);
        messageRepository.save(message);
    }

    /*수신자 이메일이 존재하는지 확인*/
    public boolean validateReceiverIsNull(String email) {
        List<Account> byEmail = accountRepository.findByEmail(email);

        if(byEmail.isEmpty()) return true;
        return false;
    }

    public List<MessageReturnForm> getMessages(String email) {
        List<Account> byEmail = accountRepository.findByEmail(email);
        Account account = byEmail.get(0);

        List<Message> byReceiverId = messageRepository.findByReceiverId(account.getId());
        List<MessageReturnForm> messageList = new ArrayList<>();

        for (Message message : byReceiverId) {
            MessageReturnForm messageReturnDto = new MessageReturnForm().createMessageReadDto(message);
            messageList.add(messageReturnDto);
        }

        return messageList;
    }

    public List<MessageReturnForm> getSendMessages(String email) {
        List<Account> byEmail = accountRepository.findByEmail(email);
        Account account = byEmail.get(0);

        List<Message> bySenderId = messageRepository.findBySenderId(account.getId());
        List<MessageReturnForm> messageList = new ArrayList<>();

        for(Message message : bySenderId) {
            MessageReturnForm messageReturnDto = new MessageReturnForm().createMessageReadDto(message);
            messageList.add(messageReturnDto);
        }

        return messageList;
    }

    public MessageReturnForm getMessage(Long id) {
        Message message = messageRepository.findById(id);

        return new MessageReturnForm().createMessageReadDto(message);
    }


    public boolean validateAuthority(Long message_id, Account requestAccount) {
        Message message = messageRepository.findById(message_id);

        Account sender = message.getSender();
        Account receiver = message.getReceiver();

        if(requestAccount.equals(sender) || requestAccount.equals(receiver)) {
            return true;
        }

        return false;
    }

    public boolean validateSendToMe(String receiver, String userEmail) {
        if(userEmail.equals(receiver)) {
            return true;
        }
        return false;
    }

}