package com.iwor.service;

import com.iwor.dao.UserDao;
import com.iwor.dto.CreateUserDto;
import com.iwor.dto.UserDto;
import com.iwor.exception.ValidationException;
import com.iwor.mapper.CreateUserMapper;
import com.iwor.mapper.UserMapper;
import com.iwor.validator.CreateUserValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService {
    private final CreateUserValidator createUserValidator;
    private final CreateUserMapper createUserMapper;
    private final UserMapper userMapper;
    private final UserDao userDao;

    public Optional<UserDto> login(String email, String password) {
        return userDao.findByEmailAndPassword(email, password)
                .map(userMapper::map);
    }

    @SneakyThrows
    public UserDto create(CreateUserDto createUserDto) {
        var validationResult = createUserValidator.validate(createUserDto);
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var userEntity = createUserMapper.map(createUserDto);
        userDao.save(userEntity);

        return userMapper.map(userEntity);
    }
}
