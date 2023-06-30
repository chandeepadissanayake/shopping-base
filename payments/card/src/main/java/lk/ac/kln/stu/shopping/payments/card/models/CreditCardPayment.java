package lk.ac.kln.stu.shopping.payments.card.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreditCardPayment {

    private String cardNumber;
    private int cvcNumber;
    private String cardHolderName;
    private float amount;

    public CreditCardPayment(String cardNumber, int cvcNumber, float amount) {
        this.cardNumber = cardNumber;
        this.cvcNumber = cvcNumber;
        this.amount = amount;
    }

    public CreditCardPayment(String cardNumber, int cvcNumber, String cardHolderName, float amount) {
        this.cardNumber = cardNumber;
        this.cvcNumber = cvcNumber;
        this.cardHolderName = cardHolderName;
        this.amount = amount;
    }

}
