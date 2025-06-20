package com.garcihard.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garcihard.todolist.model.dto.UserRequestDto;
import com.garcihard.todolist.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.garcihard.todolist.util.UserConstants;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false) // To bypass the security.
@WebMvcTest(UserController.class)
class UserControllerTest {
    private final static String REGISTER_USER_END_POINT = "/api/v1/auth/register";

    private final static String USERNAME_EMPTY = "";
    private final static String USERNAME_NULL = null;

    private final static String USERNAME_LESS_THAN_SIX_CHARACTERS = "short";
    private final static String USERNAME_GREATER_THAN_TWENTY_CHARACTERS = "aVeryLongUsernameForTesting";

    private final static String PASSWORD_EMPTY = "";
    private final static String PASSWORD_NULL = null;

    private final static String PASSWORD_LESS_THAN_EIGHT_CHARACTERS = "Pa$w0rd";
    private final static String PASSWORD_GREATER_THAN_TEN_CHARACTERS = "aV3rYL0ngPa$$w0rd!";
    private final static String PASSWORD_INVALID_PATTERN = "PASSWORD";

    @Value("${spring.garcihard.testing.username}")
    private String USERNAME_VALID;

    @Value("${spring.garcihard.testing.password}")
    private String PASSWORD_VALID;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;


    @Nested
    @DisplayName("Username field validations")
    class UsernameValidationTests {
        @Test
        void createUser_returns400_whenUsernameIsBlank_and_validPassword() throws Exception {
            UserRequestDto requestUserDto =
                    new UserRequestDto(USERNAME_EMPTY, PASSWORD_VALID);
            String requestBody = objectMapper.writeValueAsString(requestUserDto);

            mockMvc.perform(post(REGISTER_USER_END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(exception -> Assertions.assertInstanceOf(
                            MethodArgumentNotValidException.class,
                            exception.getResolvedException()))
                    .andExpect(
                            jsonPath("$.fieldErrors[*].message",
                                    Matchers.containsInAnyOrder(
                                            UserConstants.USERNAME_DTO_VALIDATION_NOT_EMPTY_MSG,
                                            UserConstants.USERNAME_DTO_VALIDATION_SIZE_MSG
                                    )
                            )
                    );
        }

        @Test
        void createUser_returns400_whenUsernameIsNull_and_validPassword() throws Exception {
            UserRequestDto requestUserDto =
                    new UserRequestDto(USERNAME_NULL, PASSWORD_VALID);
            String requestBody = objectMapper.writeValueAsString(requestUserDto);

            mockMvc.perform(post(REGISTER_USER_END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(exception -> Assertions.assertInstanceOf(
                            MethodArgumentNotValidException.class,
                            exception.getResolvedException()))
                    .andExpect(
                            jsonPath("$.fieldErrors[0].message",
                                    Matchers.containsString(
                                            UserConstants.USERNAME_DTO_VALIDATION_NOT_EMPTY_MSG
                                    )
                            )
                    );
        }

        @Test
        void createUser_returns400_whenUsernameTooShort_and_validPassword() throws Exception {
            UserRequestDto requestUserDto =
                    new UserRequestDto(USERNAME_LESS_THAN_SIX_CHARACTERS, PASSWORD_VALID);
            String requestBody = objectMapper.writeValueAsString(requestUserDto);

            mockMvc.perform(post(REGISTER_USER_END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(exception -> Assertions.assertInstanceOf(
                            MethodArgumentNotValidException.class,
                            exception.getResolvedException()))
                    .andExpect(
                            jsonPath("$.fieldErrors[0].message",
                                    Matchers.containsString(
                                            UserConstants.USERNAME_DTO_VALIDATION_SIZE_MSG
                                    )
                            )
                    );
        }

        @Test
        void createUser_returns400_whenUsernameTooLong_and_validPassword() throws Exception {
            UserRequestDto requestUserDto =
                    new UserRequestDto(USERNAME_GREATER_THAN_TWENTY_CHARACTERS, PASSWORD_VALID);
            String requestBody = objectMapper.writeValueAsString(requestUserDto);

            mockMvc.perform(post(REGISTER_USER_END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(exception -> Assertions.assertInstanceOf(
                            MethodArgumentNotValidException.class,
                            exception.getResolvedException()))
                    .andExpect(
                            jsonPath("$.fieldErrors[0].message",
                                    Matchers.containsString(
                                            UserConstants.USERNAME_DTO_VALIDATION_SIZE_MSG
                                    )
                            )
                    );
        }

    }

    @Nested
    @DisplayName("Password field validations")
    class PasswordValidationTests {
        @Test
        public void shouldReturnBadRequest_and_MethodArgumentNotValidException_whenEmptyPasswordProvided_and_validUsername() throws Exception {
            UserRequestDto requestUserDto =
                    new UserRequestDto(USERNAME_VALID, PASSWORD_EMPTY);
            String requestBody = objectMapper.writeValueAsString(requestUserDto);

            mockMvc.perform(post(REGISTER_USER_END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(exception -> Assertions.assertInstanceOf(
                            MethodArgumentNotValidException.class,
                            exception.getResolvedException()))
                    .andExpect(
                            jsonPath("$.fieldErrors[*].message",
                                    Matchers.containsInAnyOrder(
                                            UserConstants.PASSWORD_DTO_VALIDATION_MISMATCH_PATTERN_MSG,
                                            UserConstants.PASSWORD_DTO_VALIDATION_NOT_EMPTY_MSG,
                                            UserConstants.PASSWORD_DTO_VALIDATION_SIZE_MSG
                                    )
                            )
                    );
        }

        @Test
        public void shouldReturnBadRequest_and_MethodArgumentNotValidException_whenNullPasswordProvided_and_validUsername() throws Exception {
            UserRequestDto requestUserDto =
                    new UserRequestDto(USERNAME_VALID, PASSWORD_NULL);
            String requestBody = objectMapper.writeValueAsString(requestUserDto);

            mockMvc.perform(post(REGISTER_USER_END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(exception -> Assertions.assertInstanceOf(
                            MethodArgumentNotValidException.class,
                            exception.getResolvedException()))
                    .andExpect(
                            jsonPath("$.fieldErrors[0].message",
                                    Matchers.containsString(
                                            UserConstants.PASSWORD_DTO_VALIDATION_NOT_EMPTY_MSG
                                    )
                            )
                    );
        }

        @Test
        public void shouldReturnBadRequest_and_MethodArgumentNotValidException_whenLessThanEightCharactersPasswordProvided_and_validUsername() throws Exception {
            UserRequestDto requestUserDto =
                    new UserRequestDto(USERNAME_VALID, PASSWORD_LESS_THAN_EIGHT_CHARACTERS);
            String requestBody = objectMapper.writeValueAsString(requestUserDto);

            mockMvc.perform(post(REGISTER_USER_END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(exception -> Assertions.assertInstanceOf(
                            MethodArgumentNotValidException.class,
                            exception.getResolvedException()))
                    .andExpect(
                            jsonPath("$.fieldErrors[0].message",
                                    Matchers.containsString(
                                            UserConstants.PASSWORD_DTO_VALIDATION_SIZE_MSG
                                    )
                            )
                    );
        }

        @Test
        public void shouldReturnBadRequest_and_MethodArgumentNotValidException_whenGreaterThanTenCharactersPasswordProvided_and_validUsername() throws Exception {
            UserRequestDto requestUserDto =
                    new UserRequestDto(USERNAME_VALID, PASSWORD_GREATER_THAN_TEN_CHARACTERS);
            String requestBody = objectMapper.writeValueAsString(requestUserDto);

            mockMvc.perform(post(REGISTER_USER_END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(exception -> Assertions.assertInstanceOf(
                            MethodArgumentNotValidException.class,
                            exception.getResolvedException()))
                    .andExpect(
                            jsonPath("$.fieldErrors[0].message",
                                    Matchers.containsString(
                                            UserConstants.PASSWORD_DTO_VALIDATION_SIZE_MSG
                                    )
                            )
                    );
        }

        @Test
        public void shouldReturnBadRequest_and_MethodArgumentNotValidException_whenInvalidPatternPasswordProvided_and_validUsername() throws Exception {
            UserRequestDto requestUserDto =
                    new UserRequestDto(USERNAME_VALID, PASSWORD_INVALID_PATTERN);
            String requestBody = objectMapper.writeValueAsString(requestUserDto);

            mockMvc.perform(post(REGISTER_USER_END_POINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(exception -> Assertions.assertInstanceOf(
                            MethodArgumentNotValidException.class,
                            exception.getResolvedException()))
                    .andExpect(
                            jsonPath("$.fieldErrors[0].message",
                                    Matchers.containsString(
                                            UserConstants.PASSWORD_DTO_VALIDATION_MISMATCH_PATTERN_MSG
                                    )
                            )
                    );
        }
    }

    @Test
    void createUser_returns400_whenRequestIsInvalid() throws Exception {
        UserRequestDto requestUserDto =
                new UserRequestDto(USERNAME_NULL, PASSWORD_NULL);
        String requestBody = objectMapper.writeValueAsString(requestUserDto);

        mockMvc.perform(post(REGISTER_USER_END_POINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(exception -> Assertions.assertInstanceOf(
                        MethodArgumentNotValidException.class,
                        exception.getResolvedException()))
                .andExpect(
                        jsonPath("$.fieldErrors[*].message",
                                Matchers.containsInAnyOrder(
                                        UserConstants.USERNAME_DTO_VALIDATION_NOT_EMPTY_MSG,
                                        UserConstants.PASSWORD_DTO_VALIDATION_NOT_EMPTY_MSG
                                )
                        )
                );
    }

    @Test
    public void shouldReturnCreated_whenValidUsernameProvided_and_validPassword() throws Exception {
        UserRequestDto requestUserDto =
                new UserRequestDto(USERNAME_VALID, PASSWORD_VALID);
        String requestBody = objectMapper.writeValueAsString(requestUserDto);

        mockMvc.perform(post(REGISTER_USER_END_POINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }
}
