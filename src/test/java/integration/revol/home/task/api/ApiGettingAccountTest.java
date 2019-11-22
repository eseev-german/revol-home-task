package integration.revol.home.task.api;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import revol.home.task.dto.AccountDTO;
import revol.home.task.server.ServerBootstrap;

import java.math.BigDecimal;

import static integration.revol.home.task.DaoTestUtil.createAccount;
import static integration.revol.home.task.DaoTestUtil.deleteAllRows;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class ApiGettingAccountTest {
    private final static int TEST_PORT = 8081;
    private final static String APPLICATION_CONFIG_PATH = "integration.revol.home.task.config.TestContainerConfig";

    private static Server server;

    @BeforeClass
    public static void beforeClass() throws Exception {
        server = new ServerBootstrap(TEST_PORT, APPLICATION_CONFIG_PATH).createServer();

        RestAssured.port = 8081;
        server.start();

        createAccount(1L, BigDecimal.TEN);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        deleteAllRows();
        server.stop();
    }

    @Test
    public void successfullyGetAccountById() {
        AccountDTO result = getAccountById("1").statusCode(200)
                                               .extract()
                                               .as(AccountDTO.class);

        assertEquals("1", result.getId());
        assertEquals("10", result.getBalance());
    }

    @Test
    public void getAccountByUnknownId() {
        getAccountById("3").statusCode(400);
    }

    @Test
    public void getAccountByWrongFormatId() {
        getAccountById("not_a_long").statusCode(400);
    }

    private ValidatableResponse getAccountById(String id) {
        return given().get("/accounts/{id}", id)
                      .then();
    }
}
