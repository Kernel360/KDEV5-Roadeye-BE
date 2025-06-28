package org.re.company.repository;

import org.re.company.domain.CompanyQuote;
import org.re.company.domain.CompanyQuoteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyQuoteRepository extends JpaRepository<CompanyQuote, Long> {
    Page<CompanyQuote> findAllByQuoteStatus(CompanyQuoteStatus quoteStatus, Pageable pageable);
}
