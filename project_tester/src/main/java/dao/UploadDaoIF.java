package dao;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import exception.DataNotFoundException;
import model.UploadIF;
import model.entity.UploadEntity;

public interface UploadDaoIF {

	Map<Long, String> persist(UploadIF upload) throws IOException;

	File getUploadsDir();

	void delete(List<String> uploadsToDelete) throws IOException;

	String getFilePath(String id);

	String getFileExtension(String filename);

	UploadEntity getEntityById(long id) throws DataNotFoundException;

	long writeDataToDatabase(String filename, long accountId);
}
