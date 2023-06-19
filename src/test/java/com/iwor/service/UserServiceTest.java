package com.iwor.service;

import com.iwor.dao.UserDao;
import com.iwor.dto.CreateUserDto;
import com.iwor.dto.UserDto;
import com.iwor.entity.Gender;
import com.iwor.entity.Role;
import com.iwor.entity.User;
import com.iwor.exception.ValidationException;
import com.iwor.mapper.CreateUserMapper;
import com.iwor.mapper.UserMapper;
import com.iwor.validator.CreateUserValidator;
import com.iwor.validator.Error;
import com.iwor.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDao userDao;
    @Mock
    private CreateUserValidator createUserValidator;
    @Mock
    private CreateUserMapper createUserMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    @Test
    void loginSuccess() {
        User user = getUser();
        UserDto userDto = getUserDto();
        doReturn(Optional.of(user)).when(userDao)
                .findByEmailAndPassword(user.getEmail(), user.getPassword());
        doReturn(userDto).when(userMapper).map(user);

        Optional<UserDto> actualResult = userService.login(user.getEmail(), user.getPassword());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(userDto);
    }

    @Test
    void loginFail() {
        doReturn(Optional.empty()).when(userDao).findByEmailAndPassword(any(), any());

        Optional<UserDto> actualResult = userService.login("dummy", "dummy");

        assertThat(actualResult).isEmpty();
        verifyNoInteractions(userMapper);
    }

    @Test
    void create() {
        CreateUserDto createUserDto = getCreateUserDto();
        User user = getUser();
        UserDto userDto = getUserDto();

        doReturn(new ValidationResult()).when(createUserValidator).validate(createUserDto);
        doReturn(user).when(createUserMapper).map(createUserDto);
        doReturn(userDto).when(userMapper).map(user);

        UserDto actualResult = userService.create(createUserDto);

        assertThat(actualResult).isEqualTo(userDto);
        verify(userDao).save(user);
    }

    @Test
    void shouldThrowExceptionIfDtoInvalid() {
        CreateUserDto createUserDto = getFakeCreateUserDto();
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("dummy", "dummy"));
        doReturn(validationResult).when(createUserValidator).validate(createUserDto);

        Executable executable = () -> userService.create(createUserDto);

        assertThrows(ValidationException.class, executable);
        verifyNoInteractions(createUserMapper, userDao, userMapper);
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

    private static CreateUserDto getFakeCreateUserDto() {
        return CreateUserDto.builder()
                .name("Ivan")
                .birthday("1990-05-23 12:15")
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

    private static User getUser() {
        return User.builder()
                .id(123)
                .name("Ivan")
                .birthday(LocalDate.of(1990, 5, 23))
                .email("1@1")
                .password("321")
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();
    }
}