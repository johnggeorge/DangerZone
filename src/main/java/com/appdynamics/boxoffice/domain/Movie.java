package com.appdynamics.boxoffice.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "rating")
    private String rating;

    @Column(name = "genre")
    private String genre;

    @ManyToMany(mappedBy = "movies")
    @JsonIgnore
    private Set<Theater> theaters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Movie name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public Movie rating(String rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public Movie genre(String genre) {
        this.genre = genre;
        return this;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Set<Theater> getTheaters() {
        return theaters;
    }

    public Movie theaters(Set<Theater> theaters) {
        this.theaters = theaters;
        return this;
    }

    public Movie addTheater(Theater theater) {
        this.theaters.add(theater);
        theater.getMovies().add(this);
        return this;
    }

    public Movie removeTheater(Theater theater) {
        this.theaters.remove(theater);
        theater.getMovies().remove(this);
        return this;
    }

    public void setTheaters(Set<Theater> theaters) {
        this.theaters = theaters;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movie)) {
            return false;
        }
        return id != null && id.equals(((Movie) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Movie{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rating='" + getRating() + "'" +
            ", genre='" + getGenre() + "'" +
            "}";
    }
}
