package com.santeut.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "likes")
public class LikeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private int id;

    @NotNull
    @Column(name = "like_reference_type", length = 255)
    private Character likeReferenceType;

    @NotNull
    @Column(name = "like_reference_id")
    private int likeReferenceId;

    @NotNull
    @Column(name = "user_id")
    private int userId;
}
