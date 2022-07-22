package com.download.manager.download;

import com.download.manager.exceptions.DownloadException;
import com.download.manager.util.DownloadState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class Download implements Runnable {
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private DownloadState downloadState;
    private String id;

    public abstract Download init(DownloadConfig config) throws DownloadException;

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void updateState(String id, DownloadState downloadState) {
        support.firePropertyChange(id, this.downloadState, downloadState);
        this.downloadState = downloadState;
    }

    public DownloadState getDownloadState() {
        return downloadState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
