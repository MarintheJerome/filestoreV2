package org.filestore.ejb.store;

import java.io.InputStream;

public interface BinaryStoreService {
	
	boolean exists(String key) throws BinaryStoreServiceException;
	
	String put(InputStream is) throws BinaryStoreServiceException;
	
	InputStream get(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException;

	void delete(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException;
}
