package controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.Like;
import model.LikeIF;
import model.LikeItemTyp;
import service.LikeServiceIF;

@Controller
@EnableWebMvc
public class LikeController {
    
    @Autowired
    private LikeServiceIF service;

    @RequestMapping(value="/like/project/{projectId}/{userId}", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody long persistProjectLike(@PathVariable long projectId, @PathVariable long userId) throws DataAlreadyExistsException, DataNotFoundException {
    	return service.persistLike(new Like(LikeItemTyp.PROJECT, userId, projectId));
    }
    
    @RequestMapping(value="/like/feedback/{feedbackId}/{userId}", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody long persistFeedbackLike(@PathVariable long feedbackId, @PathVariable long userId) throws DataAlreadyExistsException, DataNotFoundException {
    	return service.persistLike(new Like(LikeItemTyp.FEEDBACK, userId, feedbackId));
    }
    
    @RequestMapping(value="/like/answer/{answerId}/{userId}", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody long persistAnswerLike(@PathVariable long answerId, @PathVariable long userId) throws DataAlreadyExistsException, DataNotFoundException {
    	return service.persistLike(new Like(LikeItemTyp.ANSWER, userId, answerId));
    }
    
    @RequestMapping(value="/like/{likeId}", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteLikeById(@PathVariable long likeId) {
    	service.deleteLikeById(likeId);
    }
    
    @RequestMapping(value="/like/project/{projectId}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Like> getProjectLikesById(@PathVariable long projectId) throws DataNotFoundException {
    	List<Like> result = new ArrayList<Like>();
    	List<LikeIF> likesById = service.getLikesById(projectId, LikeItemTyp.PROJECT);
    	for (LikeIF likeIF : likesById) {
    		result.add((Like)likeIF);
		}
    	return result;
    }
    
    @RequestMapping(value="/like/feedback/{feedbackId}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Like> getFeedbackLikesById(@PathVariable long feedbackId) throws DataNotFoundException {
    	List<Like> result = new ArrayList<Like>();
    	List<LikeIF> likesById = service.getLikesById(feedbackId, LikeItemTyp.FEEDBACK);
    	for (LikeIF likeIF : likesById) {
			result.add((Like)likeIF);
		}
    	return result;
    }
    
    @RequestMapping(value="/like/answer/{answerId}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Like> getAnswerLikesById(@PathVariable long answerId) throws DataNotFoundException {
    	List<Like> result = new ArrayList<Like>();
    	List<LikeIF> likesById = service.getLikesById(answerId, LikeItemTyp.ANSWER);
    	for (LikeIF likeIF : likesById) {
			result.add((Like)likeIF);
		}
		return result;
    }
    
    @ExceptionHandler(DataNotFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public void handleDataNotFoundException() {
		//Gibt nur einen Status zurück...
	}
    
}