package com.momo.notaireApplication.controller;

import com.momo.notaireApplication.model.dto.UserDTO;
import com.momo.notaireApplication.model.request.UserSearchQuery;
import com.momo.notaireApplication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/search")
    public List<UserDTO> findUsersListByQuery(@RequestBody UserSearchQuery userSearchQuery){
        return userService.searchUsersByQuery(userSearchQuery.getQuery());
    }
}
