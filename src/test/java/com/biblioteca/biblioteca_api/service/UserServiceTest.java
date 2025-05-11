package com.biblioteca.biblioteca_api.service;

import com.biblioteca.biblioteca_api.dto.UserRegistrationDto;
import com.biblioteca.biblioteca_api.model.Role;
import com.biblioteca.biblioteca_api.model.User;
import com.biblioteca.biblioteca_api.repository.RoleRepository;
import com.biblioteca.biblioteca_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> emailCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_whenValid_shouldSaveUserAndSendEmail() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("test@example.com");
        dto.setName("Test User");
        dto.setPassword("password123");
        dto.setRoles(List.of("USER"));

        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setRole("USER");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(roleRepository.findByRole("USER")).thenReturn(Optional.of(userRole));

        userService.registerUser(dto);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("Test User", savedUser.getName());
        assertEquals("password123", savedUser.getPassword());
        assertEquals(1, savedUser.getRoles().size());
        assertEquals("USER", savedUser.getRoles().get(0).getRole());

        verify(mailSender).send(emailCaptor.capture());
        SimpleMailMessage email = emailCaptor.getValue();

        assertEquals("test@example.com", email.getTo()[0]);
        assertEquals("bibliotecaprueba184@gmail.com", email.getFrom());
        assertEquals("Welcome to the library", email.getSubject());
        assertTrue(email.getText().contains("Hello Test User"));
    }

    @Test
    void testRegisterUser_whenEmailAlreadyExists_shouldThrowException() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("existing@example.com");
        dto.setName("Existing User");
        dto.setPassword("password123");
        dto.setRoles(List.of("USER"));

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("existing@example.com");

        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(dto);
        });
        assertEquals("Email already in use.", exception.getMessage());
        verify(userRepository, never()).save(any());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void testRegisterUser_whenRoleNotFound_shouldThrowException() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("test@example.com");
        dto.setName("User With Bad Role");
        dto.setPassword("password123");
        dto.setRoles(List.of("NON_EXISTENT_ROLE"));

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(roleRepository.findByRole("NON_EXISTENT_ROLE")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(dto);
        });

        assertEquals("Invalid role: NON_EXISTENT_ROLE", exception.getMessage());
        verify(userRepository, never()).save(any());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

}