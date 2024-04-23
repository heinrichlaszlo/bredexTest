package com.example.bredexTest.ad.repository;

import com.example.bredexTest.ad.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository for Ad entities
 */
@Repository
public interface AdRepository extends JpaRepository<Ad, Long>{
}
