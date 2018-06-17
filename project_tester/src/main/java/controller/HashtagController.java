package controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import exception.DataNotFoundException;
import model.Hashtag;
import model.HashtagIF;
import service.HashTagServiceIF;

/**
 * 
 * @author John
 *
 */
@Controller
@EnableWebMvc
public class HashtagController {

	@Autowired
	private HashTagServiceIF service;
	
	/**
	 * Erstellt einen neuen Hashtag in der Datenbank.
	 * @param Den Text des Hashtags
	 * @return Die Id des neu erstellten hashtags.
	 * @throws
	 */
	@RequestMapping(value="/hashtag", method=RequestMethod.POST)
	public @ResponseBody Long persist(@RequestBody String hashtag) {
		return service.persist(hashtag);
	}
	
	/**
	 * Liest den Text eines Hashtags anhand seiner ID aus
	 * @param Die Id des Hashtags
	 * @return Die Daten des Hashtags im Json Format {"id":243,"text":"#hashtag"}
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value="/hashtag/{id}", method=RequestMethod.GET)
	public @ResponseBody Hashtag get(@PathVariable long id) throws DataNotFoundException {
		  return service.get(id);
	}
	
	/**
	 * Liest alle Hashtags aus.
	 * @param --
	 * @return Eine Liste alle Hashtags im Json Format
	 * @throws  
	 */
	@RequestMapping(value="/hashtag", method=RequestMethod.GET)
	public @ResponseBody Set<Hashtag> getAll() {
		Set<Hashtag> result = new HashSet<Hashtag>();
		Set<HashtagIF> allHashtags = service.getAll();
		Iterator<HashtagIF> resultIterator = allHashtags.iterator();
		while(resultIterator.hasNext()) {
			Hashtag hashtag = (Hashtag) resultIterator.next();
			result.add(hashtag);
		}
		return result;
	}
	
	/**
	 * L�scht einen Hashtag anhand einer ID. 
	 * @param Die Id eines Hashtags
	 * @return ---
	 * @throws
	 */
	@RequestMapping(value="/hashtag/{id}", method=RequestMethod.DELETE)
	public void delete(@PathVariable long id) {
		service.delete(id);
	}
	
	@ExceptionHandler(DataNotFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public void handleDataNotFoundException() {
		//Gibt nur einen Status zur�ck...
	}
	
}