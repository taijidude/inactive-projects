package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import config.DaoConfig;
import model.Upload;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DaoConfig.class)
public class UploadDaoTest {

	@Autowired
	private UploadDaoIF dao;
	
	@Autowired
	private JdbcTemplate template;
	
	private File fileToSave;
	
	@Before
	public void setup() {
		fileToSave = new File("src/main/resources/cyberpunk-main.jpg");
	}
	
	@Test
	public void testGetFileExtension() {
		String fileExtension = dao.getFileExtension("test.jpg");
		assertEquals("Es wird .jpg erwartet!", ".jpg", fileExtension);
		fileExtension = dao.getFileExtension("src/main/resources/cyberpunk-main.jpg");
		assertEquals("Es wird .jpg erwartet!", ".jpg", fileExtension);
	}
	
	@Test
	@Transactional
	public void testSaveSingleFile() throws IOException {
		Upload imagesUpload = new Upload(1);
		byte[] readAllBytes = Files.readAllBytes(fileToSave.toPath());
		assertNotNull("Es müssen Bytes eingelesen werden!", readAllBytes);
		imagesUpload.addFileData(fileToSave.getName(), readAllBytes);
		Map<Long, String> save = dao.persist(imagesUpload);
		StringBuilder pathBuilder = new StringBuilder("uploads/").append(save.keySet().iterator().next()).append(fileToSave.getName().substring(fileToSave.getName().lastIndexOf(".")));
		Path path = Paths.get(pathBuilder.toString());
		File savedFile = path.toFile();
		assertTrue("Die Datei existiert nicht!", savedFile.exists());
		deleteAssert(path);
	}
	
	@Test
	@Transactional
	public void testSaveMultipleFiles() throws IOException {
		Upload imagesUpload = new Upload(1);
		File secondFileToSave = new File("src/main/resources/china-information.jpg");
		imagesUpload.addFileData(fileToSave.getName(), Files.readAllBytes(fileToSave.toPath()));
		imagesUpload.addFileData(secondFileToSave.getName(), Files.readAllBytes(secondFileToSave.toPath()));
		Map<Long, String> saveResult = dao.persist(imagesUpload);
		Iterator<Long> iterator = saveResult.keySet().iterator();
		while(iterator.hasNext()) {
			Long id = iterator.next();
			String filename = saveResult.get(id);
			assertTrue("Die Datei existiert nicht", checkIfFileExists(filename, id));
			deleteAssert(getPath(filename, id));
		}
	}
	
	@Test
	@Transactional
	public void testDeleteSingleFile() throws IOException {
		Upload imagesUpload = new Upload(1);
		byte[] readAllBytes = Files.readAllBytes(fileToSave.toPath());
		assertNotNull("Es müssen Bytes eingelesen werden!", readAllBytes);
		imagesUpload.addFileData(fileToSave.getName(), readAllBytes);
		Map<Long, String> saveResult = dao.persist(imagesUpload);
		StringBuilder pathBuilder = new StringBuilder("uploads/").append(saveResult.keySet().iterator().next()).append(fileToSave.getName().substring(fileToSave.getName().lastIndexOf(".")));
		Path path = Paths.get(pathBuilder.toString());
		File savedFile = path.toFile();
		assertTrue("Die Datei existiert nicht!", savedFile.exists());
		
		Long id = saveResult.keySet().iterator().next();
		List<String> uploadsToDelete = new ArrayList<String>();
		uploadsToDelete.add(""+id);
		dao.delete(uploadsToDelete);
		
		boolean fileExists = checkIfFileExists(saveResult.get(id), id);
		assertFalse("Die Datei wurde nicht gelöscht!", fileExists);
		ResultSetExtractor<Integer> resultSetExtractor = new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.first();
				return Integer.valueOf(rs.getInt(1));
			}
		};
		String sql = "select count(*) from UPLOAD where id = "+id+";";
		Integer anzahlDatensätze = template.query(sql, resultSetExtractor);
		assertTrue("Nach dem Löschen eines Uploads sollte es keine Datensätze mehr mit der Id in der Datenbank geben!", anzahlDatensätze == 0);
	}
	
	@Test
	@Transactional 
	public void testGetFilePath() throws IOException {
		Upload imagesUpload = new Upload(1);
		byte[] readAllBytes = Files.readAllBytes(fileToSave.toPath());
		assertNotNull("Es müssen Bytes eingelesen werden!", readAllBytes);
		imagesUpload.addFileData(fileToSave.getName(), readAllBytes);
		Map<Long, String> saveResult = dao.persist(imagesUpload);
		Long next = saveResult.keySet().iterator().next();
		String id = ""+next;
		String filePath = dao.getFilePath(id);
		StringBuilder pathBuilder = new StringBuilder("uploads\\").append(next).append(".");
		assertTrue("Es wurde nicht die erwartete Pfadangabe gefunden!", filePath.startsWith(pathBuilder.toString()));
	}
	
	private boolean checkIfFileExists(String filename, Long id) {
		Path path = getPath(filename, id);
		File savedFile = path.toFile();
		return savedFile.exists();
	}
	
	private Path getPath(String filename, Long id) {
		String fileExtension = dao.getFileExtension(filename);
		return Paths.get("uploads/"+id+fileExtension);
	}
	
	private void deleteAssert(Path path) throws IOException {
		Files.delete(path);
		assertTrue("Wurde nicht gelöscht!", !path.toFile().exists());
	}
}