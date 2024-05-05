package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTest {

    @Test
    void checkTotal() {

        get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkTotalWithGiven() {

        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkTotalWithGivenWithSomeLogs() {

        given()
                .log().uri()
                .log().body()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkChromeVersion() {

        given()
                .log().all()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("browsers.chrome", hasKey("100.0"));
    }

    @Test
    void checkResponceBadPractice() {

        String expectedResultString = "{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0,\"browsers\":{\"chrome\":{\"100.0\":{}," +
                "\"120.0\":{},\"121.0\":{},\"122.0\":{},\"99.0\":{}},\"firefox\":{\"122.0\"" +
                ":{},\"123.0\":{}},\"opera\":{\"106.0\":{},\"107.0\":{}}}}";

        Response actualResponse = given()
                //               .log().all()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().response();

        System.out.println(actualResponse);

        String actualResponseString = actualResponse.asString();
        System.out.println(actualResponseString);

        assertEquals(expectedResultString, actualResponseString);

    }

    @Test
    void checkTotalGoodPractice() {
        Integer expectedTotal = 20;
        Integer actualTotal = given()
                .log().uri()
                .log().body()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract()
                .path("total");

        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    void check401WDHubStatus() {

        given()
                .auth().basic("user1", "1234")
                .log().uri()
                .log().body()
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("value.ready", is(true));
    }

    @Test
    void checkWDTotal() {

        given()
                .log().uri()
                .log().body()
                .when()
                .get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("value.ready", is(true));
    }
}
