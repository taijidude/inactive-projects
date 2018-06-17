package service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.UploadDaoIF;
import model.UploadIF;

@Service
public class UploadService implements UploadServiceIF {
	
	@Autowired
	private UploadDaoIF dao; 
	
	
	@Override
	public Map<Long, String> save(UploadIF upload) throws IOException {
		return dao.persist(upload);
	}


	@Override
	public void delete(List<String> uploadsToDelete) throws IOException {
		dao.delete(uploadsToDelete);
	}
	
	@Override
	public String getFilePath(String id) {
		return dao.getFilePath(id);
	}
}