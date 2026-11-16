package com.classz.flightbook.parent.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public Parent getParent(){
        return parent;
    }

    public void setParent(Parent parent){
        this.parent=parent;
    }


}
