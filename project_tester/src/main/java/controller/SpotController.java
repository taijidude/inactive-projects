package controller;

import java.util.List;

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

import exception.DataNotFoundException;
import model.DetailLevel;
import model.Spot;
import model.SpotIF;
import service.SpotServiceIF;

@Controller
public class SpotController {

	@Autowired
	private SpotServiceIF service;
	
	@RequestMapping(value="/v1/api/spots/token", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<SpotIF> getAllToken() {
		return service.getAll(DetailLevel.TOKEN);
	}	
	
	@RequestMapping(value="/v1/api/spots/free", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<SpotIF> getAllFree() {
		return service.getAll(DetailLevel.FREE);
	}
	
	@RequestMapping(value="/v1/api/spots/token/{id}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Spot getSpot(@PathVariable long id) throws DataNotFoundException {
		return (Spot) service.getById(id);
	}
	
	@RequestMapping(value="/v1/api/spots/token", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody long peristSpot(@RequestBody Spot spot) {
		return service.persist((SpotIF) spot);
	}
	
	@RequestMapping(value="/v1/api/spots/token/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable long id) {
		service.delete(id);
	}
	
	@RequestMapping(value="/v1/api/spots/token", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void update(@RequestBody Spot spot) {
		service.update((SpotIF)spot);
	}
	
	@ExceptionHandler(DataNotFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	public void handleDataNotFoundException() {
		//Nichts machen. Einfach den Status zurück geben
	}
}