package com.wakabatimes.simplewikisetup.app.domain.model.mysql_data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * root entity
 */
@Slf4j
@EqualsAndHashCode
public class MysqlData {
    @Getter
    @NonNull
    MysqlUserName mysqlUserName;

    @Getter
    @NonNull
    MysqlPassword mysqlPassword;

    @Getter
    @NonNull
    MysqlTable mysqlTable;

    @Getter
    @NonNull
    MysqlHost mysqlHost;

    @Getter
    @NonNull
    MysqlPort mysqlPort;

    public MysqlData(MysqlUserName mysqlUserName,MysqlPassword mysqlPassword,MysqlTable mysqlTable,MysqlHost mysqlHost,MysqlPort mysqlPort){
        this.mysqlUserName = mysqlUserName;
        this.mysqlPassword = mysqlPassword;
        this.mysqlTable = mysqlTable;
        this.mysqlHost = mysqlHost;
        this.mysqlPort = mysqlPort;
    }
}
