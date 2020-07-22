package com.codegym.vndreamers.security.jwt.issuer;

import com.codegym.vndreamers.dtos.JWTResponse;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.auth.AuthService;
import com.codegym.vndreamers.services.user.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class JWTIssuerIntegrationTest {

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
    public static User userMock;

    @Autowired
    AuthService authService;

    @MockBean
    UserService userService;

    @BeforeAll
    static void mockUser() {
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
    @DisplayName("Đăng ký trả về access_token")
    void shouldReturnAccessToken() {
        JWTResponse jwtResponse = authService.register(userMock);
        assertNotNull(jwtResponse.getAccessToken());
    }

    @Test
    @DisplayName("Đăng ký trả về user")
    void shouldReturnUserRegistered() {
        JWTResponse jwtResponse = authService.register(userMock);
        assertNotNull(jwtResponse.getUser());
    }

    @Test
    @DisplayName("Mỗi User có access Token khác nhau")
    void shouldReturnDifferentAccessTokenEachNewUser() {
        User firstRegisterUser = userMock;
        User secondRegisterUser = new User();
        secondRegisterUser.setEmail("second_user@example.com");
        secondRegisterUser.setFirstName("second_user_firstName");
        secondRegisterUser.setLastName("second_user_lastName");
        secondRegisterUser.setAddress("second_user_address");
        secondRegisterUser.setPassword("second_user_password");
        secondRegisterUser.setConfirmPassword("second_user_password");
        secondRegisterUser.setBirthDate(Timestamp.valueOf(LocalDateTime.now()));
        JWTResponse jwtResponseOfFirstRegisterUser = authService.register(firstRegisterUser);
        JWTResponse jwtResponseOfSecondRegisterUser = authService.register(secondRegisterUser);
        String tokenFirstUser = jwtResponseOfFirstRegisterUser.getAccessToken();
        String tokenSecondUser = jwtResponseOfSecondRegisterUser.getAccessToken();
        assertNotEquals(tokenFirstUser, tokenSecondUser);
    }

    @Test
    @DisplayName("user đăng ký lưu vào DB")
    void shouldCallSaveUserMethod() {
        authService.register(userMock);
        verify(userService, times(1)).save(userMock);
    }
}
