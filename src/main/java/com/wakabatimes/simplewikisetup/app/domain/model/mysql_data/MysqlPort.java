package com.wakabatimes.simplewikisetup.app.domain.model.mysql_data;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * value object
 */
@Slf4j
@Value
public class MysqlPort {
    String value;
    public MysqlPort(String value){
        this.value = value;
    }
}
