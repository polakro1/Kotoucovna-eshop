package cz.example.kotoucovnaeshop.model;

public class Category {
    private long id;
    private String name;
    private Long superCategory;

    public Category() {}

    public Category(Long id, String name, Long superCategory) {
        this.id = id;
        this.name = name;
        this.superCategory = superCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSuperCategory() {
        return superCategory;
    }

    public void setSuperCategory(Long superCategory) {
        this.superCategory = superCategory;
    }
}
