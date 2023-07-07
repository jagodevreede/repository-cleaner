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
        long size = worker.totalSize();

        assertThat(size).isGreaterThan(0);
        verify(log).info(startsWith("Total size of repository "));
    }

    @Test
    void findOldFolders_and_size() throws Exception {
        RepositoryWorker worker = new RepositoryWorker(userM2Repo, log);
        List<RepositoryWorker.FolderAndLastAccessTime> oldFolders = worker.findOldFolders(1);
        long cleanupSize = worker.getCleanupSize(oldFolders);

        assertThat(oldFolders).hasSizeGreaterThan(0);
        assertThat(cleanupSize).isGreaterThan(0);
    }


}