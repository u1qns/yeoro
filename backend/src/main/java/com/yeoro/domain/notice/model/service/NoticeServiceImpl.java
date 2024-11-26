package com.yeoro.domain.notice.model.service;

import com.yeoro.domain.notice.model.dto.NoticeDto;
import com.yeoro.domain.notice.model.mapper.NoticeMapper;
import com.yeoro.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yeoro.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    @Override
    public void createNotice(NoticeDto noticeDto) {
        int result = noticeMapper.addNotice(noticeDto);
        if (result != 1) {
            throw new CustomException(NOTICE_CREATION_FAILED);
        }
    }

    @Override
    public NoticeDto findNotice(Long id) {
        noticeMapper.increaseHits(id);
        return noticeMapper.getNotice(id)
                .orElseThrow(() -> new CustomException(NOTICE_NOT_FOUND));
    }

    @Override
    public List<NoticeDto> findAllNotice() {
        return noticeMapper.getNotices();
    }

    @Override
    public void modifyNotice(NoticeDto noticeDto) {
        int result = noticeMapper.setNotice(noticeDto);
        if (result != 1) {
            throw new CustomException(NOTICE_UPDATE_FAILED);
        }
    }

    @Override
    public void removeNotice(Long id) {
        int result = noticeMapper.deleteNotice(id);
        if (result != 1) {
            throw new CustomException(NOTICE_DELETION_FAILED);
        }
    }
}
