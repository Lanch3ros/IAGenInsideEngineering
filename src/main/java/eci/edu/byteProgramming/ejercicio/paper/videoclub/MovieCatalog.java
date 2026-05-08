package eci.edu.byteProgramming.ejercicio.paper.videoclub;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class MovieCatalog {

    private final Map<Integer, Movie> movies = new LinkedHashMap<>();

    public MovieCatalog() {
        movies.put(1, MovieFactory.createPhysical("Interestellar", 8000, true));
        movies.put(2, MovieFactory.createPhysical("El Padrino",    7000, false));
        movies.put(3, MovieFactory.createDigital("Inception",      5000, true));
        movies.put(4, MovieFactory.createDigital("Matrix",         6000, true));
    }

    public Movie getMovie(int number) {
        return movies.get(number);
    }

    public Map<Integer, Movie> getAll() {
        return Collections.unmodifiableMap(movies);
    }

    public void printCatalog() {
        System.out.println("=== Catalogo de Peliculas ===");
        movies.forEach((num, movie) ->
                System.out.printf("%d. %s%n", num, movie));
        System.out.println("=============================");
    }
}
