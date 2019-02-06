package com.wakabatimes.simplewikisetup.app.domain.model.git_data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "settings")
@Data
public class GitUrlComponent {
    private String gitUrl;
    private String branch;
}
