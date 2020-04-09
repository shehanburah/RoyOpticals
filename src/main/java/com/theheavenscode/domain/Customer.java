package com.theheavenscode.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import com.theheavenscode.domain.enumeration.Title;

/**
 * A Customer.
 */
@Document(collection = "customer")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "customer_a")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("title")
    private Title title;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("dob")
    private Instant dob;

    @Field("city")
    private String city;

    @Field("address")
    private String address;

    @Field("mobile")
    private String mobile;

    @Field("landline")
    private String landline;

    @Field("gender")
    private Boolean gender;

    @Field("registered")
    private Instant registered;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Title getTitle() {
        return title;
    }

    public Customer title(Title title) {
        this.title = title;
        return this;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public Customer firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Customer lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Instant getDob() {
        return dob;
    }

    public Customer dob(Instant dob) {
        this.dob = dob;
        return this;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
    }

    public String getCity() {
        return city;
    }

    public Customer city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public Customer address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public Customer mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLandline() {
        return landline;
    }

    public Customer landline(String landline) {
        this.landline = landline;
        return this;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public Boolean isGender() {
        return gender;
    }

    public Customer gender(Boolean gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Instant getRegistered() {
        return registered;
    }

    public Customer registered(Instant registered) {
        this.registered = registered;
        return this;
    }

    public void setRegistered(Instant registered) {
        this.registered = registered;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", title='" + getTitle()+ "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", dob='" + getDob() + "'" +
            ", city='" + getCity() + "'" +
            ", address='" + getAddress() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", landline='" + getLandline() + "'" +
            ", gender='" + isGender() + "'" +
            ", registered='" + getRegistered() + "'" +
            "}";
    }
}
