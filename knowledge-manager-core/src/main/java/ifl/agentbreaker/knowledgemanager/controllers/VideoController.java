package ifl.agentbreaker.knowledgemanager.controllers;

import ifl.agentbreaker.knowledgemanager.services.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/video")
public class VideoController
{
    @Autowired
    private VideoService service;
}
