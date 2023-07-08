package io.github.jagodevreede.repository.cleaner;

import io.github.jagodevreede.repository.cleaner.util.SizeUnitSI;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class RepositoryWorker {
    private final Log log;

    private final File repositoryLocation;
    private final List<FolderAndLastAccessTime> deepestFolders = new ArrayList<>();

    public RepositoryWorker(String repositoryLocation, Log log) throws HaltException {
        this.repositoryLocation = new File(repositoryLocation);
        this.log = log;
        if (!this.repositoryLocation.exists()) {
            throw new HaltException("Repository location " + repositoryLocation + " does not exist");
        }
        log.info("Analyzing repository " + repositoryLocation);
        init();
    }

    public void init() {
        deepestFolders.clear();
        determineDeepestFolder(repositoryLocation);
    }

    public long getTotalSize() {
        long totalSize = 0;
        for (FolderAndLastAccessTime folderAndLastAccessTime : deepestFolders) {
            totalSize += folderAndLastAccessTime.sizeBytes;
        }
        return totalSize;
    }

    public List<FolderAndLastAccessTime> findOldFolders(long days, boolean snapshotOnly) {
        List<FolderAndLastAccessTime> oldFolders = new ArrayList<>();
        for (FolderAndLastAccessTime folderAndLastAccessTime : deepestFolders) {
            if (folderAndLastAccessTime.lastAccessTime < System.currentTimeMillis() - days * 24 * 60 * 60 * 1000) {
                if (!snapshotOnly || folderAndLastAccessTime.folder.getName().contains("-SNAPSHOT")) {
                    oldFolders.add(folderAndLastAccessTime);
                }
            }
        }
        return oldFolders;
    }

    public long getCleanupSize(List<FolderAndLastAccessTime> oldFolders) {
        long cleanupSize = 0;
        for (FolderAndLastAccessTime folderAndLastAccessTime : oldFolders) {
            cleanupSize += folderAndLastAccessTime.sizeBytes;
        }
        log.info("Total cleanup size " + SizeUnitSI.toHumanReadable(cleanupSize));
        return cleanupSize;
    }

    private void determineDeepestFolder(File startPoint) {
        if (startPoint == null) {
            return;
        }
        // Find deepest folder
        boolean hasSubFolders = false;
        long lastAccessTime = System.currentTimeMillis();
        long folderSizeBytes = 0;

        for (File file : startPoint.listFiles()) {
            if (file.isDirectory()) {
                hasSubFolders = true;
                determineDeepestFolder(file);
            } else {
                try {
                    BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                    long fileLastAccessTime = attr.lastAccessTime().toMillis();
                    if (fileLastAccessTime < lastAccessTime) {
                        lastAccessTime = fileLastAccessTime;
                    }
                    folderSizeBytes += attr.size();
                } catch (IOException e) {
                    log.info("Unable to get file attributes for " + file.getAbsolutePath() + " skipping");
                }
            }
        }
        if (!hasSubFolders) {
            deepestFolders.add(new FolderAndLastAccessTime(startPoint, lastAccessTime, folderSizeBytes));
        }
    }

    public static class FolderAndLastAccessTime {
        private final File folder;
        private final long lastAccessTime;
        private final long sizeBytes;

        public FolderAndLastAccessTime(File folder, long lastAccessTime, long sizeBytes) {
            this.folder = folder;
            this.lastAccessTime = lastAccessTime;
            this.sizeBytes = sizeBytes;
        }

        public File getFolder() {
            return folder;
        }

        public long getLastAccessTime() {
            return lastAccessTime;
        }

        public long getSizeBytes() {
            return sizeBytes;
        }
    }
}
