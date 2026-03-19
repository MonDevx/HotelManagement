package com.gpch.hotel.service;

import com.gpch.hotel.model.ConfirmationToken;
import com.gpch.hotel.model.User;
import com.gpch.hotel.repository.ConfirmationTokenRepository;
import com.gpch.hotel.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void saveUserEncodesPasswordActivatesUserAndPersistsIt() {
        User user = new User();
        user.setPassword("plain-password");
        when(bCryptPasswordEncoder.encode("plain-password")).thenReturn("encoded-password");

        userService.saveUser(user);

        assertEquals("encoded-password", user.getPassword());
        assertEquals(1, user.getActive());
        verify(userRepository).save(user);
    }

    @Test
    void changePasswordEncodesAndSavesLoadedUser() {
        User existingUser = new User();
        existingUser.setId("user-id");
        when(userRepository.findById("user-id")).thenReturn(existingUser);
        when(bCryptPasswordEncoder.encode("new-password")).thenReturn("encoded-new-password");

        userService.Changepassword("user-id", "new-password");

        assertEquals("encoded-new-password", existingUser.getPassword());
        verify(userRepository).save(existingUser);
    }

    @Test
    void createTokenReturnsAndPersistsGeneratedToken() {
        User user = new User();

        String createdToken = userService.Createtoken(user);

        ArgumentCaptor<ConfirmationToken> tokenCaptor = ArgumentCaptor.forClass(ConfirmationToken.class);
        verify(confirmationTokenRepository).save(tokenCaptor.capture());
        ConfirmationToken persistedToken = tokenCaptor.getValue();

        assertNotNull(createdToken);
        assertEquals(createdToken, persistedToken.getConfirmationToken());
        assertSame(user, persistedToken.getUser());
    }
}
