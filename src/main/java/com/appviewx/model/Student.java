package com.appviewx.model;

import com.appviewx.annotations.IntegerContains;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class Student implements Serializable {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @IntegerContains(values = {2,5,10}, message = "Invalid Integer provided. Should be one of {values}")
    private int age;

    public Student() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
