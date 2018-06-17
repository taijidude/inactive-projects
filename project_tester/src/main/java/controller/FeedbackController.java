package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import service.FeedbackServiceIF;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
@EnableWebMvc
public class FeedbackController {
    
    @Autowired
    private FeedbackServiceIF service;

}
