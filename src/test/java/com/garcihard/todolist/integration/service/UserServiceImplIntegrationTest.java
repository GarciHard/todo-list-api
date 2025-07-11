package com.garcihard.todolist.integration.service;

//import com.garcihard.todolist.model.dto.UserRequestDto;
//import com.garcihard.todolist.model.entity.User;
//import com.garcihard.todolist.repository.UserRepository;
//import com.garcihard.todolist.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.transaction.annotation.Transactional;
//
//import static com.garcihard.todolist.util.DefaultTestData.INTEGRATION_USER_A;
//import static com.garcihard.todolist.util.DefaultTestData.getDefaultUserRequest;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@Transactional
//@SpringBootTest
//@ActiveProfiles("test")
public class UserServiceImplIntegrationTest {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @MockitoBean
//    private PasswordEncoder encoder;
//
//
//    private UserRequestDto userA;
//
//    @BeforeEach
//    void setupTestData() {
//        userRepository.deleteAll();
//        when(encoder.encode(anyString())).thenReturn("$2a$10$uoE3.n4904pZkwWzcplRpO/qjikU8sp5VaLDr/BZzIeBWEEuaSQS6");
//        userA = getDefaultUserRequest();
//    }
//
//    @Test
//    void shouldCreateUserAndReturn201WhenUserRequestIsValid() {
//        userService.createUser(userA);
//
//        User createdUser = userRepository.findByUsername(INTEGRATION_USER_A).orElse(null);
//        assertNotNull(createdUser);
//        assertEquals(INTEGRATION_USER_A, createdUser.getUsername());
//        assertEquals("$2a$10$uoE3.n4904pZkwWzcplRpO/qjikU8sp5VaLDr/BZzIeBWEEuaSQS6", createdUser.getPassword());
//    }


}
