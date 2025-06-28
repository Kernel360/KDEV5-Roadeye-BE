package org.re.company.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.re.admin.domain.PlatformAdmin;
import org.re.common.domain.BaseEntity;
import org.re.common.exception.DomainException;
import org.re.company.converter.CompanyQuoteStatusConverter;
import org.re.company.exception.CompanyQuoteDomainException;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyQuote extends BaseEntity {
    @Convert(converter = CompanyQuoteStatusConverter.class)
    @Column(nullable = false)
    private CompanyQuoteStatus quoteStatus;

    @Embedded
    private CompanyQuoteInfo quoteInfo;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private PlatformAdmin approver;

    @Column
    private LocalDateTime approvedAt;

    @Column
    private LocalDateTime rejectedAt;

    @Column
    private String rejectionReason;

    public CompanyQuote(CompanyQuoteInfo quoteInfo, LocalDateTime requestedAt) {
        this.quoteStatus = CompanyQuoteStatus.PENDING;
        this.quoteInfo = quoteInfo;
        this.requestedAt = requestedAt;
    }

    public void approve(PlatformAdmin approver) {
        if (this.quoteStatus != CompanyQuoteStatus.PENDING) {
            throw new DomainException(CompanyQuoteDomainException.QUOTE_STATE_IS_NOT_PENDING);
        }
        this.approver = approver;
        this.quoteStatus = CompanyQuoteStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
    }

    public boolean isApproved() {
        return this.quoteStatus == CompanyQuoteStatus.APPROVED;
    }

    public void reject(PlatformAdmin approver) {
        if (this.quoteStatus != CompanyQuoteStatus.PENDING) {
            throw new DomainException(CompanyQuoteDomainException.QUOTE_STATE_IS_NOT_PENDING);
        }
        this.approver = approver;
        this.quoteStatus = CompanyQuoteStatus.REJECTED;
        this.rejectedAt = LocalDateTime.now();
    }

    public boolean isRejected() {
        return this.quoteStatus == CompanyQuoteStatus.REJECTED;
    }

    public Company toCompany() {
        return new Company(
            quoteInfo.getCompanyName(),
            quoteInfo.getCompanyBusinessNumber(),
            quoteInfo.getCompanyEmail()
        );
    }
}
