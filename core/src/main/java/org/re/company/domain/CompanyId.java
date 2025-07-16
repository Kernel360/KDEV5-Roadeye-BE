package org.re.company.domain;

import java.io.Serializable;

public record CompanyId(
    long value
) implements Serializable {
}
