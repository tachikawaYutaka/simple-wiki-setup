package com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data;

import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitData;
import com.wakabatimes.simplewikisetup.app.domain.model.mysql_data.MysqlData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode
public class BuildData {
    @Getter
    @NonNull
    GitData gitData;

    @Getter
    @NonNull
    MysqlData mysqlData;

    @Getter
    @NonNull
    BuildCommand buildCommand;

    @Getter
    @NonNull
    BuildWarFile buildWarFile;

    @Getter
    @NonNull
    BuildTargetPath buildTargetPath;

    public BuildData(GitData gitData, MysqlData mysqlData,BuildCommand buildCommand, BuildWarFile buildWarFile) {
        this.gitData = gitData;
        this.mysqlData = mysqlData;
        this.buildCommand = buildCommand;
        this.buildWarFile = buildWarFile;
    }

    public BuildData(GitData gitData, MysqlData mysqlData,BuildCommand buildCommand, BuildWarFile buildWarFile, BuildTargetPath buildTargetPath) {
        this.gitData = gitData;
        this.mysqlData = mysqlData;
        this.buildCommand = buildCommand;
        this.buildWarFile = buildWarFile;
        this.buildTargetPath = buildTargetPath;
    }
}
