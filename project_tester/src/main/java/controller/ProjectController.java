package controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import exception.DataNotFoundException;
import model.Answer;
import model.AnswerIF;
import model.Feedback;
import model.FeedbackIF;
import model.Project;
import model.ProjectIF;
import service.ProjectServiceIF;

/**
 * TODO: Es sollten alle Hashtags zu einem Projekt ausgelesen werden k�nnen. 
 * TODO: Es sollten alle Projekte zu einem Hashtag ausgelesen werden k�nnen. 
 * TODO: Die Zuordnung eines Hashtags zu einem Projekt sollte wieder aufgehoben werden k�nnen.
 * TODO: Es sollte eine Kategory einem Projekt zugeordnet werden k�nnen. 
 * TODO: Es sollten alle Kategorien zu einem Projekt ausgelesen werden k�nne. 
 * TODO: Es sollten alle Projekte zu einer Kategorie ausgelesen werden k�nnen. 
 * TODO: Es sollte ein Upload mit einem Projekt verbunden werden k�nnen.
 * TODO: Es sollten alle Bilder f�r eine Projektid ausgelesen werden k�nnen.
 * TODO: Die Zuordnung eines Uploads zu einem Projekt sollte sich l�schen lassen. 
 *  
 * @author John
 */
@Controller
@EnableWebMvc
public class ProjectController {
	
	private Calendar calender = Calendar.getInstance();
	
	@Autowired
	private ProjectServiceIF service;
	
	public static final String URL_PROJECT_CREATION = "/v1/api/views/projects";
	
	@CrossOrigin
	@RequestMapping(value=URL_PROJECT_CREATION, method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	private @ResponseBody String testErstellung(HttpServletRequest request) throws IOException, ServletException {
		
		System.out.println("===Multipart Logging ===");
		System.out.println("Request Klasse:"+request.getClass().toString()); ;
		
		return "test";
	}
	
	
	/**
	 * Speichert ein neues Projekt auf der Datenbank.   
	 * @param Die Daten f�r das zu speichernde Json werden im Requestbody �bergeben.
	 * Beispiel: {"id":1,"name":"project1",
	 * "location":"berlin1",
	 * "description":"beschreibung1",
	 * "specialFeatures":"Das macht es so besonders!1",
	 * "motivation":"motivation1",
	 * "expirationDate":1456950854730,
	 * "accountId":1,"hashtags":[]}
	 * @return Id des gespeicherten Projects
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/project", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody long create(@RequestBody Project project) throws DataNotFoundException {
		return service.create(project);
	}
	
	/**
	 * Aktualisiert ein bestehendes Projekt. Wie bei der Projekterstelltung muss das komplette
	 * Json an die Rest API �bergeben werden. 
	 * @param Json Beispiel:
	 * {"id":1,"name":"project1",
	 * "location":"berlin1",
	 * "description":"beschreibung1",
	 * "specialFeatures":"Das macht es so besonders!1",
	 * "motivation":"motivation1",
	 * "expirationDate":1456950854730,
	 * "accountId":1,"hashtags":[]}
	 * @return
	 * @throws DataNotFoundException 
	 */
	@RequestMapping(value="/project", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void update(@RequestBody Project project) throws DataNotFoundException {
		service.update(project);
	}

	/**
	 * Liest alle bestehenden Projekte aus.
	 * @param 
	 * @return Eine JSON Liste aller bislang angelegten Projekte
	 * @throws
	 */
	@RequestMapping(value="/project", method=RequestMethod.GET)
	public @ResponseBody List<ProjectIF> getAll() {
		return service.getAll();
	}
	
	/**
	 * Liest ein bestimmtes Projekt anhand einer ProjektId aus. 
	 * @param Die Id des Projekts
	 * @return Die Json Daten des Projekts 
	 * {"id":1,"name":"project1",
	 * "location":"berlin1",
	 * "description":"beschreibung1",
	 * "specialFeatures":"Das macht es so besonders!1",
	 * "motivation":"motivation1",
	 * "expirationDate":1456950854730,
	 * "accountId":1,"hashtags":[]}
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/project/{id}", method=RequestMethod.GET)
	public @ResponseBody ProjectIF get(@PathVariable long id) throws DataNotFoundException {
		return service.get(id);
	}

	/**
	 * L�scht ein bestimmtes Projekt anhand seiner ProjektId
	 * @param Die Id des Projekts
	 * @return
	 * @throws
	 */
	@RequestMapping(value="/project/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable long id) {
		service.delete(id);
	}
	
	/**
	 * F�gt ein existierendes Hashtag anhand seiner Id einem bestimmten Projekt hinzu.
	 * @param projectId - Die Id eines angelegten Projekts 
	 * @param hashtagId - Die Id eines angelegten Hashtag
	 * @return
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/project/{projectId}/hashtags/{hashtagId}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addHashtag(@PathVariable long projectId, @PathVariable long hashtagId) throws DataNotFoundException {
		service.addHashtag(projectId, hashtagId);
	}
	
	/**
	 * Entfernt die Zuordnung eines Hashtag zu einem bestimmten Projekt 
	 * anhand der ProjectId und der Hashtag Id. 
	 * @param Die Id des Projects
	 * @param Die Id des Hashtags
	 * @return
	 * @throws DataNotFoundException 
	 */
	@RequestMapping(value="/project/{projectId/hashtags/{hashtagId}}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void removeHashtag(@PathVariable long projectId, @PathVariable long hashtagId) throws DataNotFoundException {
		service.removeHashtag(projectId, hashtagId);
	}
	
	/**
	 * Ordnet eine existierende Kategory anhand der Kategory ID einem Projekt zu. 
	 * @param Die Id des Projekts
	 * @param Die Id der Kategory.
	 * @return
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/project/{projectId}/categories/{categoryId}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addCategory(@PathVariable long projectId, @PathVariable long categoryId) throws DataNotFoundException {
		service.addCategory(projectId, categoryId);
	}
	
	/**
	 * L�scht die Zuordnung einer Kategory zu einem Projekt anhand der Id's
	 * @param projectId
	 * @param categoryId
	 * @return
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/project/{projectId}/categories/{categoryId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void removeCategory(@PathVariable long projectId, @PathVariable long categoryId) throws DataNotFoundException {
		service.removeCategory(projectId, categoryId);
	}
	
	/**
	 * Ordnet einem Project einen Upload anhand der UploadId zu.
	 * @param projectId: Die Id des Projects
	 * @param uploadId: Die Id des Uploads
	 * @return
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/project/{projectId}/uploads/{uploadId}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addUpload(@PathVariable long projectId, @PathVariable long uploadId) throws DataNotFoundException {
		service.addUpload(projectId, uploadId);
	}
	
	/**
	 * L�scht die Zuordnung eines Uploads zu einem Projekt anhand der Upload und der Project Id
	 * @param projectId Die Id des Projects
	 * @param uploadId Die Id des Uploads
	 * @return
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/project/{projectId}/uploads/{uploadId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void removeUpload(@PathVariable long projectId, @PathVariable long uploadId) throws DataNotFoundException {
		service.removeUpload(projectId, uploadId);
	}
	
	/**
	 * F�gt ein Feedback einem Project hinzu.
	 * @param Die Id des Projects
	 * @param Die Id des Accounts der das Feedback gibt
	 * @param Der FeedbackText muss im RequestBody �bergeben werden
	 * @return Die Id des neu angelegten Feedbacks
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/project/{projectId}/feedback/{accountId}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody long addFeedback(@PathVariable long projectId, @PathVariable long accountId, @RequestBody String text) throws DataNotFoundException {
		//Beim Anlegen eines Feedbacks ist die Id unerheblich. Sie wird sowieso durch eine Hibernate Id ersetzt. Deshlab wird hier eine 0 �bergeben.
		FeedbackIF feedback = new Feedback(0,projectId,accountId, text, calender.getTime());
		return service.addFeedback(feedback);
	}
	/**
	 * Liest ein Feedback anhand der ID aus.
	 * @param Die Id des gesuchten Feedbacks
	 * @return Die Feedbackdaten im Json Format. 
	 * Beispiel: 
	 * {"projectId":85,"answerId":0,
	 * "accountId":43,
	 * "text":"Das ist ein Testfeedback!",
	 * "creationDate":"2016-06-11 15:00",
	 * "feedbackId":86}
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/feedback/{feedbackId}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Feedback getFeedback(@PathVariable long feedbackId) throws DataNotFoundException {
		return (Feedback) service.getFeedback(feedbackId);
	}
	
	/**
	 * L�scht ein Feedback anhand der �bergebenen Id
	 * @param Die Id des zu l�schenden Feedbacks 
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/feedback/{feedbackId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteFeedback(@PathVariable long feedbackId) throws DataNotFoundException {
		service.deleteFeedback(feedbackId);
	}
	
	/**
	 * Aktualisiert ein Feedback 
	 * @param Das komplette Feedback im Json Format
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/feedback", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void updateFeedback(@RequestBody Feedback feedback) throws DataNotFoundException {
		service.updateFeedback((FeedbackIF) feedback);
	}
	
	/**
	 * Speichert die Antwort auf ein Feedback. 
	 * @param Die Id des zubeantwortenden Feedbacks
	 * @param Die Id des Accounts, von dem die Antwort verfasst wird. 
	 * @param Der Antworttext wird im Body �bergeben. 
	 * @return
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/feedback/{feedbackId}/answer/{accountId}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody long persistAnswer(@PathVariable long feedbackId, @PathVariable long accountId, @RequestBody String text) throws DataNotFoundException {
		//Die Id der Answer ist egal, weil es nach dem Persist eh eine neue gibt.
		//Deshalb �bergebe ich hier eine 0. 
		Answer answer = new Answer(0,accountId, feedbackId, text, null);
		return service.pesistAnswer((AnswerIF)answer);
	}
	
	/**
	 * Liest eine Antwort anhand ihrer Id aus
	 * @param Die Id der auszulesenden Antwort
	 * @return Die Daten der Antwort im Json Format. 
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/answer/{answerId}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Answer getAnswer(@PathVariable long answerId) throws DataNotFoundException {
		return (Answer) service.getAnswer(answerId);
	}
	
	/**
	 * 
	 * @param answer
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/answer", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void updateAnswer(@RequestBody Answer answer) throws DataNotFoundException {
		service.updateAnswer((AnswerIF)answer);
	}
	
	@RequestMapping(value="/answer/{answerId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void removeAnswer(@PathVariable long answerId) throws DataNotFoundException {
		service.removeAnswer(answerId);
	}
	
	@ExceptionHandler(DataNotFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public void handleDataNotFoundException() {
		//Gibt nur einen Status zur�ck...
	}
}