package com.wakabatimes.simplewikisetup.app.interfaces.mysql_data;

import com.wakabatimes.simplewikisetup.app.domain.model.mysql_data.MysqlData;
import lombok.Data;

@Data
public class MysqlDataResponseDto {
    private String mysqlUserName;
    private String mysqlPassword;
    private String mysqlTable;
    private String mysqlHost;
    private String mysqlPort;

    public MysqlDataResponseDto(MysqlData mysqlData) {
        this.mysqlUserName = mysqlData.getMysqlUserName().getValue();
        this.mysqlPassword = mysqlData.getMysqlPassword().getValue();
        this.mysqlTable = mysqlData.getMysqlTable().getValue();
        this.mysqlHost = mysqlData.getMysqlHost().getValue();
        this.mysqlPort = mysqlData.getMysqlPort().getValue();
    }
}
