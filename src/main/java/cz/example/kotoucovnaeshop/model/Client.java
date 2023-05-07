package cz.example.kotoucovnaeshop.model;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.core.parameters.P;

public class Client extends Account {
    @Pattern(regexp = "^[+]?[()/0-9. -]{9,}$")
    private String tel;
    @Valid
    private Adress adress;

    public Client() {
    }

    public Client(long id, String email, String password, String username, String name, String surname, String tel, Adress adress) {
        super(id, email, password, username, name, surname);
        this.tel = tel;
        this.adress = adress;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Adress getAdress() {
        return adress;
    }

    public void setAdress(Adress adress) {
        this.adress = adress;
    }

}
