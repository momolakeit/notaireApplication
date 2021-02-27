package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.model.db.User;
import com.momo.notaireApplication.model.dto.JWTResponse;
import com.momo.notaireApplication.model.dto.UserDTO;
import com.momo.notaireApplication.model.request.LogInDTO;
import com.momo.notaireApplication.model.request.SignUpDTO;
import com.momo.notaireApplication.service.UserService;
import com.momo.notaireApplication.service.authentification.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/email/{email:.+}")
    public UserDTO findUserByEmail(@PathVariable final String email) {
        return userService.foundUserDTOByEmail(email);
    }

    @GetMapping("/{id}")
    public UserDTO findUserById(@PathVariable final Long id) {
        return userService.getUserDTOById(id);
    }
}
