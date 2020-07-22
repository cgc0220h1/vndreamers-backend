package com.codegym.vndreamers.security.jwt.issuer;

import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.repositories.UserRepository;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTest {
    private static final String VALID_USERNAME = "some_valid_username";
    private static final String VALID_PASSWORD = "some_valid_password";
    private static final String VALID_EMAIL = "some_valid_email@example.com";
    private static final Timestamp VALID_BIRTH_DATE = Timestamp.valueOf(LocalDateTime.now());
    private static final String VALID_PHONE = "0912345678";
    public static final String VALID_AVATAR = "https://giaitri.vn/wp-content/uploads/2019/07/avatar-la-gi-01.jpg";
    public static final int STATUS_ACTIVE = 1;
    public static final String FAIL_PASSWORD = "some_fail_password";
    public static final String FAIL_USERNAME = "some_fail_username";
    private static final String FAIL_EMAIL = "some_fail_email";
    private static final String FAIL_BIRTH_DATE = "some_fail_date";
    public static final String VALID_FIRST_NAME = "valid_first_name";
    public static final String VALID_LAST_NAME = "valid_last_name";

    private static User userMock;

    private UserCRUDService userService;

    private UserRepository userRepository;

    @Autowired
    public UserServiceTest(UserCRUDService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void mockUser() {
        userMock = new User();
        userMock.setEmail(VALID_EMAIL);
        userMock.setFirstName(VALID_FIRST_NAME);
        userMock.setLastName(VALID_LAST_NAME);
        userMock.setPassword(VALID_PASSWORD);
        userMock.setConfirmPassword(VALID_PASSWORD);
        userMock.setGender(1);
        userMock.setBirthDate(VALID_BIRTH_DATE);
        userMock.setImage(VALID_AVATAR);
    }

    @Test
    @DisplayName("Lưu User vào DB")
    void shouldSaveUserToDB() {
        userRepository.save(userMock);
        User userSaved = userService.findById(1);
        assertNotNull(userSaved);
    }
}
