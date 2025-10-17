package cz.ivosahlik.library.controller;

import cz.ivosahlik.library.annotation.CurrentUser;
import cz.ivosahlik.library.annotation.UserType;
import cz.ivosahlik.library.entity.Message;
import cz.ivosahlik.library.requestmodels.AdminQuestionRequest;
import cz.ivosahlik.library.service.MessagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/messages")
public class MessagesController {

    private final MessagesService messagesService;

    @PostMapping("/secure/add/message")
    public void postMessage(@CurrentUser String userEmail,
                            @RequestBody Message messageRequest) {
        messagesService.postMessage(messageRequest, userEmail);
    }

    @PutMapping("/secure/admin/message")
    public void putMessage(@CurrentUser String userEmail,
                           @UserType String userType,
                           @RequestBody AdminQuestionRequest adminQuestionRequest) throws Exception {
        if (userType == null || !userType.equals("admin")) {
            throw new Exception("Administration page only.");
        }
        messagesService.putMessage(adminQuestionRequest, userEmail);
    }

}














