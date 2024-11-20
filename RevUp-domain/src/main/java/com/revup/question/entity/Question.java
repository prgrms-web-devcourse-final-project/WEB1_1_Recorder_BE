package com.revup.question.entity;

import com.revup.common.BaseTimeEntity;
import com.revup.common.BooleanStatus;
import com.revup.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Enumerated(EnumType.STRING)
    private QuestionState state;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int readCount;

    @Enumerated(EnumType.STRING)
    private BooleanStatus isAnonymous;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User user;

    @Builder
    private Question(
            String title,
            QuestionType type,
            QuestionState state,
            String content,
            BooleanStatus isAnonymous,
            User user) {
        this.title = title;
        this.type = type;
        this.state = state;
        this.content = content;
        this.readCount = 0;
        this.isAnonymous = isAnonymous;
        this.user = user;
    }

}