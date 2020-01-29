package com.appdynamics.boxoffice.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Showtime.
 */
@Entity
@Table(name = "showtime")
public class Showtime implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time")
    private ZonedDateTime time;

    @ManyToOne
    @JsonIgnoreProperties("showtimes")
    private Movie movie;

    @ManyToOne
    @JsonIgnoreProperties("showtimes")
    private Theater theater;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public Showtime time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public Movie getMovie() {
        return movie;
    }

    public Showtime movie(Movie movie) {
        this.movie = movie;
        return this;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Theater getTheater() {
        return theater;
    }

    public Showtime theater(Theater theater) {
        this.theater = theater;
        return this;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Showtime)) {
            return false;
        }
        return id != null && id.equals(((Showtime) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Showtime{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            "}";
    }
}
