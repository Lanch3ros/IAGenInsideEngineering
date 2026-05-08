package eci.edu.byteProgramming.ejercicio.paper.videoclub;

public class Customer {
    private final String name;
    private final MembershipType membership;

    public Customer(String name, MembershipType membership) {
        this.name = name;
        this.membership = membership;
    }

    public String getName() { return name; }
    public MembershipType getMembership() { return membership; }
}
