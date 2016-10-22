package org.filestore.ejb.file;

import org.filestore.ejb.file.entity.FileItem;

import java.util.List;

/**
 * Created by guill on 21/10/2016.
 */
public interface FileServiceAdmin {

    public List<FileItem> listAllFiles() throws FileServiceException;

    public FileItem getNextStaleFile() throws FileServiceException;

    public void deleteFile(String id) throws FileServiceException;
}