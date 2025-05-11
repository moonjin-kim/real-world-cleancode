package com.moonjin.realworld.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static java.lang.Math.min;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    private static final int MAX_PAGE = 999;
    private static final int MAX_SIZE = 2000;
    @Builder.Default
    private Integer limit = 10;
    @Builder.Default
    private Integer offset = 1;

    public long getPageNum() {
        return (long) (limit - 1) * min(offset, MAX_SIZE);
    }
}
