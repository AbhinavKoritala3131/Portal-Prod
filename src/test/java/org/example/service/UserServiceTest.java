package org.example.service;

import org.example.dto.LoginDTO;
import org.example.entity.AuthorizedUser;
import org.example.entity.User;
import org.example.jwtConfig.JWTService;
import org.example.repository.AuthUsersRepo;
import org.example.repository.UserRepository;
import org.example.security.SSNEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    AuthUsersRepo authUsersRepo;
    @Mock
    UserRepository userRepository;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JWTService jwtService;

    private User user;
    private AuthorizedUser authorizedUser;
    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("John");
        user.setPassword("ThisIsAValidPassword123");
        user.setRole("ADMIN");
        user.setFname("John");
        user.setLname("Doe");
        user.setSsn("123-45-6789");

        authorizedUser=new AuthorizedUser();
        authorizedUser.setUsername("john");
        authorizedUser.setId(3423L);
        authorizedUser.setRole("ADMIN");
    }

    @Test
    void TestRegisterUser() {


        when(authUsersRepo.getByUsername("john")).thenReturn(authorizedUser);
        when(userRepository.save(user)).thenReturn(user);
        try (MockedStatic<SSNEncryptor> mockEncryptor = mockStatic(SSNEncryptor.class)) {
            mockEncryptor.when(() -> SSNEncryptor.encrypt(anyString())).thenReturn("encrypted-ssn");

            ResponseEntity<String> response = userService.register(user);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().contains("John"));

            verify(authUsersRepo).delete(authorizedUser);
            verify(userRepository).save(any(User.class));
        }

    }



    // ✅ TEST 2: Invalid SSN encryption throws exception
    @Test
    void testRegister_InvalidSSN_ThrowsException() {
        when(authUsersRepo.getByUsername("john")).thenReturn(authorizedUser);

        try (MockedStatic<SSNEncryptor> mockEncryptor = mockStatic(SSNEncryptor.class)) {
            mockEncryptor.when(() -> SSNEncryptor.encrypt(anyString())).thenThrow(new RuntimeException("Bad SSN"));

            var ex = assertThrows(
                    org.springframework.web.server.ResponseStatusException.class,
                    () -> userService.register(user)
            );

            assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
        }
    }


    @Test
    void TestLoginUser() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("john");
        loginDTO.setPassword("password123");

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(mockAuth.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken("john")).thenReturn("mocked-jwt");

        ResponseEntity<Map<String, String>> response = userService.login(loginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login Success", response.getBody().get("msg"));
        assertEquals("mocked-jwt", response.getBody().get("jwtToken"));

        verify(jwtService).generateToken("john");
    }

}