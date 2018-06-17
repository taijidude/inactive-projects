package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.FeedbackDaoIF;

@Service
public class FeedbackService implements FeedbackServiceIF {
    
    @Autowired
    private FeedbackDaoIF dao;
}
