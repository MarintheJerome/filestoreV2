package org.filestore.ejb.file.metrics;

import org.filestore.ejb.file.FileServiceException;

/**
 * Created by jerome on 08/10/2016.
 */
public interface FileServiceMetrics {
    int getTotalUploads() throws FileServiceException;

    int getTotalDownloads() throws FileServiceException;

    int getUptime() throws FileServiceException;
}