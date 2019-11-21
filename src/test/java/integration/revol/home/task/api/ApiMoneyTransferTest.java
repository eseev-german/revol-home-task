package integration.revol.home.task.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import revol.home.task.dto.MoneyTransferDTO;
import revol.home.task.model.Account;
import revol.home.task.server.ServerBootstrap;

import java.math.BigDecimal;
import java.sql.SQLException;

import static integration.revol.home.task.DaoTestUtil.createAccount;
import static integration.revol.home.task.DaoTestUtil.deleteAllRows;
import static integration.revol.home.task.DaoTestUtil.getAccountById;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class ApiMoneyTransferTest {
    private final static int TEST_PORT = 8081;
    private final static String APPLICATION_CONFIG_PATH = "integration.revol.home.task.config.TestContainerConfig";
    private final static String SOURCE_ACCOUNT = "1";
    private final static String DESTINATION_ACCOUNT = "2";

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

    @Before
    public void setUp() throws Exception {
        createAccount(1L, BigDecimal.TEN);
        createAccount(2L, BigDecimal.ZERO);
    }

    @After
    public void tearDown() throws Exception {
        deleteAllRows();
    }

    @Test
    public void successfullyTransferred() throws SQLException {
        doTransfer("10").statusCode(200);

        Account firstAccount = getAccountById(SOURCE_ACCOUNT);
        assertEquals(BigDecimal.ZERO, firstAccount.getBalance());

        Account secondAccount = getAccountById(DESTINATION_ACCOUNT);
        assertEquals(BigDecimal.TEN, secondAccount.getBalance());
    }

    @Test
    public void errorWhenTransferMoreThanOnAccount() throws SQLException {
        doTransfer("11").statusCode(400);

        Account firstAccount = getAccountById(SOURCE_ACCOUNT);
        assertEquals(BigDecimal.TEN, firstAccount.getBalance());

        Account secondAccount = getAccountById(DESTINATION_ACCOUNT);
        assertEquals(BigDecimal.ZERO, secondAccount.getBalance());
    }

    @Test
    public void errorWhenTransferNegativeValue() throws SQLException {
        doTransfer("-1").statusCode(400);

        Account firstAccount = getAccountById(SOURCE_ACCOUNT);
        assertEquals(BigDecimal.TEN, firstAccount.getBalance());

        Account secondAccount = getAccountById(DESTINATION_ACCOUNT);
        assertEquals(BigDecimal.ZERO, secondAccount.getBalance());
    }

    private ValidatableResponse doTransfer(String amount) {
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setAmount(amount);
        moneyTransferDTO.setSourceAccount(SOURCE_ACCOUNT);
        moneyTransferDTO.setDestinationAccount(DESTINATION_ACCOUNT);

        return given().body(moneyTransferDTO)
                      .contentType(ContentType.JSON)
                      .post("/accounts/transfers")
                      .then();
    }
}
