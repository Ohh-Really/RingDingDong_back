package com.ohh_really.ringdingdong.location.entity;

import com.ohh_really.ringdingdong.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class Favorite {
    @Id
    private Long id;
    @ManyToOne
    private Location location;
    @ManyToOne
    private User user;
}
