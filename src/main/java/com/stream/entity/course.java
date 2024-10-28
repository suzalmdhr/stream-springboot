package com.stream.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "yt_courses")
public class course {


    @Id
    private String id;

    private String title;


    @OneToMany(mappedBy = "course")
    private List<video> list=new ArrayList<>();


}
