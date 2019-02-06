package com.wakabatimes.simplewikisetup.app.domain.model.git_data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * root entity
 */
@Slf4j
@EqualsAndHashCode
public class GitData {
    @NonNull
    @Getter
    GitUrl gitUrl;

    @NonNull
    @Getter
    GitBranchName gitBranchName;

    @NonNull
    @Getter
    GitRepositoryPath gitRepositoryPath;

    public GitData(GitUrl gitUrl,GitBranchName gitBranchName){
        this.gitUrl = gitUrl;
        this.gitBranchName = gitBranchName;
    }

    public GitData(GitUrl gitUrl,GitBranchName gitBranchName,GitRepositoryPath gitRepositoryPath){
        this.gitUrl = gitUrl;
        this.gitBranchName = gitBranchName;
        this.gitRepositoryPath = gitRepositoryPath;
    }
}
