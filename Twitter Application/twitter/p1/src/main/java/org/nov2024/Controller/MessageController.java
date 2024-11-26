package org.nov2024.Controller;

import org.nov2024.Models.Message;
import org.nov2024.Models.User;
import org.nov2024.Repo.MessageRepository;
import org.nov2024.Repo.UserRepository;
import org.nov2024.Service.MessageService;
import org.nov2024.Service.UserManagementService;
import org.nov2024.Utils.CustomTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private MessageService messageService;
    private UserManagementService userManagementService;

    @Autowired
    public MessageController(MessageService messageService, UserManagementService userManagementService) {
        this.messageService = messageService;
        this.userManagementService = userManagementService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> addMessage(@RequestBody String content, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract the token after "Bearer "
            String token = authorizationHeader.substring(7);

            String username = CustomTokenUtil.extractUsername(token);
            User user = userManagementService.findByUsername(username).get();

            content = content.split(":")[1]
                    .replace("{","")
                    .replace("}","")
                    .replace("\"","")
                    .trim();

            Message message = new Message();

            message.setContent(content);
            message.setProducerId(user);
            message.setCreatedt(new Date(System.currentTimeMillis()));
            messageService.save(message);

            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("Bad Request");
        }
    }

    @GetMapping("/getmessageForSubs")
    public ResponseEntity<?> getAllMessagesForSubscriber(@RequestHeader(value = "Authorization", required = false) String authorizationHeader){

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract the token after "Bearer "
            String token = authorizationHeader.substring(7);

            String username = CustomTokenUtil.extractUsername(token);

            User user = userManagementService.findByUsername(username).orElseThrow(
                    () -> new IllegalArgumentException(username + "not found"));

            List<Map<String,String>> messages = messageService.getAllMessagesForSubscriber(user.getUserid());

            return ResponseEntity.ok(messages);
        }else {
            return ResponseEntity.badRequest().body("Bad Request");
        }

    }
}