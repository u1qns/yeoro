package com.yeoro.domain.notice.controller;

import com.yeoro.domain.notice.model.dto.NoticeDto;
import com.yeoro.domain.notice.model.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@Slf4j
@Tag(name = "공지사항 API", description = "공지사항 관련 API")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 모두 조회")
    @GetMapping
    public ResponseEntity<List<NoticeDto>> getAllNotice() {
        List<NoticeDto> notices = noticeService.findAllNotice();
        return ResponseEntity.ok(notices);
    }

    @Operation(summary = "공지사항 조회")
    @Parameter(name = "id", description = "공지사항 아이디", example = "2")
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDto> getNoticeById(@PathVariable Long id) {
        NoticeDto notice = noticeService.findNotice(id);
        return ResponseEntity.ok(notice);
    }

    @Operation(summary = "공지사항 추가")
    @PostMapping
    public ResponseEntity<Void> addNotice(@Valid @RequestBody NoticeDto noticeDto) {
        noticeService.createNotice(noticeDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "공지사항 수정")
    @Parameter(name = "id", description = "공지사항 아이디", example = "2")
    @PostMapping("/{id}")
    public ResponseEntity<Void> updateNotice(@PathVariable Long id, @Valid @RequestBody NoticeDto noticeDto) {
        noticeService.modifyNotice(noticeDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "공지사항 삭제")
    @Parameter(name = "id", description = "공지사항 아이디", example = "2")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.removeNotice(id);
        return ResponseEntity.noContent().build();
    }
}
