package com.example.UberReviewService.repositories;

import com.example.UberReviewService.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface DriverRepository extends JpaRepository<Driver, Long> {

//    Optional<Driver> findByIdAndLicenseNumber(Long id, String licenseNumber);

//    @Query(nativeQuery = true, value = "Select * from Driver where id = :id and license_number = :license")
    Optional<Driver> findByIdAndLicenseNumber(Long id, String license);

    List<Driver> findAllByIdIn(List<Long> driverIds);
}
