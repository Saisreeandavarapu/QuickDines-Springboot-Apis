package com.HRMS.QuickDines.Sales.repo;

import com.HRMS.QuickDines.Sales.Entity.RestaurantStatus;
import com.HRMS.QuickDines.Sales.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantsRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByStatus(String active);
    long countByStatus(String status);
    List<Restaurant> findByStatus(
            RestaurantStatus status);


}
