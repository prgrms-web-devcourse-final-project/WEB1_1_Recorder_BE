package com.revup.feedback.controller;

import com.revup.feedback.request.MentorCreateRequest;
import com.revup.feedback.request.MentorPageRequest;
import com.revup.feedback.service.response.MentorResponse;
import com.revup.feedback.usecase.CreateMentorUseCase;
import com.revup.feedback.usecase.GetMentorListUseCase;
import com.revup.global.dto.ApiResponse;
import com.revup.page.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mentor")
@RequiredArgsConstructor
public class MentorController {

    private final CreateMentorUseCase createMentorUseCase;
    private final GetMentorListUseCase getMentorListUseCase;

    /**
     * 피드백 멘토 지원
     * @param mentorCreateRequest 멘토 지원에 필요한 내용들
     * @return 생성된 멘토 id
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createMentor(@RequestBody MentorCreateRequest mentorCreateRequest) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        createMentorUseCase.execute(mentorCreateRequest)
                )
        );
    }

    /**
     * 멘토 목록 페이지 조회
     * @param mentorPageRequest 조회할 페이지 번호
     * @return 해당 페이지에 나타날 멘토 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<MentorResponse>>> getMentorList(@RequestBody MentorPageRequest mentorPageRequest) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        getMentorListUseCase.execute(mentorPageRequest)
                )
        );
    }

}