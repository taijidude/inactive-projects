package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import exception.DataAlreadyExistsException;
import exception.DataNotFoundException;
import model.Category;
import model.CategoryIF;
import service.CategoryServiceIF;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Controller
@EnableWebMvc
public class CategoryController {
    
    @Autowired
    private CategoryServiceIF service;

    /**
     * Legt eine neue Category auf der Datenbank an.  
     * @param Der Text der neuen Category muss im RequestBody übergeben
     * @return Die Id der neuen Category. 
     * @throws DataAlreadyExistsException 
     */
    @RequestMapping(value="/category", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody long persist(@RequestBody String categoryText) throws DataAlreadyExistsException {
    	return service.persist((CategoryIF)new Category(categoryText));
    }
    
    /**
     * Liest eine Category anhand ihrer Id aus.
     * @param Die Id der Category.
     * @return Die Category im Jsonformat. Beispiel: {"id":191,"text":"FASHION"}
     * @throws DataNotFoundException
     */
    @RequestMapping(value="/category/{id}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Category get(@PathVariable long id) throws DataNotFoundException {
    	return (Category) service.get(id);
    }
    
    /**
     * Löscht eine Kategorie anhand einer entsprechenden Id. 
     * @param Die Id der zu löschenden Kategory.
     * @return 
     * @throws
     */
    @RequestMapping(value="/category/{id}", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
    	service.delete(id);
    }
    
    @ExceptionHandler(DataAlreadyExistsException.class)
	@ResponseStatus(value=HttpStatus.CONFLICT)
    private void handleDataAlreadyExistsException() {}
    
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    private void handleDataNotFoundException() {}
}