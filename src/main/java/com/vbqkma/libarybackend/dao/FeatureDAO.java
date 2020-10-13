package com.vbqkma.libarybackend.dao;

import com.vbqkma.libarybackend.model.Feature;
import com.vbqkma.libarybackend.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeatureDAO extends JpaRepository<Feature, Long> {
//    public List<Feature> findAll();
}
