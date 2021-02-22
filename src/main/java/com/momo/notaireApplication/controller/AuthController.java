package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.model.dto.JWTResponse;
import com.momo.notaireApplication.model.request.LogInDTO;
import com.momo.notaireApplication.model.request.SignUpDTO;
import com.momo.notaireApplication.service.authentification.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class AuthController extends BaseController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody SignUpDTO signUpDTO) {
        authService.createUser(signUpDTO);
        return ResponseEntity.ok("user created");
    }
    @PostMapping("/logIn")
    public JWTResponse logIn(@RequestBody LogInDTO logInDTO) {
        return authService.logInUser(logInDTO);
    }
}
