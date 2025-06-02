package org.re.hq.common.dto;

import java.util.List;

public class ListResponse<T> extends SuccessResponse<List<T>> {
    public ListResponse(List<T> data) {
        super(data);
    }
}
