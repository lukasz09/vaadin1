package com.example.vaadindemo.domain;

import java.util.UUID;

public class Movie {
	
    private UUID id;

    private String name;

    private String genre;

    private String production;

    public Movie(String name, String genre, String production) {
        super();
        this.name = name;
        this.genre = genre;
        this.production = production;
    }

    public Movie() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getProduction() {
        return this.production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    @Override
    public String toString() {
        return "Movie [name=" + this.name + ", genre=" + this.genre
                            + ", production=" + this.production + "]";
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
