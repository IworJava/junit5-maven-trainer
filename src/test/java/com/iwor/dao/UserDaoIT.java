package com.iwor.dao;

import com.iwor.entity.Gender;
import com.iwor.entity.Role;
import com.iwor.entity.User;
import com.iwor.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class UserDaoIT extends IntegrationTestBase {

    UserDao userDao = UserDao.getInstance();

    @Test
    void findAll() {
        User user1 = userDao.save(getUser("1@1"));
        User user2 = userDao.save(getUser("2@2"));
        User user3 = userDao.save(getUser("3@3"));

        List<User> actualResult = userDao.findAll();

        assertThat(actualResult.size()).isGreaterThanOrEqualTo(3);
        List<Integer> ids = actualResult.stream()
                .map(User::getId)
                .toList();
        assertThat(ids).contains(user1.getId(), user2.getId(), user3.getId());
    }

    @Test
    void findById() {
        User user = userDao.save(getUser("1@1"));

        Optional<User> actualResult = userDao.findById(user.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    @Test
    void shouldNotFindByIdIfUserDoesNotExist() {

        Optional<User> actualResult = userDao.findById(any());

        assertThat(actualResult).isEmpty();
    }

    @Test
    void save() {
        User user = getUser("1@1");

        User actualResult = userDao.save(user);

        assertNotNull(actualResult.getId());
    }

    @Test
    void findByEmailAndPassword() {
        User user = userDao.save(getUser("1@1"));

        Optional<User> actualResult = userDao
                .findByEmailAndPassword(user.getEmail(), user.getPassword());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    @Test
    void shouldNotFindByEmailAndPasswordIfUserDoesNotExist() {

        Optional<User> actualResult = userDao
                .findByEmailAndPassword("dummy", "dummy");

        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() {
        User user = userDao.save(getUser("1@1"));

        boolean actualResult = userDao.delete(user.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() {
        boolean actualResult = userDao.delete(any());

        assertFalse(actualResult);
    }

    @Test
    void update() {
        User user = userDao.save(getUser("1@1"));
        user.setName("Name1");
        user.setPassword("2222");

        userDao.update(user);
        Optional<User> actualResult = userDao.findById(user.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getName()).isEqualTo("Name1");
        assertThat(actualResult.get().getPassword()).isEqualTo("2222");

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