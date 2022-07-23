package com.download.manager.download.sftp;

import com.download.manager.download.Download;
import com.download.manager.download.DownloadConfig;
import com.download.manager.download.DownloadInfo;
import com.download.manager.download.DownloadState;
import com.download.manager.download.ftp.FTPDownloadConfig;
import com.download.manager.exceptions.DownloadException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class SFTPDownload extends Download {
    private final Logger logger = LoggerFactory.getLogger(SFTPDownload.class);
    private SFTPConfig sftpConfig;

    @Override
    public Download init(DownloadConfig config) throws DownloadException {
        sftpConfig = (SFTPConfig) config;
        setId(sftpConfig.getId());
        updateState(getId(), new DownloadInfo(DownloadState.INITIALIZED, ""));
        logger.info("Initialized download " + sftpConfig.getSftpUrl());
        return this;
    }

    @Override
    public void run() {
        try {
            FileSystemManager manager = VFS.getManager();
            FileObject local = manager.resolveFile(new File(sftpConfig.getFullOutputFilePath()).getAbsolutePath());
            FileObject remote = manager.resolveFile(sftpConfig.getSftpUrl());

            logger.info("Started download " + sftpConfig.getSftpUrl());
            remote.copyFrom(local, Selectors.SELECT_SELF);

            local.close();
            remote.close();
            logger.info("Finished download " + sftpConfig.getSftpUrl());
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }

    }
}
