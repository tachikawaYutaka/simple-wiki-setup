package com.wakabatimes.simplewikisetup.app.domain.service.git_data;

import com.wakabatimes.simplewikisetup.app.domain.model.git_data.GitData;

public interface GitDataService {
    GitData clone(GitData gitData);
}
