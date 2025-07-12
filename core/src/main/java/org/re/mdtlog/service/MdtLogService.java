package org.re.mdtlog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.mdtlog.domain.MdtLog;
import org.re.mdtlog.domain.MdtLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MdtLogService {
    private final MdtLogRepository mdtLogRepository;

    @Transactional
    public void save(MdtLog mdtlog) {
        mdtLogRepository.save(mdtlog);
    }

    @Transactional
    public void save(List<MdtLog> logs) {
        mdtLogRepository.saveAll(logs);
    }
}
