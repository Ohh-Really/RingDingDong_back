package com.ohh_really.ringdingdong.location.repository;

import com.ohh_really.ringdingdong.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    ArrayList<Location> findByRegionAndRegion1AndRegion2(String region, String region1, String region2);

    ArrayList<Location> findByRegionAndRegion1(String region, String region1);



}
