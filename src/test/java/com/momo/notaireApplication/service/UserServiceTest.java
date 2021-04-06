package com.momo.notaireApplication.service;

import com.momo.notaireApplication.controller.request.HeaderCatcherService;
import com.momo.notaireApplication.exception.validation.notFound.UserNotFoundException;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.ClientDTO;
import com.momo.notaireApplication.model.dto.NotaireDTO;
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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HeaderCatcherService headerCatcherService;

    private static final String NOM = "nom";

    private static final String PRENOM = "prenom";

    private static final String EMAIL = "email";


    @Test
    public void getNotaireLanceExceptionAssert() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getUser(1L);
        });
    }

    @Test
    public void getNotaire() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(ObjectTestUtils.initNotaire()));
        User user = userService.getUser(1L);
        ObjectTestUtils.assertUsers(NOM, PRENOM, EMAIL, user);
    }

    @Test
    public void getNotaireDTOSameUserNoInfoFilterd() {
        Notaire notaire = ObjectTestUtils.initNotaire();
        notaire.setConversations(new ArrayList<>());
        notaire.setRendezVous(new ArrayList<>());
        notaire.setFactures(new ArrayList<>());
        notaire.setFichierDocuments(new ArrayList<>());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(notaire));
        when(headerCatcherService.getLoggedUserId()).thenReturn(1L);
        UserDTO user = userService.getUserDTOById(1L);
        assertNotNull(user.getFactures());
        assertNotNull(user.getFichierDocuments());
    }
    @Test
    public void getNotaireDTOSameUserInfoFilterd() {
        Notaire notaire = ObjectTestUtils.initNotaire();
        notaire.setConversations(new ArrayList<>());
        notaire.setRendezVous(new ArrayList<>());
        notaire.setFactures(new ArrayList<>());
        notaire.setFichierDocuments(new ArrayList<>());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(notaire));
        when(headerCatcherService.getLoggedUserId()).thenReturn(2L);
        UserDTO user = userService.getUserDTOById(1L);
        assertNull(user.getFactures());
        assertNull(user.getFichierDocuments());
    }

    @Test
    public void getNotaireAvecEmail() {
        when(userRepository.findByEmailAdress(anyString())).thenReturn(Optional.of(ObjectTestUtils.initNotaire()));
        User user = userService.foundUserByEmail(EMAIL);
        ObjectTestUtils.assertUsers(NOM, PRENOM, EMAIL, user);
    }

    @Test
    public void saveUser() {
        when(userRepository.save(any(User.class))).thenReturn(ObjectTestUtils.initNotaire());
        User user = userService.saveUser(new User());
        ObjectTestUtils.assertUsers(NOM, PRENOM, EMAIL, user);
    }

    @Test
    public void notaireByEmailToDTO() {
        Notaire notaire = ObjectTestUtils.initNotaire();
        when(userRepository.findByEmailAdress(anyString())).thenReturn(Optional.of(notaire));
        UserDTO userDTO = userService.foundUserDTOByEmail("email@mail.com");
        ObjectTestUtils.assertUsers(userDTO.getNom(), userDTO.getPrenom(), userDTO.getEmailAdress(), notaire);
    }

    @Test
    public void clientByEmailToDTO() {
        Client client = ObjectTestUtils.initClient();
        when(userRepository.findByEmailAdress(anyString())).thenReturn(Optional.of(client));
        UserDTO userDTO = userService.foundUserDTOByEmail("email@mail");
        ObjectTestUtils.assertUsers(userDTO.getNom(), userDTO.getPrenom(), userDTO.getEmailAdress(), client);
    }

    @Test
    public void notaireByIdToDTO() {
        Notaire notaire = ObjectTestUtils.initNotaire();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(notaire));
        UserDTO userDTO = userService.getUserDTOById(1L);
        ObjectTestUtils.assertUsers(userDTO.getNom(), userDTO.getPrenom(), userDTO.getEmailAdress(), notaire);
    }

    @Test
    public void clientByIdToDTO() {
        Client client = ObjectTestUtils.initClient();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(client));
        UserDTO userDTO = userService.getUserDTOById(1L);
        ObjectTestUtils.assertUsers(userDTO.getNom(), userDTO.getPrenom(), userDTO.getEmailAdress(), client);
    }

    @Test
    public void searchUsersByNotaireReturnsAllClient() {
        Client client = ObjectTestUtils.initClient();
        Notaire notaire = ObjectTestUtils.initNotaire();
        notaire.setPrenom("wesh");
        notaire.setNom("mec");
        when(headerCatcherService.getUserRole()).thenReturn("notaire");

        when(userRepository.findAll()).thenReturn(Arrays.asList(notaire, client));
        List<UserDTO> userDTOList = userService.searchUsersByQuery("nom");
        assertEquals(1, userDTOList.size());

    }

    @Test
    public void searchUsersByClientReturnsAllNotaire() {
        Client client = ObjectTestUtils.initClient();
        Notaire notaire = ObjectTestUtils.initNotaire();
        notaire.setPrenom("wesh");
        notaire.setNom("mec");
        when(headerCatcherService.getUserRole()).thenReturn("client");

        when(userRepository.findAll()).thenReturn(Arrays.asList(notaire, client));
        List<UserDTO> userDTOList = userService.searchUsersByQuery("wesh");
        assertEquals(1, userDTOList.size());

    }

}