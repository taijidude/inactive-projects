package dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import exception.DataNotFoundException;
import model.UploadIF;
import model.entity.UploadEntity;

/**
 * @author John
 *
 */
public class UploadDao implements UploadDaoIF {

	@PersistenceContext
	private EntityManager em;

	private File uploadDir;

	public File getUploadDir() {
		return uploadDir;
	}

	public UploadDao(File uploadDir) {
		if(!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		this.uploadDir = uploadDir;
	}

	@Override
	@Transactional
	public UploadEntity getEntityById(long id) throws DataNotFoundException {
		Query query = em.createQuery("from UploadEntity ue where ue.id = :id");
		query.setParameter("id", id);
		try {
			return (UploadEntity) query.getSingleResult();
		} catch (NoResultException nre) {
			throw new DataNotFoundException();
		}
	}
	
	@Override
	@Transactional
	public Map<Long,String> persist(UploadIF upload) throws IOException {
		HashMap<Long, String> result = new HashMap<Long, String>();
		Map<String, byte[]> data = upload.getData();
		long userId = upload.getAccountId();
		for (String filename : data.keySet()) {
			String extension = getFileExtension(filename);
			long id = writeDataToDatabase(filename, userId);
			writeDataToFileSystem(id, data.get(filename), extension);
			result.put(Long.valueOf(id), filename);
		}
		return result;
	}	

	@Override
	public String getFileExtension(String filename) {
		Pattern pattern = Pattern.compile("\\.[^.\\/:*?\"<>|\r\n]+$");
		Matcher matcher = pattern.matcher(filename);
		if(matcher.find()) {
			return matcher.group();
		} else {
			throw new IllegalStateException("Es konnte kein Datei Endung ermittelt werden!");
		}
	}

	@Transactional
	@Override
	public long writeDataToDatabase(String filename, long accountId) {
		UploadEntity entity = new UploadEntity(accountId, filename);
		em.persist(entity);
		return entity.getId();
	}

	protected void writeDataToFileSystem(long id, byte[] data, String extension) throws IOException {
		File target = null;
		String imageUploadPath = new StringBuilder(uploadDir.getPath()).toString();
		target = new File(imageUploadPath);
		StringBuilder targetFilePathBuilder = new StringBuilder(target.getPath()).append("\\").append(id).append(extension);
		String targetFilePath = targetFilePathBuilder.toString();
		Path path = Paths.get(targetFilePath);
		Files.write(path, data);

	}
	@Override
	public File getUploadsDir() {
		return uploadDir;
	}

	@Override
	@Transactional
	public void delete(List<String> uploadsToDelete) throws IOException {
		Query deleteQuery = em.createQuery("delete from UploadEntity ue where ue.id = :id");
		File[] listFiles = uploadDir.listFiles();
		for (File file : listFiles) {
			String baseName = FilenameUtils.getBaseName(file.getName());
			if(uploadsToDelete.contains(baseName)) {
				Files.delete(file.toPath()); 
				deleteQuery.setParameter("id", Long.parseLong(baseName));
				deleteQuery.executeUpdate();
			}
		}
	}

	@Override
	public String getFilePath(String id) {
		File[] listFiles = uploadDir.listFiles();
		for (File file : listFiles) {
			if(id.equals(FilenameUtils.getBaseName(file.getName()))) {
				return file.toPath().toString();
			}
		}
		return null;
	}
}