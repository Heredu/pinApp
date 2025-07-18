package com.pinApp.customerManagement.repository;

import com.pinApp.customerManagement.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT c.age FROM Client c")
    List<Integer> findAllAges();
}
