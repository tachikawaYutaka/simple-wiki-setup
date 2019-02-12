package com.wakabatimes.simplewikisetup.app.domain.service.git_data;

import com.wakabatimes.simplewikisetup.SimpleWikiSetupApplication;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitBranchName;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitData;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitUrl;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitUrlComponent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SimpleWikiSetupApplication.class)
public class GitDataServiceTest {
    @Autowired
    private GitDataService gitDataService;

    @Autowired
    private GitUrlComponent gitUrlComponent;

    @Test
    @Ignore
    public void get(){
        String url = gitUrlComponent.getGitUrl();
        String branch = gitUrlComponent.getBranch();
        GitUrl gitUrl = new GitUrl(url);
        GitBranchName gitBranchName = new GitBranchName(branch);
        GitData gitData = new GitData(gitUrl,gitBranchName);
        GitData gitData1 = gitDataService.clone(gitData);
        Assert.assertTrue(gitData1.getGitRepositoryPath().getValue() != null);
    }
}
