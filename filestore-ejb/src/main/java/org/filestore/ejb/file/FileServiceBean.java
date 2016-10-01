package org.filestore.ejb.file;

import org.filestore.ejb.file.entity.FileItem;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jerome on 01/10/2016.
 */
@Stateless(name = "fileservice")
@Remote(FileService.class)
public class FileServiceBean implements FileService {

    private static final Logger LOGGER = Logger.getLogger(FileServiceBean.class.getName());

    @PersistenceContext(unitName="filestore-pu")
    public EntityManager em;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String postFile(String owner, List<String> receivers, String message, String name, String stream) throws FileServiceException {
        LOGGER.log(Level.INFO, "Post File called");
        try {
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            FileItem file = new FileItem();
            file.setId(id);
            file.setOwner(owner);
            file.setReceivers(receivers);
            file.setMessage(message);
            file.setName(name);
            file.setStream(stream);
            em.persist(file);
            return id;
        } catch ( Exception e ) {
            LOGGER.log(Level.SEVERE, "An error occured during posting file", e);
            throw new FileServiceException(e);
        }
    }

    @Override
    public FileItem getFile(String id) throws FileServiceException {
        return null;
    }

    @Override
    public void deleteFile(String id) throws FileServiceException {

    }
}
