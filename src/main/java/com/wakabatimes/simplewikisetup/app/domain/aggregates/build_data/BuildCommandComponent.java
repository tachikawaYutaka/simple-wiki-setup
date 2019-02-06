package com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "settings")
@Data
public class BuildCommandComponent {
    String buildCommand;
    String buildWarName;
    String buildWarVersion;
}
