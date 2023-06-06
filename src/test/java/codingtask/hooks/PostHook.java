package codingtask.hooks;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class PostHook {
    @BeforeAll
    public static void setup() {
        // TEST DATA PREPARATION
    }

    @AfterAll
    public static void dispose() {
        // TEST DATA RESTORATION
    }

    protected static final String BASE_URL = "https://jsonplaceholder.typicode.com";
}
