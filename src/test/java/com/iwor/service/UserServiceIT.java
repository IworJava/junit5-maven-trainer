package com.iwor.service;

import com.iwor.dao.UserDao;
import com.iwor.dto.CreateUserDto;
import com.iwor.dto.UserDto;
import com.iwor.entity.Gender;
import com.iwor.entity.Role;
import com.iwor.entity.User;
import com.iwor.integration.IntegrationTestBase;
import com.iwor.mapper.CreateUserMapper;
import com.iwor.mapper.UserMapper;
import com.iwor.validator.CreateUserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserServiceIT extends IntegrationTestBase {

    UserDao userDao;
    UserService userService;

    @BeforeEach
    void init() {
        userDao = UserDao.getInstance();
        userService = new UserService(
                CreateUserValidator.getInstance(),
                CreateUserMapper.getInstance(),
                UserMapper.getInstance(),
                userDao
        );
    }

    @Test
    void login() {
        User user = userDao.save(getUser("1@1"));

        Optional<UserDto> actualResult = userService.login(user.getEmail(), user.getPassword());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void create() {
        CreateUserDto createUserDto = getCreateUserDto();

        UserDto actualResult = userService.create(createUserDto);

        assertNotNull(actualResult.getId());
    }

    private static CreateUserDto getCreateUserDto() {
        return CreateUserDto.builder()
                .name("Ivan")
                .birthday("1990-05-23")
                .email("1@1")
                .password("321")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();
    }

    private static UserDto getUserDto() {
        return UserDto.builder()
                .id(123)
                .name("Ivan")
                .birthday(LocalDate.of(1990, 5, 23))
                .email("1@1")
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();
    }

    private static User getUser(String email) {
        return User.builder()
                .name("Ivan")
                .birthday(LocalDate.of(1990, 5, 23))
                .email(email)
                .password("321")
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();
    }
}