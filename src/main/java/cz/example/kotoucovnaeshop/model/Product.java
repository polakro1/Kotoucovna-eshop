package cz.example.kotoucovnaeshop.model;


public class Product {
    private long id;
    private double price;
    private int quantity;
    private String name;
    private String descriptionShort;
    private String description;
    private Category category;

    private String imageUrl;

    public Product() {
    }

    public Product(long id, double price, int quantity, String name, String descriptionShort, String description, String imageUrl, Category category) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.name = name;
        this.descriptionShort = descriptionShort;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
