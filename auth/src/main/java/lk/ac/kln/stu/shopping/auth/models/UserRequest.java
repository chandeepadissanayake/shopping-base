package lk.ac.kln.stu.shopping.auth.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class UserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobile;
    private List<String> roleNames;

    public UserRequest(String firstName, String lastName, String email, String password, String mobile, List<String> roleNames) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.roleNames = roleNames;
    }
}
