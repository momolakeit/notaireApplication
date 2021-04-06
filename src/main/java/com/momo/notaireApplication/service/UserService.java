package com.momo.notaireApplication.service;

import com.momo.notaireApplication.controller.request.HeaderCatcherService;
import com.momo.notaireApplication.exception.validation.notFound.UserNotFoundException;
import com.momo.notaireApplication.jwt.JwtAuthentication;
import com.momo.notaireApplication.mapping.UserMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.UserDTO;
import com.momo.notaireApplication.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final String CLIENT = "CLIENT";
    private final String NOTAIRE = "NOTAIRE";

    private UserRepository userRepository;

    private HeaderCatcherService headerCatcherService;

    public UserService(UserRepository userRepository, HeaderCatcherService headerCatcherService) {
        this.userRepository = userRepository;
        this.headerCatcherService = headerCatcherService;
    }

    public User getUser(Long userID) {
        return userRepository.findById(userID).orElseThrow(UserNotFoundException::new);
    }

    public User foundUserByEmail(String email) {
        return userRepository.findByEmailAdress(email).orElseThrow(UserNotFoundException::new);
    }

    public UserDTO foundUserDTOByEmail(String email) {
        User user = foundUserByEmail(email);
        return toDTO(this.filterUserInfo(user));
    }

    public UserDTO getUserDTOById(Long id) {
        User user = getUser(id);
        return toDTO(this.filterUserInfo(user));
    }

    public List<UserDTO> searchUsersByQuery(String searchQuery) {
        String roleDuUserAChercher = "";

        switch (headerCatcherService.getUserRole().toUpperCase()) {
            case CLIENT:
                roleDuUserAChercher = NOTAIRE;
                break;
            case NOTAIRE:
                roleDuUserAChercher = CLIENT;
                break;
            default:
                break;
        }

        String finalRoleDuUserAChercher = roleDuUserAChercher;
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getClass().getSimpleName().equalsIgnoreCase(finalRoleDuUserAChercher))
                .filter(user -> filterByUsersByName(searchQuery, user))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> saveMutlipleUsers(List<User> users) {
        return userRepository.saveAll(users);
    }


    private UserDTO toDTO(User user) {
        switch (user.getClass().getSimpleName().toUpperCase()) {
            case CLIENT:
                return UserMapper.instance.toDTO((Client) user);
            case NOTAIRE:
                return UserMapper.instance.toDTO((Notaire) user);
            default:
                return null;
        }

    }

    private User filterUserInfo(User user) {
        Long loggedUserId = this.headerCatcherService.getLoggedUserId();
        String role = headerCatcherService.getUserRole();
        if(!loggedUserId.equals(user.getId())){
            user.setFactures(null);
            user.setMessages(null);
            user.setFichierDocuments(null);
            user.setConversations(null);
        }
        if(role.equalsIgnoreCase("Notaire")){
            user.setRendezVous(null);
        }
        return user;
    }

    private Boolean filterByUsersByName(String query, User user) {
        return user.getNom().toLowerCase().contains(query.toLowerCase()) || user.getPrenom().toLowerCase().contains(query.toLowerCase());
    }

}
