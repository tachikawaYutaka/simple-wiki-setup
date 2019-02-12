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
        BufferedReader br = null;
        BufferedWriter bw = null;
        String ymlPath = repositoryPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "config" + File.separator + "application.yml";
        try{
            br = new BufferedReader(new InputStreamReader(new FileInputStream(ymlPath), "UTF8"));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ymlPath), "UTF8"));

            String line;
            while((line = br.readLine()) != null){
                // 置換処理
                Pattern pattern = Pattern.compile("url:.(.+)");
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    line = line.replace(matcher.group(0), "jdbc:mysql://" + mysqlHost+ ":"  + mysqlPort + "/" + mysqlTable + "?serverTimezone=JST");
                }

                Pattern pattern2 = Pattern.compile("username:.(.+)");
                Matcher matcher2 = pattern2.matcher(line);
                while (matcher2.find()) {
                    line = line.replace(matcher2.group(0), mysqlUserName);
                }

                Pattern pattern3 = Pattern.compile("password:.(.+)");
                Matcher matcher3 = pattern3.matcher(line);
                while (matcher3.find()) {
                    line = line.replace(matcher3.group(0), mysqlPassword);
                }

                // ファイルへ書き込み
                bw.write(line);
                bw.newLine();
            }
        } catch(RuntimeException e){
            log.error("Error",e);
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            log.error("Error",e);
            throw new RuntimeException(e.getMessage());
        }finally{
            if(br != null){
                try{
                    br.close();
                }catch(IOException e){
                    log.error("Error",e);
                    throw new RuntimeException(e.getMessage());
                }
            }
            if(bw != null){
                try{
                    bw.close();
                }catch(IOException e){
                    log.error("Error",e);
                    throw new RuntimeException(e.getMessage());
                }
            }
        }

        String flywayCommand[] = {buildCommand,"flyway:migrate","-Dflyway.user=" + mysqlUserName ,"-Dflyway.password=" + mysqlPassword,"-Dflyway.url=jdbc:mysql://" + mysqlHost + ":"  + mysqlPort +"/" + mysqlTable + "?serverTimezone=JST", "-Dflyway.driver=com.mysql.cj.jdbc.Driver","-Dflyway.placeholderReplacement=false"};
        String packageCommand[]= {buildCommand,"clean", "package", "-DskipTests=true"};
        Process p = null;
        Process p2 = null;
        Runtime runtime = Runtime.getRuntime();

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
            String line = null;
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
            String line = null;
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
        BuildData buildData1 = new BuildData(buildData.getGitData(),buildData.getMysqlData(),buildData.getBuildCommand(),buildData.getBuildWarFile(),buildTargetPath);
        return buildData1;
    }
}
