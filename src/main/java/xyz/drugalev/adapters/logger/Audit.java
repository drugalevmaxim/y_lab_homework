package xyz.drugalev.adapters.logger;

import xyz.drugalev.domain.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton audit class.
 *
 * @author Drugalev Maxim
 */
public class Audit {
    private static Audit _instance;
    private final List<AuditRecord> records = new ArrayList<>();

    private Audit() {
    }

    /**
     * Get audit instance.
     *
     * @return audit instance
     */
    public static Audit getInstance() {
        if (_instance == null) {
            _instance = new Audit();
        }
        return _instance;
    }

    /**
     * Log action in audit.
     *
     * @param user   the user
     * @param action action to log
     */
    public void log(User user, String action) {
        records.add(new AuditRecord(user, LocalDateTime.now(), action));
    }

    /**
     * Get actions log from audit.
     *
     * @return the audit records
     */
    public List<AuditRecord> getAuditRecords() {
        return records;
    }
}
