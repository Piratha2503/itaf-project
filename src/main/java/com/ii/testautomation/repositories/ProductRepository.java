package com.ii.testautomation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ii.testautomation.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
