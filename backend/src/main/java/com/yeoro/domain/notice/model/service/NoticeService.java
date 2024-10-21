package com.yeoro.domain.notice.model.service;

import com.yeoro.domain.notice.model.dto.NoticeDto;

import java.util.List;

public interface NoticeService {
    void createNotice(NoticeDto noticeDto);
    NoticeDto findNotice(Long id);
    List<NoticeDto> findAllNotice();
    void modifyNotice(NoticeDto noticeDto);
    void removeNotice(Long id);
}
