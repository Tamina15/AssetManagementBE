package com.nashtech.rookies.assetmanagement.repository;

import com.nashtech.rookies.assetmanagement.entity.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Integer> {
}
