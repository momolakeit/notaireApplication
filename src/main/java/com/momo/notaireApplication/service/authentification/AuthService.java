package com.momo.notaireApplication.service.authentification;

import com.momo.notaireApplication.exception.BadPasswordException;
import com.momo.notaireApplication.exception.validation.BadRoleException;
import com.momo.notaireApplication.exception.validation.UserAlreadyExistsException;
import com.momo.notaireApplication.exception.validation.notFound.UserNotFoundException;
import com.momo.notaireApplication.jwt.JwtProvider;
import com.momo.notaireApplication.model.db.Client;
import com.momo.notaireApplication.model.db.Notaire;
import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.JWTResponse;
import com.momo.notaireApplication.model.request.LogInDTO;
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

    private JwtProvider jwtProvider;

    private final String NOTAIRE_ROLE="NOTAIRE";
    private final String CLIENT_ROLE="CLIENT";


    public AuthService(UserRepository userRepository, PasswordEncoder encoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtProvider = jwtProvider;
    }

    public JWTResponse createUser(SignUpDTO signUpDTO){
        userRepository.findByEmailAdress(signUpDTO.getEmailAdress()).ifPresent(user -> {
                throw new UserAlreadyExistsException();
        });
        switch (signUpDTO.getRole().toUpperCase()){
            case NOTAIRE_ROLE:
                userRepository.save(initUser(new Notaire(),signUpDTO));
                break;
            case CLIENT_ROLE:
                userRepository.save(initUser(new Client(),signUpDTO));
                break;
            default:
                throw new BadRoleException();
        }

        return logInUser(new LogInDTO(signUpDTO.getEmailAdress(),signUpDTO.getPassword()));
    }

    public JWTResponse logInUser(LogInDTO logInDTO){
        User user = userRepository.findByEmailAdress(logInDTO.getEmailAdress()).orElseThrow(UserNotFoundException::new);
        JWTResponse jwt=null ;
        if(encoder.matches(logInDTO.getPassword(), user.getPassword())){
            jwt = new JWTResponse();
            jwt.setToken(jwtProvider.generate(user));
        }else{
            throw new BadPasswordException();
        }
        return jwt;
    }

    private User initUser(User user,SignUpDTO signUpDTO){
        user.setEmailAdress(signUpDTO.getEmailAdress());
        user.setNom(signUpDTO.getNom());
        user.setPrenom(signUpDTO.getPrenom());
        user.setPassword(encoder.encode(signUpDTO.getPassword()));
        return user;
    }
}
