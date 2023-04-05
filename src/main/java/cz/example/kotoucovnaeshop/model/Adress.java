package cz.example.kotoucovnaeshop.model;

public class Adress {
    private long id;
    private String street;
    private String buildingNumber;
    private String city;
    private String postalCode;
    private String country;

    public Adress() {
    }

    public Adress(long id, String street, String buildingNumber, String city, String postalCode, String country) {
        this.id = id;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
