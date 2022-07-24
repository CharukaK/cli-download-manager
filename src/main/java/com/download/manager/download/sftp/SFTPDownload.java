package com.download.manager.download.sftp;

import com.download.manager.download.Download;
import com.download.manager.download.DownloadConfig;
import com.download.manager.download.DownloadInfo;
import com.download.manager.download.DownloadManager;
import com.download.manager.download.DownloadState;
import com.download.manager.exceptions.DownloadException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.RandomAccessContent;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.util.RandomAccessMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static com.download.manager.download.Util.getNewFileName;

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
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        FileObject remoteFileObject = null;
        try {
            FileSystemManager manager = VFS.getManager();
            remoteFileObject = manager.resolveFile(sftpConfig.getSftpUrl());
            RandomAccessContent rac = remoteFileObject.getContent().getRandomAccessContent(RandomAccessMode.READ);
            File outputFile = new File(sftpConfig.getFullOutputFilePath());
            if (outputFile.exists()) {
                if (sftpConfig.getTries() == 0) {
                    // There exist a file name collision
                    outputFile = getNewFileName(outputFile);
                    sftpConfig.setFileName(outputFile.getName());
                    getDownloadInfo().setFilePath(sftpConfig.getFullOutputFilePath());
                    updateState(getId(), getDownloadInfo());
                } else {
                    rac.seek(outputFile.length());
                }
            }

            bufferedInputStream = new BufferedInputStream(rac.getInputStream());
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            byte[] buffer = new byte[8192];
            int byteCount;

            logger.info("Starting download " + sftpConfig.getFileName());
            while ((byteCount = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, byteCount);
            }

            logger.info("Finished download " + sftpConfig.getSftpUrl());
            updateState(sftpConfig.getId(), new DownloadInfo(DownloadState.COMPLETED, getDownloadInfo().getFilePath()));
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.info("Failed download " + sftpConfig.getSftpUrl());
            try {
                Thread.sleep(sftpConfig.getRetryInterval());
            } catch (InterruptedException ex) {
                logger.error(e.getMessage(), e);
            }

            logger.info("Attempting retry for download " + sftpConfig.getSftpUrl());
            try {
                if (sftpConfig.getTries() < sftpConfig.getRetryCount()) {
                    sftpConfig.increaseTries();
                    DownloadManager.getInstance().submitDownload(new SFTPDownload().init(sftpConfig));
                } else {
                    updateState(sftpConfig.getId(), new DownloadInfo(DownloadState.FAILED, getDownloadInfo().getFilePath()));
                }
            } catch (DownloadException ex) {
                logger.error(e.getMessage(), e);
            }
        } finally {
            try {
                Objects.requireNonNull(bufferedOutputStream).flush();
                Objects.requireNonNull(bufferedOutputStream).close();
                Objects.requireNonNull(bufferedInputStream).close();
                Objects.requireNonNull(remoteFileObject).close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

    }
}
