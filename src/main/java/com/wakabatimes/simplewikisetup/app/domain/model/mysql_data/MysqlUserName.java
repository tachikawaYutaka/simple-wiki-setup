package com.wakabatimes.simplewikisetup.app.domain.model.mysql_data;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * value object
 */
@Slf4j
@Value
public class MysqlUserName {
    String value;

    public MysqlUserName(String value){
        validatedValue(value);
        this.value = value;
    }

    private void validatedValue(String value) {
        String inputPattern = "^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]+$";
        if (!Pattern.matches(inputPattern, value)) {
            throw new RuntimeException("ユーザー名の入力が正しくありません。半角英数で入力してください。");
        }

        Integer count = value.length();
        if(count < 1 || count > 255) {
            throw new RuntimeException("ユーザー名の入力が正しくありません。1～255字内で入力してください。");
        }
    }
}
