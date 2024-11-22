package com.revup.feedback.controller.mapper;

import com.revup.common.BooleanStatus;
import com.revup.feedback.controller.request.FeedbackCreateRequest;
import com.revup.feedback.entity.Feedback;
import com.revup.feedback.entity.enums.FeedbackType;
import com.revup.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public Feedback toEntity(User student, User teacher, FeedbackCreateRequest feedbackCreateRequest) {
        BooleanStatus booleanStatus = BooleanStatus.FALSE;
        if (feedbackCreateRequest.isGithubLinkReveal()) booleanStatus = BooleanStatus.TRUE;

        return Feedback.builder()
                .student(student)
                .teacher(teacher)
                .type(FeedbackType.valueOf(feedbackCreateRequest.getType()))
                .title(feedbackCreateRequest.getTitle())
                .githubLink(feedbackCreateRequest.getGithubLink())
                .githubLinkReveal(booleanStatus)
                .description(feedbackCreateRequest.getDescription())
                .build();
    }

}