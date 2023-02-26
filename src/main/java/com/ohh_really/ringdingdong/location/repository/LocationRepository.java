package com.ohh_really.ringdingdong.location.repository;

import com.ohh_really.ringdingdong.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
