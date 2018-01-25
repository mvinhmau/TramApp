package com.example.marievinhmau.projettram;



public class Ticket {
    private int id;
    private String phone_user;
    private int nb;

    public Ticket() {
    }

    public Ticket( String id_user, int nb) {

        this.phone_user = id_user;
        this.nb = nb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone_user() {
        return phone_user;
    }

    public void setPhone_user(String phone_user) {
        this.phone_user=phone_user;
    }

    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }
}
