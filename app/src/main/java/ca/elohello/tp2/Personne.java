package ca.elohello.tp2;

import java.io.Serializable;

public class Personne implements Serializable {
    protected String name;
    protected String lastName;
    protected String email;
    protected int age;
    protected String mdp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public Personne(String name, String lastName, String email, int age, String mdp) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.mdp = mdp;
    }

    public Personne(String name, String lastName, String email, int age) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
    }

}
