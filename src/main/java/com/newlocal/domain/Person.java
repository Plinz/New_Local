package com.newlocal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Stock> stocks = new HashSet<>();
    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Grade> grades = new HashSet<>();
    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PurchasePending> purchasePendings = new HashSet<>();
    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PurchaseDone> purchaseDones = new HashSet<>();
    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Location> locations = new HashSet<>();
    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Holding> holdings = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Person user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Stock> getStocks() {
        return stocks;
    }

    public Person stocks(Set<Stock> stocks) {
        this.stocks = stocks;
        return this;
    }

    public Person addStock(Stock stock) {
        this.stocks.add(stock);
        stock.setPerson(this);
        return this;
    }

    public Person removeStock(Stock stock) {
        this.stocks.remove(stock);
        stock.setPerson(null);
        return this;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public Person grades(Set<Grade> grades) {
        this.grades = grades;
        return this;
    }

    public Person addGrade(Grade grade) {
        this.grades.add(grade);
        grade.setPerson(this);
        return this;
    }

    public Person removeGrade(Grade grade) {
        this.grades.remove(grade);
        grade.setPerson(null);
        return this;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }

    public Set<PurchasePending> getPurchasePendings() {
        return purchasePendings;
    }

    public Person purchasePendings(Set<PurchasePending> purchasePendings) {
        this.purchasePendings = purchasePendings;
        return this;
    }

    public Person addPurchasePending(PurchasePending purchasePending) {
        this.purchasePendings.add(purchasePending);
        purchasePending.setPerson(this);
        return this;
    }

    public Person removePurchasePending(PurchasePending purchasePending) {
        this.purchasePendings.remove(purchasePending);
        purchasePending.setPerson(null);
        return this;
    }

    public void setPurchasePendings(Set<PurchasePending> purchasePendings) {
        this.purchasePendings = purchasePendings;
    }

    public Set<PurchaseDone> getPurchaseDones() {
        return purchaseDones;
    }

    public Person purchaseDones(Set<PurchaseDone> purchaseDones) {
        this.purchaseDones = purchaseDones;
        return this;
    }

    public Person addPurchaseDone(PurchaseDone purchaseDone) {
        this.purchaseDones.add(purchaseDone);
        purchaseDone.setPerson(this);
        return this;
    }

    public Person removePurchaseDone(PurchaseDone purchaseDone) {
        this.purchaseDones.remove(purchaseDone);
        purchaseDone.setPerson(null);
        return this;
    }

    public void setPurchaseDones(Set<PurchaseDone> purchaseDones) {
        this.purchaseDones = purchaseDones;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public Person locations(Set<Location> locations) {
        this.locations = locations;
        return this;
    }

    public Person addLocation(Location location) {
        this.locations.add(location);
        location.setPerson(this);
        return this;
    }

    public Person removeLocation(Location location) {
        this.locations.remove(location);
        location.setPerson(null);
        return this;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<Holding> getHoldings() {
        return holdings;
    }

    public Person holdings(Set<Holding> holdings) {
        this.holdings = holdings;
        return this;
    }

    public Person addHolding(Holding holding) {
        this.holdings.add(holding);
        holding.setPerson(this);
        return this;
    }

    public Person removeHolding(Holding holding) {
        this.holdings.remove(holding);
        holding.setPerson(null);
        return this;
    }

    public void setHoldings(Set<Holding> holdings) {
        this.holdings = holdings;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        if (person.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), person.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            "}";
    }
}
