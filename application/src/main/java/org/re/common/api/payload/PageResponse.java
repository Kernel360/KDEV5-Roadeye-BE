package org.re.common.api.payload;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter
public class PageResponse<T> extends SuccessResponse<List<T>> {
    private final PageInfo pageInfo;

    private PageResponse(List<T> data, PageInfo pageInfo) {
        super(data);
        this.pageInfo = pageInfo;
    }

    public static <T, R> PageResponse<R> of(Page<T> page, Function<T, R> mapper) {
        List<R> mappedContent = page.getContent().stream()
            .map(mapper)
            .toList();
        return new PageResponse<>(mappedContent, PageInfo.of(page));
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
