package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.validation.notFound.UserNotFoundException;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.UserDTO;
import com.momo.notaireApplication.repositories.UserRepository;
import com.momo.notaireApplication.testUtils.ObjectTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private static final String NOM = "nom";

    private static final String PRENOM = "prenom";

    private static final String EMAIL = "email";


    @Test
    public void getNotaireLanceExceptionAssert() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getUser(Long.valueOf(1));
        });
    }

    @Test
    public void getNotaire() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(ObjectTestUtils.initNotaire()));
        User user = userService.getUser(Long.valueOf(1));
        ObjectTestUtils.assertUsers(NOM,PRENOM,EMAIL,user);
    }
    @Test
    public void getNotaireAvecEmail() {
        Mockito.when(userRepository.findByEmailAdress(anyString())).thenReturn(Optional.of(ObjectTestUtils.initNotaire()));
        User user = userService.foundUserByEmail(EMAIL);
        ObjectTestUtils.assertUsers(NOM,PRENOM,EMAIL,user);
    }
    @Test
    public void saveUser() {
        Mockito.when(userRepository.save(any(User.class))).thenReturn(ObjectTestUtils.initNotaire());
        User user = userService.saveUser(new User());
        ObjectTestUtils.assertUsers(NOM,PRENOM,EMAIL,user);
    }

    @Test
    public void notaireToDTO(){
        Notaire notaire = ObjectTestUtils.initNotaire();
        UserDTO userDTO = userService.toDTO(notaire);
        ObjectTestUtils.assertUsers(userDTO.getNom(),userDTO.getPrenom(),userDTO.getEmailAdress(),notaire);
    }

    @Test
    public void clientToDTO(){
        Client client = ObjectTestUtils.initClient();
        UserDTO userDTO = userService.toDTO(client);
        ObjectTestUtils.assertUsers(userDTO.getNom(),userDTO.getPrenom(),userDTO.getEmailAdress(),client);
    }


}