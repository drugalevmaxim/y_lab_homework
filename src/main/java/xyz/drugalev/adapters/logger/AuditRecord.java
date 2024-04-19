package xyz.drugalev.adapters.logger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.drugalev.domain.entity.User;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class AuditRecord {
    private final User user;
    private final LocalDateTime timestamp;
    private final String action;

    @Override
    public String toString() {
        return "User " + user + " done '" + action + " at " + timestamp;
    }
}
