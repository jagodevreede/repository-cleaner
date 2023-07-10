package io.github.jagodevreede.repository.cleaner.maven;

import io.github.jagodevreede.repository.cleaner.RepositoryWorker;
import io.github.jagodevreede.repository.cleaner.util.SizeUnitSI;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;

@Mojo(name = "analyze", threadSafe = true)
public class RepositoryAnalyzerCleanerMojo extends BaseRepositoryCleanerMojo {
    private final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    void executeAction(RepositoryWorker worker, List<RepositoryWorker.FolderAndLastAccessTime> oldFolders) {
        long totalSize = worker.getTotalSize();
        getLog().info("Found the following to clean:");
        for (RepositoryWorker.FolderAndLastAccessTime oldFolder : oldFolders) {
            File folderToDelete = oldFolder.getFolder();
            LocalDateTime lastAccessTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(oldFolder.getLastAccessTime()), TimeZone.getDefault().toZoneId());
            getLog().info(folderToDelete.getAbsolutePath() + " (" + SizeUnitSI.toHumanReadable(oldFolder.getSizeBytes()) + ") last accessed " + lastAccessTime.format(dateFormat));
        }
        long cleanUpSize = worker.getCleanupSize(oldFolders);
        getLog().info("Current size of repository is " + SizeUnitSI.toHumanReadable(totalSize));
        getLog().info("Size of repository after cleaning would be " + SizeUnitSI.toHumanReadable(totalSize - cleanUpSize));
        getLog().info("Cleanup size is " + SizeUnitSI.toHumanReadable(cleanUpSize));
    }
}
