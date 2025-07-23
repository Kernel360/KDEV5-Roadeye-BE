package org.re.common.api.payload;

import java.util.List;
import java.util.function.Function;

public class ListResponse<T> extends SuccessResponse<List<T>> {
    public ListResponse(List<T> data) {
        super(data);
    }

    public static <T, R> ListResponse<R> of(List<T> data, Function<T, R> mapper) {
        List<R> mappedData = data.stream()
            .map(mapper)
            .toList();
        return new ListResponse<>(mappedData);
    }
}
