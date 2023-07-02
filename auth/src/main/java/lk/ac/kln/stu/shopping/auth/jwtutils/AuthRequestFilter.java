package lk.ac.kln.stu.shopping.auth.jwtutils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ac.kln.stu.shopping.auth.models.Role;
import lk.ac.kln.stu.shopping.auth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthRequestFilter extends OncePerRequestFilter {

    private final AuthUserDetailsService authUserDetailsService;
    private final TokenManager tokenManager;

    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final String[] excludedPaths = {"/login"};

    @Autowired
    public AuthRequestFilter(AuthUserDetailsService authUserDetailsService, TokenManager tokenManager) {
        this.authUserDetailsService = authUserDetailsService;
        this.tokenManager = tokenManager;
    }

    private boolean shouldSkipFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        for (String excludedPath : excludedPaths) {
            if (pathMatcher.match(excludedPath, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        String jwtToken = null;

        if (!this.shouldSkipFilter(request)) {
            final String requestTokenHeader = request.getHeader("Authorization");

            // JWT Token is in the form "Bearer token". Remove Bearer word and get
            // only the Token
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);

                try {
                    username = this.tokenManager.getUsernameFromToken(jwtToken);
                }

                catch (SignatureException signatureException) {
                    System.out.println("Invalid Token passed!");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                catch (IllegalArgumentException e) {
                    System.out.println("Unable to get JWT Token");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                catch (ExpiredJwtException e) {
                    System.out.println("JWT Token has expired");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
            else {
                logger.warn("JWT Token does not begin with Bearer String");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Once we get the token validate it.
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = this.authUserDetailsService.loadUserByUsername(username);

                // if token is valid configure Spring Security to manually set
                // authentication
                if (this.tokenManager.validateJwtToken(jwtToken, user)) {
                    this.setAuthenticationContext(user, request);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
            else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(User user, HttpServletRequest request) {
        System.out.println(user.getAuthorities());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
