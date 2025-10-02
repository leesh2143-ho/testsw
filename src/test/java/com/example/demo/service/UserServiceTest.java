package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId("testuser");
        user.setPassword("password");
        user.setName("테스트유저");
        user.setPhoneNumber("010-1234-5678");
        user.setEmail("test@example.com");
    }

    @Test
    @DisplayName("회원가입 성공")
    void join_success() {
        // given
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User savedUser = userService.join(user);

        // then
        assertThat(savedUser.getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    @DisplayName("중복 회원으로 인한 회원가입 실패")
    void join_fail_due_to_duplicate() {
        // given
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(user));

        // when & then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.join(user));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}