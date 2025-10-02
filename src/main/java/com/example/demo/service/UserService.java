package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자 등록
     */
    @Transactional
    public User join(User user) {
        validateDuplicateUser(user); // 중복 회원 검증
        userRepository.save(user);
        return user;
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByUserId(user.getUserId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원 조회
     */
    public Page<User> findUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * 회원 단건 조회
     */
    public User findOne(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public void updateUser(Long id, User user) {
        User findUser = userRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("해당 회원이 존재하지 않습니다. id=" + id));

        findUser.setPassword(user.getPassword());
        findUser.setName(user.getName());
        findUser.setPhoneNumber(user.getPhoneNumber());
        findUser.setEmail(user.getEmail());
    }

    /**
     * 회원 정보 삭제
     */
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}