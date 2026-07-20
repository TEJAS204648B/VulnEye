package com.vulneye.platform.service.interfaces;

import com.vulneye.platform.dto.auth.LoginRequest;

public interface AuthenticationService {

    void authenticate(LoginRequest loginRequest);

}