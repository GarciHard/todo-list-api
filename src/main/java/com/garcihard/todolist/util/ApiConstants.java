package com.garcihard.todolist.util;

public class ApiConstants {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CODE_UNAUTHORIZED = "UNAUTHORIZED";

    public static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";

    public final static String USERNAME_DTO_VALIDATION_NOT_EMPTY_MSG = "Username is mandatory.";
    public final static String USERNAME_DTO_VALIDATION_SIZE_MSG = "Username size must be between 6 and 20 characters.";

    public final static String PASSWORD_DTO_VALIDATION_MISMATCH_PATTERN_MSG = "Invalid password format, must contain at least: 1 uppercase, 1 lowercase, 1 number & 1 special character.";
    public final static String PASSWORD_DTO_VALIDATION_NOT_EMPTY_MSG = "Password is mandatory.";
    public final static String PASSWORD_DTO_VALIDATION_SIZE_MSG = "Password size must be between 8 and 10.";

    public final static String TASK_TITLE_DTO_VALIDATION_NOT_EMPTY = "Title is mandatory.";

    public final static String REQUEST_VALIDATION_CODE = "VALIDATION";
    public final static String REQUEST_VALIDATION_FAILED = "Validation Failed for the given request.";

    public final static String USER_CONFLICT_CODE = "CONFLICT";
    public final static String USER_ALREADY_TAKEN = "Username '%s' is already taken.";

    public final static String USER_FORBIDDEN_CODE = "NOT_FOUND";
    public final static String USER_FORBIDDEN_RESOURCE = "Task not found.";

    public final static String INVALID_CREDENTIALS_CODE = "UNAUTHORIZED";
    public final static String INVALID_CREDENTIALS = "Invalid credentials, try again.";

}
