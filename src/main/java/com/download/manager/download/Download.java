package com.download.manager.download;

import com.download.manager.exceptions.DownloadException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class Download implements Runnable {
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String id;
    private DownloadInfo downloadInfo;

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
