package integration.revol.home.task.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import revol.home.task.dto.AccountDTO;
import revol.home.task.model.Account;
import revol.home.task.server.ServerBootstrap;

import java.math.BigDecimal;
import java.sql.SQLException;

import static integration.revol.home.task.DaoTestUtil.deleteAllRows;
import static integration.revol.home.task.DaoTestUtil.getAccountById;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;


public class ApiCreationTest {
    private final static int TEST_PORT = 8081;
    private final static String APPLICATION_CONFIG_PATH = "integration.revol.home.task.config.TestContainerConfig";

    private final static String ID = "1111";
    private final static String BALANCE = "10000";

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
    public void returnedSameBalance() {
        AccountDTO result = performCreateAccountRequest(ID, BALANCE)
                .statusCode(200)
                .extract()
                .as(AccountDTO.class);

        assertEquals(BALANCE, result.getBalance());
    }

    @Test
    public void givenIdIgnored() {
        AccountDTO result = performCreateAccountRequest(ID, BALANCE)
                .statusCode(200)
                .extract()
                .as(AccountDTO.class);

        assertNotEquals(ID, result.getId());
    }

    @Test
    public void returnedIdIsNotBlank() {
        AccountDTO result = performCreateAccountRequest(ID, BALANCE)
                .statusCode(200)
                .extract()
                .as(AccountDTO.class);

        assertFalse(result.getId()
                          .isBlank());
    }

    @Test
    public void accountWithGivenIdIsInDb() throws SQLException {
        AccountDTO result = performCreateAccountRequest(ID, BALANCE)
                .statusCode(200)
                .extract()
                .as(AccountDTO.class);

        Account accountWithGivenIdInDb = getAccountById(result.getId());

        assertEquals(new BigDecimal(BALANCE), accountWithGivenIdInDb.getBalance());
    }

    @Test
    public void emptyBalance() {
        performCreateAccountRequest(null, "")
                .statusCode(400);
    }

    @Test
    public void wrongFormatBalance() {
        performCreateAccountRequest(null, "Not_a_big_decimal")
                .statusCode(400);
    }

    private ValidatableResponse performCreateAccountRequest(String id, String balance) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(balance);
        accountDTO.setId(id);

        return given().body(accountDTO)
                      .contentType(ContentType.JSON)
                      .post("/accounts")
                      .then();
    }

}
