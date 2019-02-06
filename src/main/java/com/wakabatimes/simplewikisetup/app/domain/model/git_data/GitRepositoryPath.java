package com.wakabatimes.simplewikisetup.app.domain.model.git_data;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * value object
 */
@Slf4j
@Value
public class GitRepositoryPath {
    String value;

    public GitRepositoryPath(String value){
        this.value = value;
    }
}
