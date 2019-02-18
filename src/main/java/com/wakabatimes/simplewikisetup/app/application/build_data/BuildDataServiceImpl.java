package com.wakabatimes.simplewikisetup.app.application.build_data;

import com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data.BuildData;
import com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data.BuildTargetPath;
import com.wakabatimes.simplewikisetup.app.domain.service.build_data.BuildDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class BuildDataServiceImpl implements BuildDataService{

    @Override
    public BuildData build(BuildData buildData) {
        String buildCommand = buildData.getBuildCommand().getValue();
        String repositoryPath = buildData.getGitData().getGitRepositoryPath().getValue();

        File repository = new File(repositoryPath);
        if(!repository.exists()) throw new RuntimeException("指定されたリポジトリ―が見つかりません。");

        String mysqlHost = buildData.getMysqlData().getMysqlHost().getValue();
        String mysqlPort = buildData.getMysqlData().getMysqlPort().getValue();
        String mysqlUserName = buildData.getMysqlData().getMysqlUserName().getValue();
        String mysqlPassword = buildData.getMysqlData().getMysqlPassword().getValue();
        String mysqlTable = buildData.getMysqlData().getMysqlTable().getValue();


        //ymlファイルの置換
        String ymlPath = repositoryPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "config" + File.separator + "application.yml";
        try{
            // FileWriterクラスのオブジェクトを生成する
            FileWriter file = new FileWriter(ymlPath);
            // PrintWriterクラスのオブジェクトを生成する
            PrintWriter pw = new PrintWriter(new BufferedWriter(file));

            //ファイルに書き込み
            pw.println("server:");
            pw.println("  port: 8080");
            pw.println("spring:");
            pw.println("  datasource:");
            pw.println("    url: jdbc:mysql://" + mysqlHost + "/" + mysqlTable + "?serverTimezone=JST");
            pw.println("    username: " + mysqlUserName);
            pw.println("    password: " + mysqlPassword);
            pw.println("    driverClassName: com.mysql.cj.jdbc.Driver");
            pw.println("    thymeleaf:");
            pw.println("      cache: false");
            pw.println("      mode: HTML");
            pw.println("mybatis.configuration.mapUnderscoreToCamelCase: true");

            //ファイルを閉じる
            pw.close();
        } catch(RuntimeException | IOException e){
            log.error("Error",e);
            throw new RuntimeException(e.getMessage());
        }
        String chmodCommand[] = {"chmod","775",buildCommand};
        String buildCommandPath = "." + File.separator + buildCommand;
        String flywayCommand[] = {buildCommandPath,"flyway:migrate","-Dflyway.user=" + mysqlUserName ,"-Dflyway.password=" + mysqlPassword,"-Dflyway.url=jdbc:mysql://" + mysqlHost + ":"  + mysqlPort +"/" + mysqlTable + "?serverTimezone=JST", "-Dflyway.driver=com.mysql.cj.jdbc.Driver","-Dflyway.placeholderReplacement=false"};
        String packageCommand[]= {buildCommandPath,"package", "-DskipTests=true"};
        Process pre;
        Process p;
        Process p2;
        Runtime runtime = Runtime.getRuntime();
        if(buildCommand.equals("mvnw")){
            try {
                pre = runtime.exec(chmodCommand, null, repository);
            } catch (IOException e) {
                log.error("Error",e);
                throw new RuntimeException(e.getMessage());
            }

            try {
                pre.waitFor();
            } catch (InterruptedException e) {
                log.error("Error",e);
                throw new RuntimeException(e.getMessage());
            }

            InputStream isPre = pre.getInputStream();
            BufferedReader brPre = new BufferedReader(new InputStreamReader(isPre)); // テキスト読み込みを行えるようにする

            while (true) {
                String line;
                try {
                    line = brPre.readLine();
                } catch (IOException e) {
                    log.error("Error",e);
                    throw new RuntimeException(e.getMessage());
                }
                if (line == null) {
                    break;
                } else {
                    log.info("chmod:" + line);
                }
            }
        }

        try {
            p = runtime.exec(flywayCommand, null, repository);
        } catch (IOException e) {
            log.error("Error",e);
            throw new RuntimeException(e.getMessage());
        }

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            log.error("Error",e);
            throw new RuntimeException(e.getMessage());
        }

        InputStream is = p.getInputStream();
        BufferedReader br2 = new BufferedReader(new InputStreamReader(is)); // テキスト読み込みを行えるようにする

        while (true) {
            String line;
            try {
                line = br2.readLine();
            } catch (IOException e) {
                log.error("Error",e);
                throw new RuntimeException(e.getMessage());
            }
            if (line == null) {
                break;
            } else {
                log.info("flywayCommand:" + line);
            }
        }

        try {
            p2 = runtime.exec(packageCommand, null, repository);
        } catch (IOException e) {
            log.error("Error",e);
            throw new RuntimeException(e.getMessage());
        }

        try {
            p2.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        InputStream is2 = p2.getInputStream();
        BufferedReader br3 = new BufferedReader(new InputStreamReader(is2));

        while (true) {
            String line;
            try {
                line = br3.readLine();
            } catch (IOException e) {
                log.error("Error",e);
                throw new RuntimeException(e.getMessage());
            }
            if (line == null) {
                break;
            } else {
                log.info("packageCommand:" + line);
            }
        }

        String warPath = repositoryPath + File.separator + "target" + File.separator;
        BuildTargetPath buildTargetPath = new BuildTargetPath(warPath);
        return new BuildData(buildData.getGitData(),buildData.getMysqlData(),buildData.getBuildCommand(),buildData.getBuildWarFile(),buildTargetPath);
    }
}
