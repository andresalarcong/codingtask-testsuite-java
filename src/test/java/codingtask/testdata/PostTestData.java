package codingtask.testdata;

import java.util.Map;
import java.util.stream.Stream;

public class PostTestData {
    public static Stream<Map<String, Object>> DataTestForPostUnsuccessful() {
        return Stream.of(
                Map.of("title", "", "body", "", "userId", -1),
                Map.of("title", "", "body", "", "userId", 1.0),
                Map.of("title", "", "body", "", "userId", ""),
                Map.of("title", "", "body", 1, "userId", 1),
                Map.of("title", 1, "body", "", "userId", 1),
                Map.of("title", 1, "body", 1, "userId", 1)
        );
    }
}
