package com.momo.notaireApplication.service.authentification;

import com.momo.notaireApplication.exception.UserAlreadyExistsException;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.request.SignUpDTO;
import com.momo.notaireApplication.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    public final static String EMAIL_ADRESS = "momo@email.com";

    public final static String PRENOM = "Killua";

    public final static String NOM_DE_FAMILLE = "Uzumaki";

    public final static String NOTAIRE_ROLE = "NOTAIRE";

    public final static String CLIENT_ROLE = "CLIENT";

    public final static String PASSWORD = "dontHackMe";


    @Test
    public void testCreateUserNotaire() {
        initMocks();
        SignUpDTO signUpDTO = initSignUpDTONotaireRole();
        authService.createUser(signUpDTO);
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        Notaire notaire = (Notaire) userArgumentCaptor.getValue();
        assertEquals(EMAIL_ADRESS, notaire.getEmailAdress());
        assertEquals(PASSWORD, notaire.getPassword());
        assertEquals(PRENOM, notaire.getPrenom());
        assertEquals(NOM_DE_FAMILLE, notaire.getNom());
    }

    @Test
    public void testCreateUserExistingThrowException() {
        when(userRepository.findByEmailAdress(anyString())).thenReturn(Optional.of(new Notaire()));
        SignUpDTO signUpDTO = initSignUpDTONotaireRole();
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            authService.createUser(signUpDTO);
        });
    }


    @Test
    public void testCreateUserClient() {
        initMocks();
        SignUpDTO signUpDTO = initSignUpDTOClientRole();
        authService.createUser(signUpDTO);
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        Client client = (Client) userArgumentCaptor.getValue();
        assertEquals(EMAIL_ADRESS, client.getEmailAdress());
        assertEquals(PASSWORD, client.getPassword());
        assertEquals(PRENOM, client.getPrenom());
        assertEquals(NOM_DE_FAMILLE, client.getNom());
    }


    private void initMocks() {
        when(passwordEncoder.encode(anyString())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
    }

    private static SignUpDTO initSignUpDTO() {
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setEmailAdress(EMAIL_ADRESS);
        signUpDTO.setNom(NOM_DE_FAMILLE);
        signUpDTO.setPrenom(PRENOM);
        signUpDTO.setPassword(PASSWORD);
        return signUpDTO;
    }

    public static SignUpDTO initSignUpDTONotaireRole() {
        SignUpDTO signUpDTO = initSignUpDTO();
        signUpDTO.setRole(NOTAIRE_ROLE);
        return signUpDTO;
    }

    public static SignUpDTO initSignUpDTOClientRole() {
        SignUpDTO signUpDTO = initSignUpDTO();
        signUpDTO.setRole(CLIENT_ROLE);
        return signUpDTO;
    }

}