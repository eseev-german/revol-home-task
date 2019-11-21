package integration.revol.home.task.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import revol.home.task.dto.AccountDTO;
import revol.home.task.model.Account;
import revol.home.task.server.ServerBootstrap;

import java.math.BigDecimal;
import java.sql.SQLException;

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

    private static AccountDTO result;

    @BeforeClass
    public static void beforeClass() throws Exception {
        server = new ServerBootstrap(TEST_PORT, APPLICATION_CONFIG_PATH).createServer();

        RestAssured.port = 8081;
        server.start();

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(BALANCE);
        accountDTO.setId(ID);

        result = given().body(accountDTO)
                        .contentType(ContentType.JSON)
                        .post("/accounts")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(AccountDTO.class);

    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop();
    }

    @Test
    public void returnedSameBalance() {
        assertEquals(BALANCE, result.getBalance());
    }

    @Test
    public void givenIdIgnored() {
        assertNotEquals(ID, result.getId());
    }

    @Test
    public void returnedIdIsNotBlank() {
        assertFalse(result.getId().isBlank());
    }

    @Test
    public void accountWithGivenIdIsInDb() throws SQLException {
        Account accountWithGivenIdInDb = getAccountById(result.getId());

        assertEquals(new BigDecimal(BALANCE), accountWithGivenIdInDb.getBalance());
    }
}
