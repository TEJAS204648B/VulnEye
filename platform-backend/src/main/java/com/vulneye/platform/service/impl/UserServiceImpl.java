package com.vulneye.platform.service.impl;

import com.vulneye.platform.dto.user.CreateUserRequest;
import com.vulneye.platform.dto.user.UserResponse;
import com.vulneye.platform.entity.User;
import com.vulneye.platform.exception.BadRequestException;
import com.vulneye.platform.exception.ResourceNotFoundException;
import com.vulneye.platform.mapper.UserMapper;
import com.vulneye.platform.repository.UserRepository;
import com.vulneye.platform.service.interfaces.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse updateUser(Long id, CreateUserRequest request) {

        User user = getUserEntityById(id);

        userMapper.updateEntity(user, request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        User user = getUserEntityById(id);

        userRepository.delete(user);
    }

    @Override
    public UserResponse getUserById(Long id) {

        return userMapper.toResponse(getUserEntityById(id));
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    private User getUserEntityById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with ID: " + id));
    }
}