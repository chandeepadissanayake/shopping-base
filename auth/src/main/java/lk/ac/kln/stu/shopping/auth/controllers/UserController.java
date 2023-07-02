package lk.ac.kln.stu.shopping.auth.controllers;

import lk.ac.kln.stu.shopping.auth.models.User;
import lk.ac.kln.stu.shopping.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping({"user", "user/"})
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public User getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuthorization) {
        String jwtToken = headerAuthorization.substring(7);
        Optional<User> user = this.userService.getUser(jwtToken);
        // Since guaranteed to exist, we directly get the user.
        return user.get();
    }

}
