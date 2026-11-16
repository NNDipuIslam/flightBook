package com.classz.flightbook.user.entity;
import jakarta.persistence.*;



@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName roleName;

    // Constructors
    public Role() {
    }

    public Role(RoleName roleName) {
        this.roleName = roleName;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }
}