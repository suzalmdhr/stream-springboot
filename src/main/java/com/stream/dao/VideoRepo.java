package com.stream.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stream.entity.video;
import java.util.List;


@Repository
public interface VideoRepo extends JpaRepository<video,String> {


    Optional<video> findByTitle(String title);
}
