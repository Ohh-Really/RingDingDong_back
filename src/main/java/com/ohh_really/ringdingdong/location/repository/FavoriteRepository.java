package com.ohh_really.ringdingdong.location.repository;

import com.ohh_really.ringdingdong.location.entity.Favorite;
import com.ohh_really.ringdingdong.location.entity.Location;
import com.ohh_really.ringdingdong.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserAndIsCurrentTrue(User user);
    ArrayList<Favorite> findByUser(User user);

    ArrayList<Favorite> findByLocation_Region(String region);

    ArrayList<Favorite> findByLocation_RegionAndLocation_Level1(String region, String level1);




}
