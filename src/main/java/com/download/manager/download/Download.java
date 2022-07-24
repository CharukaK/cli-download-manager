package com.download.manager.download;

import com.download.manager.download.models.DownloadConfig;
import com.download.manager.download.models.DownloadInfo;
import com.download.manager.download.models.DownloadState;
import com.download.manager.exceptions.DownloadException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for Clients of different protocols.
 */
public abstract class Download implements Runnable {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String id;
    private DownloadInfo downloadInfo = new DownloadInfo(DownloadState.INITIALIZED, "");

    /**
     * Abstract method to be implemented by the extended clients to initialize the clients.
     * @param config Client configuration
     * @return Client for the respective protocol
     * @throws DownloadException
     */
    public abstract Download init(DownloadConfig config) throws DownloadException;

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void updateState(String id, DownloadInfo downloadInfo) {
        support.firePropertyChange(id, this.downloadInfo, downloadInfo);
        this.downloadInfo = downloadInfo;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DownloadInfo getDownloadInfo() {
        return downloadInfo;
    }
}
