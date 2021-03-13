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
@RequestMapping("/auth")
public class AuthController extends BaseController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<JWTResponse> createUser(@RequestBody SignUpDTO signUpDTO) {
        return ResponseEntity.ok(authService.createUser(signUpDTO));
    }
    @PostMapping("/logIn")
    public ResponseEntity<JWTResponse> logIn(@RequestBody LogInDTO logInDTO) {
        return ResponseEntity.ok(authService.logInUser(logInDTO));
    }
}
