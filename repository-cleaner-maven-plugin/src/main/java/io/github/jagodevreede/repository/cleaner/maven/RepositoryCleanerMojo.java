package io.github.jagodevreede.repository.cleaner.maven;

import io.github.jagodevreede.repository.cleaner.RepositoryWorker;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;

import javax.inject.Inject;
import java.util.List;

@Mojo(name = "clean", defaultPhase = LifecyclePhase.VALIDATE, threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE)
public class RepositoryCleanerMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    MavenProject project;

    @Parameter(property = "daysToKeep", defaultValue = "365", readonly = true)
    int daysToKeep;

    @Parameter(property = "clean.dryRun", defaultValue = "false", readonly = true)
    boolean dryRun;

    @Parameter(defaultValue = "${localRepository}", readonly = true)
    ArtifactRepository localRepository;

    public void execute() throws MojoExecutionException {
        if (project != null && project.getParent() != null) {
            getLog().info("Skipping repository clean check, as this is a sub-project");
            return;
        }
        getLog().info("Analyzing with " + daysToKeep + " days to keep... " + localRepository.getBasedir());
        RepositoryWorker worker = new RepositoryWorker(localRepository.getBasedir(), getLog());
        List<RepositoryWorker.FolderAndLastAccessTime> oldFolders = worker.findOldFolders(daysToKeep);
        worker.getCleanupSize(oldFolders);
        if (dryRun) {
            getLog().info("Not actual deleting, as this is a dry run");
        } else {
            worker.delete(oldFolders);
        }
    }

}
