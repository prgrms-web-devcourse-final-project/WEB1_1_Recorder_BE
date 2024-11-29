package com.revup.question.controller;

import com.revup.global.dto.ApiResponse;
import com.revup.page.Page;
import com.revup.question.dto.QuestionUpdateRequest;
import com.revup.question.dto.request.QuestionAcceptAnswerRequest;
import com.revup.question.dto.request.QuestionCreateRequest;
import com.revup.question.dto.request.QuestionPageRequest;
import com.revup.question.dto.response.QuestionBriefResponse;
import com.revup.question.dto.response.QuestionDetailsResponse;
import com.revup.question.dto.response.QuestionIdResponse;
import com.revup.question.usecase.*;
import com.revup.user.entity.User;
import com.revup.user.util.UserUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.revup.global.dto.ApiResponse.success;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
public class QuestionController {
    private final CreateQuestionUseCase createQuestionUseCase;
    private final UpdateQuestionUseCase updateQuestionUseCase;
    private final DeleteQuestionUseCase deleteQuestionUseCase;
    private final GetQuestionListUseCase getQuestionListUseCase;
    private final GetQuestionDetailsUseCase getQuestionDetailsUseCase;
    private final GetMyQuestionsUseCase getMyQuestionsUseCase;
    private final GetSpecificQuestionsUseCase getSpecificQuestionsUseCase;
    private final UserUtil userUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<QuestionIdResponse>> create(@Valid @RequestBody QuestionCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(success(createQuestionUseCase.execute(request)));

    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Page<QuestionBriefResponse>>> getQuestionList(@Valid QuestionPageRequest request) {
        return ResponseEntity.ok().body(success(getQuestionListUseCase.execute(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<QuestionDetailsResponse>> getQuestionDetails(@RequestParam Long id,
                                                                                   @CookieValue(value = "viewed_questions",defaultValue = "") String viewedQuestions,
                                                                                   HttpServletResponse response
    ) {
        // 쿠키에서 해당 ID 조회 여부 확인
        boolean alreadyViewed = viewedQuestions.contains(id.toString());

        QuestionDetailsResponse questionDetailsResponse = getQuestionDetailsUseCase.execute(id, alreadyViewed);

        // 조회수가 증가된 경우에만 쿠키 업데이트
        if (!alreadyViewed) {
            String updatedViewedQuestions = viewedQuestions.isEmpty()
                    ? id.toString()
                    : viewedQuestions + "," + id;

            Cookie cookie = new Cookie("viewed_questions", updatedViewedQuestions);
            cookie.setHttpOnly(true); // 클라이언트 스크립트에서 접근 불가
            cookie.setPath("/api/v1/question");
            cookie.setMaxAge(24 * 60 * 60); // 24시간 유지
            response.addCookie(cookie);
        }

        return ResponseEntity.ok().body(success(questionDetailsResponse));
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<QuestionBriefResponse>>> getPopularQuestions(@RequestParam int size,
                                                                                        @RequestParam int days
    ) {
        return ResponseEntity.ok().body(success(getSpecificQuestionsUseCase.getPopulars(size, days)));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<QuestionBriefResponse>>> getRecentQuestions(@RequestParam int size){
        return ResponseEntity.ok().body(success(getSpecificQuestionsUseCase.getRecent(size)));
    }

    @GetMapping("/stack")
    public ResponseEntity<ApiResponse<List<QuestionBriefResponse>>> getStackQuestions(@RequestParam int size,
                                                                                      @RequestParam String stack){
        return ResponseEntity.ok().body(success(getSpecificQuestionsUseCase.getByStack(size, stack)));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<QuestionBriefResponse>>> getMyQuestionList(@RequestParam(required = false) Long lastId,
                                                                                      @RequestParam int size
    ) {
        return ResponseEntity.ok().body(success(getMyQuestionsUseCase.execute(lastId, size)));
    }

    @PatchMapping("/accept")
    public ResponseEntity<ApiResponse<QuestionIdResponse>> acceptAnswer(@RequestBody QuestionAcceptAnswerRequest request){
        User currentUser = userUtil.getCurrentUser();
        return ResponseEntity.ok().body(success(updateQuestionUseCase.acceptAnswer(request, currentUser)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<QuestionIdResponse>> update(@Valid @RequestBody QuestionUpdateRequest request){
        return ResponseEntity.ok().body(success((updateQuestionUseCase.updateQuestion(request))));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        deleteQuestionUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

}
