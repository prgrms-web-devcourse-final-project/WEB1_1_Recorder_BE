package com.revup.question.repository;

import com.revup.common.SkillStack;
import com.revup.question.entity.Question;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long>, CustomQuestionRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select q from Question q where q.id = :id")
    Optional<Question> findByIdWithPessimisticLock(Long id);

    @Query(value = "SELECT stacks " +
            "FROM question_stacks " +
            "GROUP BY stacks " +
            "ORDER BY COUNT(stacks) DESC " +
            "LIMIT :size",
            nativeQuery = true)
    List<SkillStack> findPopularStacks(int size);

    @Query("select q from Question q join fetch q.user u where q.id = :questionId")
    Optional<Question> findByIdWithUser(Long questionId);

}
