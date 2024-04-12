package xyz.drugalev.adapters.logger;

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
    private final List<String> records = new ArrayList<>();

    private Audit() {
    }

    /**
     * Get audit instance.
     *
     * @return audit instance.
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
     * @param action action to log.
     */
    public void log(String action) {
        LocalDateTime timestamp = LocalDateTime.now();
        String record = timestamp + " : " + action;
        records.add(record);
    }

    /**
     * Get actions log from audit.
     */
    public List<String> getAuditRecords() {
        return records;
    }
}
