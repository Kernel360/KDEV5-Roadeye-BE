package org.re.hq.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> extends SuccessResponse<List<T>> {
    private final PageInfo pageInfo;

    private PageResponse(List<T> data, PageInfo pageInfo) {
        super(data);
        this.pageInfo = pageInfo;
    }

    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(page.getContent(), PageInfo.of(page));
    }

    public record PageInfo(
        int page,
        int size,
        long total
    ) {
        public static PageInfo of(Page<?> page) {
            return new PageInfo(page.getNumber(), page.getSize(), page.getTotalElements());
        }
    }
}
