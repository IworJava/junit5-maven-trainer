package com.iwor.mapper;

import com.iwor.dto.UserDto;
import com.iwor.entity.Gender;
import com.iwor.entity.Role;
import com.iwor.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    private final UserMapper mapper = UserMapper.getInstance();

    @Test
    void map() {
        User user = User.builder()
                .id(123)
                .name("Ivan")
                .birthday(LocalDate.of(1990, 5, 23))
                .email("1@1")
                .password("123")
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();

        UserDto actualResult = mapper.map(user);
        UserDto expectedResult = UserDto.builder()
                .id(123)
                .name("Ivan")
                .birthday(LocalDate.of(1990, 5, 23))
                .email("1@1")
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}