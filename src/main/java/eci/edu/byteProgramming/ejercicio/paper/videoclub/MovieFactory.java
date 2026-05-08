package eci.edu.byteProgramming.ejercicio.paper.videoclub;

public class MovieFactory {

    public static Movie createPhysical(String title, double price, boolean available) {
        return new PhysicalMovie(title, price, available);
    }

    public static Movie createDigital(String title, double price, boolean available) {
        return new DigitalMovie(title, price, available);
    }
}
