package com.classz.flightbook.parent.entity;

import com.classz.flightbook.user.entity.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parents")
public class Parent extends User {

    private String name;
    private String phoneNumber;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> childList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void addChild(Student childId) {
        childList.add(childId);
        childId.setParent(this);
    }

    public void removeChild(Student childId) {
        childList.remove(childId);
        childId.setParent(null);
    }

}
