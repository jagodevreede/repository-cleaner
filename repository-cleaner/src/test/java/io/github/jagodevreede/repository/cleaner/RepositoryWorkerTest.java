package io.github.jagodevreede.repository.cleaner;

import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class RepositoryWorkerTest {
    private static final String userM2Repo = new File(System.getProperty("user.home"), ".m2/repository").getAbsolutePath();

    @Mock
    Log log;

    @Test
    void totalSize() throws Exception {
        RepositoryWorker worker = new RepositoryWorker(userM2Repo, log);
        long size = worker.getTotalSize();

        assertThat(size).isGreaterThan(0);
        verify(log).info(startsWith("Total size of repository "));
    }

    @Test
    void findOldFolders_and_size() throws Exception {
        RepositoryWorker worker = new RepositoryWorker(userM2Repo, log);
        List<RepositoryWorker.FolderAndLastAccessTime> oldFolders = worker.findOldFolders(0, false);
        long cleanupSize = worker.getCleanupSize(oldFolders);

        assertThat(oldFolders).hasSizeGreaterThan(0);
        assertThat(cleanupSize).isGreaterThan(0);
    }

    @Test
    void findOldFolders_and_size_withSnapshot() throws Exception {
        RepositoryWorker worker = new RepositoryWorker(userM2Repo, log);
        List<RepositoryWorker.FolderAndLastAccessTime> oldFolders = worker.findOldFolders(0, true);
        long cleanupSize = worker.getCleanupSize(oldFolders);

        assertThat(oldFolders).hasSizeGreaterThan(0)
                        .allMatch(f -> f.getFolder().getAbsolutePath().contains("-SNAPSHOT"));

        assertThat(cleanupSize).isGreaterThan(0);
    }
}