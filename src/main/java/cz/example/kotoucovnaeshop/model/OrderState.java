package cz.example.kotoucovnaeshop.model;

public class OrderState {
    private long id;
    private String Name;

    public long getId() {
        return id;
    }

    public OrderState setId(long id) {
        this.id = id;

        return this;
    }

    public String getName() {
        return Name;
    }

    public OrderState setName(String name) {
        this.Name = name;

        return this;
    }
}
