package service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import model.UploadIF;

public interface UploadServiceIF {
	Map<Long, String> save(UploadIF upload) throws IOException;
	
	void delete(List<String> uploadsToDelete) throws IOException;

	String getFilePath(String id);
	
}