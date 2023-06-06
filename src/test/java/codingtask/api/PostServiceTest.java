package codingtask.api;

import codingtask.domain.Post;
import codingtask.hooks.PostHook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostServiceTest extends PostHook {

    @Test
    @DisplayName("Get a post list successful") // /posts
    public void testGetAPostListSuccessful() {
        long startTime = System.currentTimeMillis();
        System.out.println("START : " + startTime + "ms");
        given()
                .baseUri(BASE_URL)
        .when()
                .get("/posts")
        .then()
            .assertThat()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("$", is(not(empty()))) // root
                .body("$", is(instanceOf(List.class)));
    }

    @Test
    @DisplayName("Validate fields and type of values in a Post") // /posts/ {id}
    public void testValidateFieldsAndTypeOfValuesInAPost() {
        long startTime = System.currentTimeMillis();
        System.out.println("START : " + startTime + "ms");
        given()
                .baseUri(BASE_URL)
        .when()
                .get("/posts")
        .then()
            .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", Map.class)
                .stream()
                .findAny()
                .ifPresent(post -> {
                    given()
                            .baseUri(BASE_URL)
                            .pathParam("postId", post.get("id"))
                    .when()
                            .get("/posts/{postId}")
                    .then()
                        .assertThat()
                            .statusCode(200)
                            .body("userId", notNullValue())
                            .body("id", notNullValue())
                            .body("title", notNullValue())
                            .body("body", notNullValue())
                            .body("userId", instanceOf(Integer.class))
                            .body("id", instanceOf(Integer.class))
                            .body("title", instanceOf(String.class))
                            .body("body", instanceOf(String.class))
                            .body("id", equalTo(post.get("id")));
                });
    }

    @Test
    @DisplayName("Validate fields and type of values in a Comment")
    // /comments?postId= {id} and /posts/ {id} /comments are the same
    public void testValidateFieldsAndTypeOfValuesInAComment() {
        long startTime = System.currentTimeMillis();
        System.out.println("START : " + startTime + "ms");
        given()
                .baseUri(BASE_URL)
        .when()
                .get("/posts")
        .then()
            .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", Map.class)
                .stream()
                .findAny()
                .ifPresent(post -> {
                    given()
                            .baseUri(BASE_URL)
                            .pathParam("postId", post.get("id"))
                    .when()
                            .get("/posts/{postId}/comments")
                    .then()
                        .assertThat()
                            .statusCode(200)
                            .extract()
                            .jsonPath()
                            .getList(".", Map.class)
                            .stream()
                            .findAny()
                            .ifPresent(comment -> {
                                given()
                                        .baseUri(BASE_URL)
                                        .pathParam("commentId", comment.get("id"))
                                .when()
                                        .get("/comments/{commentId}")
                                .then()
                                    .assertThat()
                                        .statusCode(200)
                                        .body("postId", notNullValue())
                                        .body("id", notNullValue())
                                        .body("email", notNullValue())
                                        .body("body", notNullValue())
                                        .body("id", instanceOf(Integer.class))
                                        .body("postId", instanceOf(Integer.class))
                                        .body("name", instanceOf(String.class))
                                        .body("email", instanceOf(String.class))
                                        .body("body", instanceOf(String.class))
                                        .body("postId", equalTo(post.get("id")));
                            });
                });
    }

    @Test
    @DisplayName("Create a Post successful")
    public void testCreatePostSuccessful() {
        long startTime = System.currentTimeMillis();
        System.out.println("START : " + startTime + "ms");
        Post post = given()
                    .baseUri(BASE_URL)
                    .body("{\"title\":\"The Title\",\"body\":\"The Body\",\"userId\":50}")
                    .header("Content-type", "application/json; charset=UTF-8")
                .when()
                    .post("/posts")
                .then()
                    .assertThat()
                    .statusCode(201)
                    .extract()
                    .as(Post.class);

        // Reuse the post object to meaning the deserialization

        // Perform assertions on the Post object
        assertEquals(50, post.getUserId());
        assertEquals("The Title", post.getTitle());
        assertEquals("The Body", post.getBody());
        assertNotNull(post.getId());
    }


    @Test
    @DisplayName("Update a Post successful")
    public void testUpdatePostSuccessful() {
        long startTime = System.currentTimeMillis();
        System.out.println("START : " + startTime + "ms");
        given()
                .baseUri(BASE_URL)
                .body("{\"id\":1,\"title\":\"updated\",\"body\":\"updated\",\"userId\":2}")
                .header("Content-type", "application/json; charset=UTF-8")
        .when()
                .put("/posts/1")
        .then()
            .assertThat()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", equalTo(2))
                .body("title", equalTo("updated"))
                .body("body", equalTo("updated"));
    }

    @Test
    @DisplayName("Delete a Post successful")
    public void testDeletePostSuccessful() {
        long startTime = System.currentTimeMillis();
        System.out.println("START : " + startTime + "ms");
        given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json; charset=UTF-8")
        .when()
                .delete("/posts/{id}", 1)
        .then()
            .assertThat()
                .statusCode(200);

        given()
                .baseUri("https://jsonplaceholder.typicode.com")
        .when()
                .get("/posts/{id}", 1) // Replace {id} with the same ID used for deletion
        .then()
            .assertThat()
                .statusCode(404);
    }

    @Test
    @DisplayName("Patch a Post successful")
    public void testPatchPostSuccessful() {
        long startTime = System.currentTimeMillis();
        System.out.println("START : " + startTime + "ms");
        given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .header("Content-type", "application/json; charset=UTF-8")
                .body("{\"title\":\"New Title\",\"body\":\"New Body\"}")
        .when()
                .patch("/posts/{id}", 1) // Replace {id} with the actual ID of the resource you want to update
        .then()
            .assertThat()
                .statusCode(200)
                .body("title", equalTo("New Title"))
                .body("body", equalTo("New Body"))
                .body("id", equalTo(1))
                .body("userId", equalTo(1));
    }

}
