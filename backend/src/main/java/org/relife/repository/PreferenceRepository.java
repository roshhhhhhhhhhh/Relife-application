package org.relife.repository;

import org.relife.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Integer> {

    Optional<Preference> findByUserId(Integer userId);
}
