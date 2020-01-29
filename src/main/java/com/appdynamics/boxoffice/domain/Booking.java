package com.appdynamics.boxoffice.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "no_of_seats")
    private Integer noOfSeats;

    @ManyToOne
    @JsonIgnoreProperties("bookings")
    private Movie movie;

    @ManyToOne
    @JsonIgnoreProperties("bookings")
    private Showtime showtime;

    @ManyToOne
    @JsonIgnoreProperties("bookings")
    private Theater theater;

    @ManyToMany(mappedBy = "bookings")
    @JsonIgnore
    private Set<Customer> customers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNoOfSeats() {
        return noOfSeats;
    }

    public Booking noOfSeats(Integer noOfSeats) {
        this.noOfSeats = noOfSeats;
        return this;
    }

    public void setNoOfSeats(Integer noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public Movie getMovie() {
        return movie;
    }

    public Booking movie(Movie movie) {
        this.movie = movie;
        return this;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public Booking showtime(Showtime showtime) {
        this.showtime = showtime;
        return this;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public Theater getTheater() {
        return theater;
    }

    public Booking theater(Theater theater) {
        this.theater = theater;
        return this;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public Booking customers(Set<Customer> customers) {
        this.customers = customers;
        return this;
    }

    public Booking addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.getBookings().add(this);
        return this;
    }

    public Booking removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.getBookings().remove(this);
        return this;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Booking)) {
            return false;
        }
        return id != null && id.equals(((Booking) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Booking{" +
            "id=" + getId() +
            ", noOfSeats=" + getNoOfSeats() +
            "}";
    }
}
