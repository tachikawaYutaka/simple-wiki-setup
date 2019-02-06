package com.wakabatimes.simplewikisetup.app.application.git_data;

import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitBranchName;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitData;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitRepositoryPath;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitUrl;
import com.wakabatimes.simplewikisetup.app.domain.service.git_data.GitDataService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
@Slf4j
@Service
public class GitDataServiceImpl implements GitDataService{
    @Override
    public GitData clone(GitData gitData) {
        try {
            Path tempDir = Files.createTempDirectory("tempDir_");
            Repository localRepo = new FileRepository( tempDir.toString() );
            Git git = new Git( localRepo );

            if( git != null ){
                //. git clone
                try {
                    log.info(tempDir.toString());
                    git.cloneRepository().setURI( gitData.getGitUrl().getValue() ).setBranch(gitData.getGitBranchName().getValue()).setDirectory( new File( tempDir.toString()) ).call();

                    GitUrl gitUrl = new GitUrl(gitData.getGitUrl().getValue());
                    GitBranchName gitBranchName = new GitBranchName(gitData.getGitBranchName().getValue());
                    GitRepositoryPath gitRepositoryPath = new GitRepositoryPath(tempDir.toString());
                    GitData gitData1 = new GitData(gitUrl,gitBranchName,gitRepositoryPath);
                    return gitData1;
                } catch (GitAPIException e) {
                    log.error("Error: ", e);
                    throw new RuntimeException(e.getMessage());
                }
            }else {
                throw new RuntimeException("リポジトリ―を作成できません。");
            }
        } catch (IOException e) {
            log.error("Error: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
