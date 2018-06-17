package model;

import java.util.Map;

public interface UploadIF {

	void addFileData(String originalFilename, byte[] bytes);

	Map<String, byte[]> getData();

	long getAccountId();
}