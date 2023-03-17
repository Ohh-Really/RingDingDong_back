package com.ohh_really.ringdingdong.location.entity;

import com.ohh_really.ringdingdong.user.entity.User;
import lombok.*;
import org.checkerframework.checker.interning.qual.CompareToMethod;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Favorite implements Comparable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Location location;
    @ManyToOne
    private User user;
    Boolean isCurrent;

    @Override
    public int compareTo(Object o) {
        // isCurrent가 true인 것이 더 높은 우선순위를 가지도록 함
        if (this.isCurrent && !((Favorite) o).isCurrent) {
            return -1;
        } else if (!this.isCurrent && ((Favorite) o).isCurrent) {
            return 1;
        } else {
            return 0;
        }
    }
}
