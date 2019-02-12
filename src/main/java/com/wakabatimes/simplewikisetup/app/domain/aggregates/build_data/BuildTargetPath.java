package com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * value object
 */
@Slf4j
@Value
public class BuildTargetPath {
    String value;

    public BuildTargetPath(String value){
        this.value = value;
    }
}
