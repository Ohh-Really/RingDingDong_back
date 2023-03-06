package com.ohh_really.ringdingdong.location.repository;

import com.ohh_really.ringdingdong.location.entity.Favorite;
import com.ohh_really.ringdingdong.location.entity.Location;
import com.ohh_really.ringdingdong.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserAndLocation(User user, Location location);

    Optional<Favorite> findByUserAndIsCurrentTrue(User user);




}
