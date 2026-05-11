package org.relife.repository;

import org.relife.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {

    List<Resource> findByUserId(Integer userId);
    List<Resource> findByCategory(String category);
    List<Resource> findByCity(String city);
    List<Resource> findByResourceType(String resourceType);
    List<Resource> findByStatus(String status);
}
