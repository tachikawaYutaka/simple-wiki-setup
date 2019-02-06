package com.wakabatimes.simplewikisetup.app.domain.service.build_data;

import com.wakabatimes.simplewikisetup.app.domain.aggregates.build_data.BuildData;

public interface BuildDataService {
    BuildData build(BuildData buildData);
}
