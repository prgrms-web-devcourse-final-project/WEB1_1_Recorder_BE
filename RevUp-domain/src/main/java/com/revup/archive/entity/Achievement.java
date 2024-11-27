package com.revup.archive.entity;

import com.revup.achieve.AchieveType;
import com.revup.common.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "achievements",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"type", "standard"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Achievement extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 450)
    private String description;

    @Enumerated(EnumType.STRING)
    private AchieveType type;

    private Integer standard;

    @Builder
    private Achievement(
            String description,
            AchieveType type,
            Integer standard
    ) {
        this.description = description;
        this.type = type;
        this.standard = standard;
    }
}
