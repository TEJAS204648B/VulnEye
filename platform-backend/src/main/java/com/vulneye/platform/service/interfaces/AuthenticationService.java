package com.vulneye.platform.service.interfaces;

import com.vulneye.platform.dto.auth.LoginRequest;
import com.vulneye.platform.dto.auth.LoginResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest loginRequest);

}