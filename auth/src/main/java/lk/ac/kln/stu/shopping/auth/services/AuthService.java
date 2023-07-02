package lk.ac.kln.stu.shopping.auth.services;

import lk.ac.kln.stu.shopping.auth.jwtutils.AuthUserDetailsService;
import lk.ac.kln.stu.shopping.auth.jwtutils.TokenManager;
import lk.ac.kln.stu.shopping.auth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthUserDetailsService authUserDetailsService;
    private final TokenManager tokenManager;

    @Autowired
    public AuthService(AuthUserDetailsService authUserDetailsService, TokenManager tokenManager) {
        this.authUserDetailsService = authUserDetailsService;
        this.tokenManager = tokenManager;
    }

    public String setLogin(String email, String password) throws DisabledException, BadCredentialsException {
        User userDetails = this.authUserDetailsService.loadUserByUsername(email);
        if (!userDetails.getPassword().equals(password)) {
            throw new BadCredentialsException("Invalid Password!");
        }
        return this.tokenManager.generateJwtToken(userDetails);
    }
}
