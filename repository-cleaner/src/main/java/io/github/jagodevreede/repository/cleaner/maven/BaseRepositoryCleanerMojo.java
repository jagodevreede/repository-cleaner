package io.github.jagodevreede.repository.cleaner.maven;

import io.github.jagodevreede.repository.cleaner.RepositoryWorker;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

abstract class BaseRepositoryCleanerMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    MavenProject project;

    @Parameter(property = "daysToKeep", defaultValue = "365", readonly = true)
    int daysToKeep;

    @Parameter(property = "clean.snapshotOnly", defaultValue = "false", readonly = true)
    boolean snapshotOnly;

    @Parameter(defaultValue = "${localRepository}", readonly = true)
    ArtifactRepository localRepository;

    public void execute() throws MojoExecutionException {
        if (project != null && project.getParent() != null) {
            getLog().info("Skipping repository clean check, as this is a sub-project");
            return;
        }
        getLog().info("Analyzing with " + daysToKeep + " days to keep... " + localRepository.getBasedir());
        RepositoryWorker worker = new RepositoryWorker(localRepository.getBasedir(), getLog());
        List<RepositoryWorker.FolderAndLastAccessTime> oldFolders = worker.findOldFolders(daysToKeep, snapshotOnly);
        if (oldFolders.isEmpty()) {
            getLog().info("Found nothing to clean");
            return;
        }
        executeAction(worker, oldFolders);
    }

    abstract void executeAction(RepositoryWorker worker, List<RepositoryWorker.FolderAndLastAccessTime> oldFolders);
}
