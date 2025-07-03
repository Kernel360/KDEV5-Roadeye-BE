package org.re.test.api.payload;

import jakarta.validation.constraints.NotNull;

public record TodoItem(
    @NotNull String title
) {
}
