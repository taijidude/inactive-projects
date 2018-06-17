package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import model.UploadIF;
import model.Upload;
import service.UploadServiceIF;

/**
 * TODO: Es sollte sich eine Liste von Files l�schen lassen und nicht nur immer eins pro Aufruf
 * @author John
 *
 */

@Controller
@EnableWebMvc
public class UploadController {
	
	@Autowired
	private UploadServiceIF uploadService; 	
	
	/**
	 * L�d eine Menge von Dateien auf den Server. Die Datei selber wird im Verzeichnis Upload
	 * gespeichert und es wird ein Datensatz in der Uploadtabelle angelegt. Die
	 * Datei bekommt als neuen Namen die ID aus der Datenbank. 
	 * @param Die Liste der Dateien
	 * @param Die Id des Accounts, der die Dateien hochgeladen hat.
	 * @return Eine Liste der Dateinamen und ihrer zugeordneten Id's
	 * @throws IOException
	 */
	@RequestMapping(value="/upload/{userId}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<Long, String> upload(@RequestParam List<MultipartFile> files, @PathVariable int userId) throws IOException {
		UploadIF upload = new Upload(userId);
		extractFileData(upload, files);
		Map<Long, String> saveResult = uploadService.save(upload);
		return saveResult;
	}
	
	/**
	 * L�scht eine Datei und ihren zugeh�rigen Datensatz.
	 * @param die Id's des Uploads
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/upload/{uploadId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable int uploadId) throws IOException {
		List<String> uploadsToDelete = new ArrayList<String>();
		uploadsToDelete.add(Integer.valueOf(uploadId).toString());
		uploadService.delete(uploadsToDelete);
	}
	/**
	 * Liest den Dateipfad auf dem Server zur Datei aus.
	 * @param Die Id des Uploads
	 * @return Der realtive Pfad auf dem Server
	 * @throws
	 */
	@RequestMapping(value="/upload/path/{uploadId}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String getPath(@PathVariable int uploadId) {
		return uploadService.getFilePath(Integer.valueOf(uploadId).toString());
	}
	
	private void extractFileData(UploadIF upload, List<MultipartFile> files) throws IOException {
		for (MultipartFile multipartFile : files) {
			String originalFilename = multipartFile.getOriginalFilename();
			byte[] bytes = multipartFile.getBytes();
			upload.addFileData(originalFilename, bytes);
		}
	}
}