package com.momo.notaireApplication.service.authentification;

import com.momo.notaireApplication.exception.UserAlreadyExistsException;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.request.SignUpDTO;
import com.momo.notaireApplication.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private UserRepository userRepository;

    private PasswordEncoder encoder;

    private final String NOTAIRE_ROLE="NOTAIRE";
    private final String CLIENT_ROLE="CLIENT";


    public AuthService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void createUser(SignUpDTO signUpDTO){
        userRepository.findByEmailAdress(signUpDTO.getEmailAdress()).ifPresent(user -> {
                throw new UserAlreadyExistsException();
        });
        switch (signUpDTO.getRole()){
            case NOTAIRE_ROLE:
                userRepository.save(initUser(new Notaire(),signUpDTO));
                break;
            case CLIENT_ROLE:
                userRepository.save(initUser(new Client(),signUpDTO));
                break;
        }
    }

    private User initUser(User user,SignUpDTO signUpDTO){
        user.setEmailAdress(signUpDTO.getEmailAdress());
        user.setNom(signUpDTO.getNom());
        user.setPrenom(signUpDTO.getPrenom());
        user.setPassword(encoder.encode(signUpDTO.getPassword()));
        return user;
    }
}
