package com.momo.notaireApplication.service;

import com.momo.notaireApplication.exception.validation.notFound.UserNotFoundException;
import com.momo.notaireApplication.mapping.ClientMapper;
import com.momo.notaireApplication.mapping.NotaireMapper;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.UserDTO;
import com.momo.notaireApplication.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final String CLIENT = "CLIENT";
    private final String NOTAIRE = "NOTAIRE";

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long userID) {
        return userRepository.findById(userID).orElseThrow(UserNotFoundException::new);
    }

    public User foundUserByEmail(String email) {
        return userRepository.findByEmailAdress(email).orElseThrow(UserNotFoundException::new);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public UserDTO toDTO(User user) {
        switch (user.getClass().getSimpleName().toUpperCase()){
            case CLIENT:
                return ClientMapper.instance.toDTO((Client) user);
            case NOTAIRE:
                return NotaireMapper.instance.toDTO((Notaire)user);
            default:
               return null;
        }

    }

}
