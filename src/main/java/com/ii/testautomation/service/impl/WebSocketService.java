package com.ii.testautomation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public void sendMessage(Integer value){
      //  System.out.println("========"+value);
      //  simpMessagingTemplate.convertAndSend("/queue/percentage",value);
    }
}
