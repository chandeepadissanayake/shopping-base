package lk.ac.kln.stu.shopping.auth.authentication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class AuthRequestModel implements Serializable {

    private String username;
    private String password;

    public AuthRequestModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
