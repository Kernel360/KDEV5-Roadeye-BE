package org.re.common.api.payload;

import java.util.function.Function;

public class SingleItemResponse<T> extends SuccessResponse<T> {
    public SingleItemResponse(T data) {
        super(data);
    }

    public static <T> SingleItemResponse<T> of(T data) {
        return new SingleItemResponse<>(data);
    }

    public static <T, R> SingleItemResponse<R> of(T car, Function<T, R> mapper) {
        return new SingleItemResponse<>(mapper.apply(car));
    }
}
