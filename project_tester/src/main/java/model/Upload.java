package model;

import java.util.HashMap;
import java.util.Map;

public class Upload implements UploadIF {

	private long accountId;
	private Map<String, byte[]> fileData = new HashMap<String, byte[]>();

	public Upload(long userId) {
		this.accountId = userId;
	}
	
	@Override
	public long getAccountId() {
		return accountId; 
	}
	
	@Override
	public void addFileData(String originalFilename, byte[] bytes) {
		fileData.put(originalFilename, bytes);
	}

	@Override
	public Map<String, byte[]> getData() {
		return fileData;
	}	
}