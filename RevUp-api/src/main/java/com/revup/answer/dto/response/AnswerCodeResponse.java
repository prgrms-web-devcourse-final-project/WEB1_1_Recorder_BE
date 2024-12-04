package com.revup.answer.dto.response;

public record AnswerCodeResponse(
        String name,
        String content
) {
    public static AnswerCodeResponse of(AnswerCode answerCode) {
        return new AnswerCodeResponse(
                answerCode.getName(),
                answerCode.getContent()
        );
    }
}
