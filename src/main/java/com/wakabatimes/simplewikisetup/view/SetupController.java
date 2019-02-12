package com.wakabatimes.simplewikisetup.view;

import com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data.BuildCommand;
import com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data.BuildCommandComponent;
import com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data.BuildData;
import com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data.BuildWarFile;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitBranchName;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitData;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitUrl;
import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitUrlComponent;
import com.wakabatimes.simplewikisetup.app.domain.model.mysql_data.*;
import com.wakabatimes.simplewikisetup.app.domain.service.build_data.BuildDataService;
import com.wakabatimes.simplewikisetup.app.domain.service.git_data.GitDataService;
import com.wakabatimes.simplewikisetup.app.interfaces.mysql_data.MysqlDataForm;
import com.wakabatimes.simplewikisetup.app.interfaces.mysql_data.MysqlDataResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Controller
public class SetupController {
    @Autowired
    private BuildDataService buildDataService;
    @Autowired
    private GitDataService gitDataService;
    @Autowired
    private BuildCommandComponent buildCommandComponent;
    @Autowired
    private GitUrlComponent gitUrlComponent;

    @GetMapping("/")
    public String setup(){
        return "home";
    }

    @PostMapping("/confirm")
    public String confirm(@ModelAttribute MysqlDataForm form, Model model, RedirectAttributes attr){
        try{
            MysqlUserName mysqlUserName = new MysqlUserName(form.getMysqlUserName());
            MysqlPassword mysqlPassword = new MysqlPassword(form.getMysqlPassword());
            MysqlTable mysqlTable = new MysqlTable(form.getMysqlTable());
            MysqlHost mysqlHost = new MysqlHost(form.getMysqlHost());
            MysqlPort mysqlPort = new MysqlPort(form.getMysqlPort());
            MysqlData mysqlData = new MysqlData(mysqlUserName,mysqlPassword,mysqlTable,mysqlHost,mysqlPort);
            MysqlDataResponseDto mysqlDataResponseDto = new MysqlDataResponseDto(mysqlData);
            model.addAttribute("form",mysqlDataResponseDto);
        }catch(RuntimeException e){
            log.error("Error",e);
            attr.addFlashAttribute("error",true);
            attr.addFlashAttribute("errorMessage",e.getMessage());
            return "redirect:/";
        }
        return "confirm";
    }

    @PostMapping("/complete")
    public String  complete(@ModelAttribute MysqlDataForm form,HttpServletResponse response){
        try{
            MysqlUserName mysqlUserName = new MysqlUserName(form.getMysqlUserName());
            MysqlPassword mysqlPassword = new MysqlPassword(form.getMysqlPassword());
            MysqlTable mysqlTable = new MysqlTable(form.getMysqlTable());
            MysqlHost mysqlHost = new MysqlHost(form.getMysqlHost());
            MysqlPort mysqlPort = new MysqlPort(form.getMysqlPort());
            MysqlData mysqlData = new MysqlData(mysqlUserName,mysqlPassword,mysqlTable,mysqlHost,mysqlPort);

            String url = gitUrlComponent.getGitUrl();
            String branch = gitUrlComponent.getBranch();
            GitUrl gitUrl = new GitUrl(url);
            GitBranchName gitBranchName = new GitBranchName(branch);
            GitData gitData = new GitData(gitUrl,gitBranchName);
            GitData gitData1 = gitDataService.clone(gitData);

            BuildCommand buildCommand = new BuildCommand(buildCommandComponent.getBuildCommand());

            String warName = buildCommandComponent.getBuildWarName();
            String versionName = buildCommandComponent.getBuildWarVersion();
            BuildWarFile buildWarFile = new BuildWarFile(warName + "-" + versionName + ".war");
            BuildData buildData = new BuildData(gitData1,mysqlData,buildCommand,buildWarFile);
            BuildData result = buildDataService.build(buildData);
            return download(result,response);
        }catch(RuntimeException e){
            log.error("Error",e);
        }
        return null;
    }

    @PostMapping("/download")
    public String download(BuildData result, HttpServletResponse response){
        response.setContentType("application/octet-stream;charset=MS932");
        response.setHeader("Content-Disposition", "attachment; filename=simpleWiki.zip");
        response.setHeader("Content-Transfer-Encoding", "binary");

        String resultData = result.getBuildTargetPath().getValue() + result.getBuildWarFile().getValue();
        File warFile = new File(resultData);
        if(warFile.exists()){
            try {
                OutputStream os = response.getOutputStream();
                ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(os));
                ZipEntry ze = new ZipEntry(result.getBuildWarFile().getValue());
                zos.putNextEntry(ze);

                InputStream in = new BufferedInputStream(new FileInputStream(result.getBuildTargetPath().getValue() + result.getBuildWarFile().getValue()));

                byte[] b = new byte[1024];
                int len;
                while((len = in.read(b)) != -1) {
                    zos.write(b, 0, len);
                }
                in.close();
                zos.closeEntry();
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            log.error("Error","指定されたファイルがありません。");
        }
        return null;
    }

}
