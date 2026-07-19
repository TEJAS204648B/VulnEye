package com.vulneye.platform.service.interfaces;

import com.vulneye.platform.dto.user.CreateUserRequest;
import com.vulneye.platform.dto.user.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse updateUser(Long id, CreateUserRequest request);

    void deleteUser(Long id);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();
}