package com.wakabatimes.simplewikisetup.app.interfaces.mysql_data;

import lombok.Data;

@Data
public class MysqlDataForm {
    private String mysqlUserName;
    private String mysqlPassword;
    private String mysqlTable;
    private String mysqlHost;
    private String mysqlPort;
}
