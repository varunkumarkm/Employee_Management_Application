package com.employee.management.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.employee.management.app.Entities.PastExperience;


@Repository
public interface PastExperienceRepository extends JpaRepository<PastExperience, Integer> {

}
