package com.yeoro.domain.notice.entity;

import com.yeoro.entityDefault.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends BaseTime {
    private Integer id;
    private String title;
    private String context;
    private Integer hits;
    private Boolean isImportant;
}
