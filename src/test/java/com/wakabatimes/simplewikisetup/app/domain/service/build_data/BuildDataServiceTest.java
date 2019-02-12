package com.wakabatimes.simplewikisetup.app.domain.service.build_data;

import com.wakabatimes.simplewikisetup.SimpleWikiSetupApplication;
import com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data.BuildCommand;
import com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data.BuildCommandComponent;
import com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data.BuildData;
import com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data.BuildWarFile;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitBranchName;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitData;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitUrl;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitUrlComponent;
import com.wakabatimes.simplewikisetup.app.domain.model.mysql_data.*;
import com.wakabatimes.simplewikisetup.app.domain.service.git_data.GitDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SimpleWikiSetupApplication.class)
public class BuildDataServiceTest {
    @Autowired
    private BuildDataService buildDataService;
    @Autowired
    private GitDataService gitDataService;
    @Autowired
    private BuildCommandComponent buildCommandComponent;
    @Autowired
    private GitUrlComponent gitUrlComponent;

    @Test
    @Ignore
    public void build(){
        String url = gitUrlComponent.getGitUrl();
        String branch = gitUrlComponent.getBranch();
        GitUrl gitUrl = new GitUrl(url);
        GitBranchName gitBranchName = new GitBranchName(branch);
        GitData gitData = new GitData(gitUrl,gitBranchName);
        GitData gitData1 = gitDataService.clone(gitData);

        MysqlPort mysqlPort = new MysqlPort("3306");
        MysqlUserName mysqlUserName = new MysqlUserName("wiki");
        MysqlHost mysqlHost = new MysqlHost("localhost");
        MysqlPassword mysqlPassword = new MysqlPassword("hoge00");
        MysqlTable mysqlTable = new MysqlTable("hogehogehoge");
        MysqlData mysqlData = new MysqlData(mysqlUserName,mysqlPassword,mysqlTable,mysqlHost,mysqlPort);

        BuildCommand buildCommand = new BuildCommand(buildCommandComponent.getBuildCommand());

        String warName = buildCommandComponent.getBuildWarName();
        String versionName = buildCommandComponent.getBuildWarVersion();
        BuildWarFile buildWarFile = new BuildWarFile(warName + "-" + versionName + ".war");
        BuildData buildData = new BuildData(gitData1,mysqlData,buildCommand,buildWarFile);
        BuildData result = buildDataService.build(buildData);
        log.info(result.getBuildTargetPath().getValue() + result.getBuildWarFile().getValue());
    }
}
