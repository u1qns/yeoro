package com.yeoro.domain.notice.model.mapper;

import com.yeoro.domain.notice.model.dto.NoticeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface NoticeMapper {
    List<NoticeDto> getNotices();
    Optional<NoticeDto> getNotice(Long id);
    int addNotice(NoticeDto noticeDto);
    int setNotice(NoticeDto noticeDto);
    int increaseHits(Long id);
    int deleteNotice(Long id);
}
