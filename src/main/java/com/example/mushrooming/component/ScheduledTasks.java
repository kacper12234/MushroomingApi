package com.example.mushrooming.component;

import com.example.mushrooming.repository.FindRepository;
import com.example.mushrooming.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Component
@AllArgsConstructor
public class ScheduledTasks {

    private final FindRepository findRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldData()
    {
    findRepository.deleteFindsByCreatedDateBefore(Instant.now().minus(2, ChronoUnit.WEEKS));
    refreshTokenRepository.deleteAllByCreatedDateBefore(Instant.now().minus(1, ChronoUnit.DAYS));
    }

}
