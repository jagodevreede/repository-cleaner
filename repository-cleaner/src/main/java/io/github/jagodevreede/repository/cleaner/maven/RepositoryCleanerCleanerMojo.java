package io.github.jagodevreede.repository.cleaner.maven;

import io.github.jagodevreede.repository.cleaner.RepositoryWorker;
import io.github.jagodevreede.repository.cleaner.util.SizeUnitSI;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

@Mojo(name = "clean", threadSafe = true)
public class RepositoryCleanerCleanerMojo extends BaseRepositoryCleanerMojo {

    @Parameter(property = "clean.dryRun", defaultValue = "false", readonly = true)
    boolean dryRun;

    @Override
    void executeAction(RepositoryWorker worker, List<RepositoryWorker.FolderAndLastAccessTime> oldFolders) {
        long cleanupSize = worker.getCleanupSize(oldFolders);
        if (dryRun) {
            getLog().info("Not actual deleting, as this is a dry run");
            getLog().info("Would have cleaned " + SizeUnitSI.toHumanReadable(cleanupSize));
        } else {
            long beforeSize = worker.getTotalSize();
            delete(oldFolders);
            worker.init();
            long totalSize = worker.getTotalSize();
            getLog().info("Total size of repository before cleaning " + SizeUnitSI.toHumanReadable(beforeSize));
            getLog().info("Total size of repository after cleaning " + SizeUnitSI.toHumanReadable(totalSize));
            getLog().info("Cleaned " + SizeUnitSI.toHumanReadable(cleanupSize));
        }
    }

    public void delete(List<RepositoryWorker.FolderAndLastAccessTime> oldFolders) {
        for (RepositoryWorker.FolderAndLastAccessTime folder : oldFolders) {
            File folderToDelete = folder.getFolder();
            getLog().info("Deleting " + folderToDelete.getAbsolutePath() + " (" + SizeUnitSI.toHumanReadable(folder.getSizeBytes()) + ")");
            for (File file : folderToDelete.listFiles()) {
                if (!file.delete()) {
                    getLog().warn("Unable to delete " + file.getAbsolutePath());
                }
            }
            if (!folderToDelete.delete()) {
                getLog().warn("Unable to delete " + folderToDelete.getAbsolutePath());
            }
        }
    }

}
