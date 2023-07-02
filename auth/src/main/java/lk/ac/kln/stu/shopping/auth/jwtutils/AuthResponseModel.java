package lk.ac.kln.stu.shopping.auth.jwtutils;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class AuthResponseModel implements Serializable {

    private final String token;
    private final String errorMessage;

    public AuthResponseModel(String token, String errorMessage) {
        this.token = token;
        this.errorMessage = errorMessage;
    }

}
