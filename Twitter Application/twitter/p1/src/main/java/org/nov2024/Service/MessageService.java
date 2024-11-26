package org.nov2024.Service;

import jakarta.transaction.Transactional;
import org.nov2024.Models.Message;
import org.nov2024.Repo.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional
    public Message save(Message message){
        return messageRepository.save(message);
    }

    public List<Map<String,String>> getAllMessagesForSubscriber(Integer subscriberid) {

        List<Object[]> messageObjects = messageRepository.messagesForSubs(subscriberid);
        List<Map<String,String>> mapOfMessages = new ArrayList<>();

        messageObjects.forEach(objects -> {
            String content = objects[0].toString();
            String sendername = objects[1].toString();

            Map<String,String> map = new HashMap<>();
            map.put("content",content);
            map.put("sender",sendername);

            mapOfMessages.add(map);
        });

        return mapOfMessages;
    }

}
