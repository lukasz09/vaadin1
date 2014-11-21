package com.example.vaadindemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.vaadindemo.domain.Movie;

public class MovieManager {
	
    private List<Movie> db = new ArrayList<Movie>();

    public void addMovie(Movie movie) {
        Movie p = new Movie(movie.getName(), movie.getGenre(), movie.getProduction());
        p.setId(UUID.randomUUID());
        db.add(p);
    }

    public List<Movie> findAll() {
        return db;
    }

    public void delete(Movie movie) {

        Movie toRemove = null;
        for (Movie m: db) {
            if (m.getId().compareTo(movie.getId()) == 0) {
                toRemove = m;
                break;
            }
        }
        db.remove(toRemove);
    }

    public void updateMovie(Movie movie) {
        for (Movie m: db) {
            if (m.getId().compareTo(movie.getId()) == 0) {
                m.setName(movie.getName());
                m.setGenre(movie.getGenre());
                m.setProduction(movie.getProduction());
                break;
            }
        }
    }
}
