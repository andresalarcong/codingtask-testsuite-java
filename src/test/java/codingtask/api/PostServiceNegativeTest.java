package codingtask.api;

import codingtask.domain.Post;
import codingtask.hooks.PostHook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostServiceNegativeTest extends PostHook {

    @DisplayName("Create a Post unsuccessful")
    @ParameterizedTest
    @MethodSource("codingtask.testdata.PostTestData#DataTestForPostUnsuccessful")
    public void testCreatePostUnsuccessful(Map<String, Object> testCase) {
        long startTime = System.currentTimeMillis();
        System.out.println("START : " + startTime + "ms");
        String title = String.valueOf(testCase.get("title"));
        String body = String.valueOf(testCase.get("body"));
        var userId = testCase.get("userId");

        given()
                .baseUri(BASE_URL)
                .body("{\"title\":" + title + ",\"body\":\"" + body + "\",\"userId\":" + userId + "}")
                .header("Content-type", "application/json; charset=UTF-8")
        .when()

                .post("/posts")
        .then()
            .assertThat()
                .statusCode(500);
    }

    @Test
    @DisplayName("Delete a Non-Existent Post")
    public void testDeletePostUnsuccessful() {
        long startTime = System.currentTimeMillis();
        System.out.println("START : " + startTime + "ms");
        given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json; charset=UTF-8")
        .when()

                .delete("/posts/{id}", Integer.MAX_VALUE)
        .then()
            .assertThat()
                .statusCode(404);
    }

    @Test
    @DisplayName("Update a Post with Invalid Attribute")
    public void testUpdatePostWithInvalidData() {
        long startTime = System.currentTimeMillis();
        System.out.println("START : " + startTime + "ms");
        int postId = 1; // ID of the post to update
        String invalidData = "{\"invalidField\":\"The Value\"}"; // Invalid data for update

        given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json; charset=UTF-8")
                .body(invalidData)
        .when()
                .put("/posts/{id}", postId)
        .then()
            .assertThat()
                .statusCode(400);
    }

    @Test
    @DisplayName("Update a post with invalid Body")
    public void testUpdatePostWithInvalidDataUsingPut() {
        long startTime = System.currentTimeMillis();
        System.out.println("START : " + startTime + "ms");
        given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json; charset=UTF-8")
                .body("{\"title\":\"New Title\",\"body\":123}")
        .when()
                .put("/posts/{id}", 1) // Replace {id} with the actual ID of the post
        .then()
            .assertThat()
                .statusCode(400);
    }

    @Test
    @DisplayName("Update a post with invalid data using PATCH")
    public void testUpdatePostWithInvalidDataUsingPatch() {
        long startTime = System.currentTimeMillis();
        System.out.println("START : " + startTime + "ms");
        given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json; charset=UTF-8")
                .body("{\"title\":\"New Title\",\"body\":123}")
        .when()
                .patch("/posts/{id}", 1) // Replace {id} with the actual ID of the post
        .then()
            .assertThat()
                .statusCode(400);
    }

    @Test
    @DisplayName("Get a non-existent post")
    public void testGetNonExistentPost() {
        long startTime = System.currentTimeMillis();
        System.out.println("START : " + startTime + "ms");
        given()
                .baseUri(BASE_URL)
        .when()
                .get("/posts/{id}", Integer.MAX_VALUE) // Replace number with an ID that doesn't exist in the API
        .then()
            .assertThat()
                .statusCode(404);
    }

}
