package com.wakabatimes.simplewikisetup.app.domain.model.git_data;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * value object
 */
@Slf4j
@Value
public class GitBranchName {
    String value;

    public GitBranchName(String value){
        this.value = value;
    }
}
