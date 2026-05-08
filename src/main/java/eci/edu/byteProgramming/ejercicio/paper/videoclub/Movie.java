package eci.edu.byteProgramming.ejercicio.paper.videoclub;

public abstract class Movie {
    protected final String title;
    protected final double price;
    protected boolean available;

    protected Movie(String title, double price, boolean available) {
        this.title = title;
        this.price = price;
        this.available = available;
    }

    public abstract String getType();

    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("[%s] %s - $%.0f - %s",
                getType(), title, price, available ? "Disponible" : "No disponible");
    }
}
