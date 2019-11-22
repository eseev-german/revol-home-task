package integration.revol.home.task.api;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ValidatableResponse;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import revol.home.task.dto.AccountDTO;
import revol.home.task.server.ServerBootstrap;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static integration.revol.home.task.DaoTestUtil.createAccount;
import static integration.revol.home.task.DaoTestUtil.deleteAllRows;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApiGettingAllAccountsTest {
    private final static int TEST_PORT = 8081;
    private final static String APPLICATION_CONFIG_PATH = "integration.revol.home.task.config.TestContainerConfig";

    private static Server server;

    @BeforeClass
    public static void beforeClass() throws Exception {
        server = new ServerBootstrap(TEST_PORT, APPLICATION_CONFIG_PATH).createServer();

        RestAssured.port = 8081;
        server.start();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop();
    }

    @After
    public void tearDown() throws Exception {
        deleteAllRows();
    }

    @Test
    public void successfullyGetAllAccounts() throws SQLException {
        createAccount(1L, BigDecimal.TEN);
        AccountDTO firstExpected = new AccountDTO();
        firstExpected.setId("1");
        firstExpected.setBalance("10");

        createAccount(2L, BigDecimal.ZERO);
        AccountDTO secondExpected = new AccountDTO();
        secondExpected.setId("2");
        secondExpected.setBalance("0");

        List<AccountDTO> result = getAccounts().extract()
                                               .as(new TypeRef<List<AccountDTO>>() {
                                               });
        assertEquals(2, result.size());
        assertTrue(result.contains(firstExpected));
        assertTrue(result.contains(secondExpected));
    }

    @Test
    public void noAccountsReturnsIfDbIsEmpty() {
        List<AccountDTO> result = getAccounts().statusCode(200)
                                               .extract()
                                               .as(new TypeRef<List<AccountDTO>>() {
                                               });
        assertTrue(result.isEmpty());
    }

    private ValidatableResponse getAccounts() {
        return given().get("/accounts")
                      .then();
    }
}
