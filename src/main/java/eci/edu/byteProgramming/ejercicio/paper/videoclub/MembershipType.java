package eci.edu.byteProgramming.ejercicio.paper.videoclub;

public enum MembershipType {
    BASIC("Basica"),
    PREMIUM("Premium");

    private final String displayName;

    MembershipType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }
}
