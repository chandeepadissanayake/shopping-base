package lk.ac.kln.stu.shopping.communication.sms.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SMSMessage {

    private String to;
    private String message;

    public SMSMessage(String to, String message) {
        this.to = to;
        this.message = message;
    }

}
