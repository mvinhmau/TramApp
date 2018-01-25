package com.example.marievinhmau.projettram;


public class User {

    private int id;
    private String lastName;
    private String firstName;
    private String phone;
    private String mdp;

    //Constructeur

    public User( String lastName, String firstName, String phone, String mdp) {
        super();

        this.lastName = lastName;
        this.firstName = firstName;
        this.phone = phone;
        this.mdp = mdp;
    }

    public User(){}

    //Getters
    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhone() {
        return phone;
    }

    public String getMdp() {
        return mdp;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", phone='" + phone + '\'' +
                ", mdp='" + mdp + '\'' +
                '}';
    }
}
