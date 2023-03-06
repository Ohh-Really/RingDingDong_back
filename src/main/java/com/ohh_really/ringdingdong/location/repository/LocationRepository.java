package com.ohh_really.ringdingdong.location.repository;

import com.ohh_really.ringdingdong.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    ArrayList<Location> findByRegionAndLevel1AndLevel2(String region, String level1, String level2);
    ArrayList<Location> findByRegionAndLevel1(String region, String level1);
    Optional<Location> findByLevel1AndLevel2AndLevel3AndLevel4(String level1, String level2, String level3, String level4);
}
