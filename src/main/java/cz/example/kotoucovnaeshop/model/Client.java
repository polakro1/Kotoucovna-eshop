package cz.example.kotoucovnaeshop.model;

public class Client extends Account {
    private String tel;
    private Adress adress;

    private Cart cart;

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
