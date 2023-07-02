package lk.ac.kln.stu.shopping.auth.authentication;

import io.jsonwebtoken.SignatureException;
import lk.ac.kln.stu.shopping.auth.models.User;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Claims; import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TokenManager {

    public static final long TOKEN_VALIDITY = 10 * 60 * 60 * 24; // Valid for a day

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(this.jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) throws SignatureException {
        final Claims claims = this.parseClaims(token);
        return claims.getSubject();
    }

    public Boolean validateJwtToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        Claims claims = this.parseClaims(token);
        boolean isTokenExpired = claims.getExpiration().before(new Date());
        return (username.equals(userDetails.getUsername()) && !isTokenExpired);
    }

    public String generateJwtToken(User user) {
        return Jwts.builder().setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

}
