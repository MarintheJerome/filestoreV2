package org.filestore.ejb.file;

import org.filestore.ejb.file.entity.FileItem;
import org.filestore.ejb.file.metrics.FileServiceMetricsBean;
import org.filestore.ejb.store.BinaryStoreService;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jerome on 01/10/2016.
 */
@Stateless(name = "fileservice")
@Interceptors(FileServiceMetricsBean.class)
@Remote(FileService.class)
public class FileServiceBean implements FileService {

    private static final Logger LOGGER = Logger.getLogger(FileServiceBean.class.getName());

    @PersistenceContext(unitName="filestore-pu")
    public EntityManager em;

    @EJB
    protected BinaryStoreService store;

    @Resource
    private SessionContext ctx;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String postFile(String owner, List<String> receivers, String message, String name, InputStream stream) throws FileServiceException {
        LOGGER.log(Level.INFO, "Post File called");
        try {
            String sid = store.put(stream);
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            FileItem file = new FileItem();
            file.setId(id);
            file.setOwner(owner);
            file.setReceivers(receivers);
            file.setMessage(message);
            file.setName(name);
            file.setStream(sid);
            em.persist(file);
            return id;
        } catch ( Exception e ) {
            LOGGER.log(Level.SEVERE, "An error occured during posting file", e);
            ctx.setRollbackOnly();
            throw new FileServiceException(e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public FileItem getFile(String id) throws FileServiceException {
        LOGGER.log(Level.INFO, "Get File called");
        try {
            FileItem item = em.find(FileItem.class, id);
            if (item == null) {
                throw new FileServiceException("Unable to get file with id '" + id + "' : file does not exists");
            }
            return item;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occured during getting file", e);
            throw new FileServiceException(e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public InputStream getFileContent(String id) throws FileServiceException {
        LOGGER.log(Level.INFO, "Get File Content called");
        try{
            FileItem item = em.find(FileItem.class, id);
            if (item == null) {
                throw new FileServiceException("Unable to get file with id '" + id + "' : file does not exists");
            }
            InputStream is = store.get(item.getStream());
            return is;
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "An error occured during getting file", e);
            throw new FileServiceException(e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteFile(String id) throws FileServiceException {
        LOGGER.log(Level.INFO, "Delete File called");
        try {
            FileItem item = em.find(FileItem.class, id);
            if (item == null) {
                throw new FileServiceException("Unable to delete file with id '" + id + "' : file does not exists");
            } else {
                em.remove(item);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occured during deleting file", e);
            throw new FileServiceException(e);
        }
    }
}
