package com.github.astappiev.jdbcperf.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.github.javafaker.Faker;

@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = -1536293196241768787L;
    private static final Random random = new Random();

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS user (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(255) NOT NULL, last_name VARCHAR(255) NOT NULL, address VARCHAR(1024), zip_code INT, city VARCHAR(255), birthday DATE, created_at DATETIME NOT NULL);";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String address;
    @Column(name = "zip_code")
    private Integer zipCode;
    private String city;
    private Date birthday;
    @Column(name = "created_at")
    private Date createdAt;

    public static User fake() {
        Faker faker = new Faker();
        User user = new User();
        user.setFirstName(faker.address().firstName());
        user.setLastName(faker.address().lastName());
        if (random.nextBoolean()) {
            user.setAddress(faker.address().streetAddress());
            user.setZipCode(Integer.valueOf(faker.address().zipCode().replace("-", "")));
            user.setCity(faker.address().city());
        }
        if (random.nextBoolean()) {
            try {
                user.setBirthday(new SimpleDateFormat("MMMM d, yyyy").parse(faker.backToTheFuture().date()));
            } catch (ParseException e) {
                // nothing
            }
        }
        user.setCreatedAt(new Date());
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(final Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(final Date birthday) {
        this.birthday = birthday;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final User user = (User) o;

        return new EqualsBuilder().append(id, user.id).append(firstName, user.firstName).append(lastName, user.lastName).append(address, user.address).append(zipCode, user.zipCode).append(city, user.city).append(birthday, user.birthday).append(createdAt, user.createdAt).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(firstName).append(lastName).append(address).append(zipCode).append(city).append(birthday).append(createdAt).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("firstName", firstName)
            .append("lastName", lastName)
            .append("address", address)
            .append("zipCode", zipCode)
            .append("city", city)
            .append("birthday", birthday)
            .append("createdAt", createdAt)
            .toString();
    }
}
