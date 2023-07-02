package lk.ac.kln.stu.shopping.auth.controllers;

import lk.ac.kln.stu.shopping.auth.authentication.AuthRequestModel;
import lk.ac.kln.stu.shopping.auth.authentication.AuthResponseModel;
import lk.ac.kln.stu.shopping.auth.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping({"", "/"})
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseModel> login(@RequestBody AuthRequestModel request) {
        try {
            String jwtToken = this.authService.setLogin(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(new AuthResponseModel(jwtToken, null));
        }

        catch (DisabledException disabledException) {
            return new ResponseEntity<>(new AuthResponseModel(null, "Account disabled!"), HttpStatus.valueOf(409));
        }

        catch (BadCredentialsException badCredentialsException) {
            return new ResponseEntity<>(new AuthResponseModel(null, "Invalid Credentials/No such account found!"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/seller")
    @PreAuthorize("hasAuthority('SELLER')")
    public void isAuthedForSeller() {}

    @PostMapping("/buyer")
    @PreAuthorize("hasAuthority('BUYER')")
    public void isAuthedForBuyer() {}

}
