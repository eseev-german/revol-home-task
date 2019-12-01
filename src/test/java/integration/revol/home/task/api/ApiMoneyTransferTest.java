package integration.revol.home.task.api;

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

import java.math.BigDecimal;
import java.sql.SQLException;

import static integration.revol.home.task.DaoTestUtil.createAccount;
import static integration.revol.home.task.DaoTestUtil.deleteAllRows;
import static integration.revol.home.task.DaoTestUtil.getAccountById;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class ApiMoneyTransferTest {
    private final static String SOURCE_ACCOUNT = "1";
    private final static String DESTINATION_ACCOUNT = "2";

    private static Server server;

    @BeforeClass
    public static void beforeClass() throws Exception {
        server = TestServerBootstrap.createServer();
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
    public void errorWhenTransferMoreThanOnAccountBalance() throws SQLException {
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

    @Test
    public void errorWhenTransferEmptyValue() throws SQLException {
        doTransfer("").statusCode(400);

        Account firstAccount = getAccountById(SOURCE_ACCOUNT);
        assertEquals(BigDecimal.TEN, firstAccount.getBalance());

        Account secondAccount = getAccountById(DESTINATION_ACCOUNT);
        assertEquals(BigDecimal.ZERO, secondAccount.getBalance());
    }

    @Test
    public void errorWhenTransferFromUnknownSource() throws SQLException {
        doTransfer("1", "3", DESTINATION_ACCOUNT).statusCode(400);

        Account secondAccount = getAccountById(DESTINATION_ACCOUNT);
        assertEquals(BigDecimal.ZERO, secondAccount.getBalance());
    }

    @Test
    public void errorWhenTransferFromEmptySource() throws SQLException {
        doTransfer("1", "", DESTINATION_ACCOUNT).statusCode(400);

        Account secondAccount = getAccountById(DESTINATION_ACCOUNT);
        assertEquals(BigDecimal.ZERO, secondAccount.getBalance());
    }

    @Test
    public void errorWhenTransferFromInvalidIdSource() throws SQLException {
        doTransfer("1", "not_a_long", DESTINATION_ACCOUNT).statusCode(400);

        Account secondAccount = getAccountById(DESTINATION_ACCOUNT);
        assertEquals(BigDecimal.ZERO, secondAccount.getBalance());
    }

    @Test
    public void errorWhenTransferToUnknownDestination() throws SQLException {
        doTransfer("1", SOURCE_ACCOUNT, "3").statusCode(400);

        Account firstAccount = getAccountById(SOURCE_ACCOUNT);
        assertEquals(BigDecimal.TEN, firstAccount.getBalance());
    }

    @Test
    public void errorWhenTransferToInvalidDestination() throws SQLException {
        doTransfer("1", SOURCE_ACCOUNT, "not_a_long").statusCode(400);

        Account firstAccount = getAccountById(SOURCE_ACCOUNT);
        assertEquals(BigDecimal.TEN, firstAccount.getBalance());
    }

    @Test
    public void errorWhenTransferToEmptyDestination() throws SQLException {
        doTransfer("1", SOURCE_ACCOUNT, "").statusCode(400);

        Account firstAccount = getAccountById(SOURCE_ACCOUNT);
        assertEquals(BigDecimal.TEN, firstAccount.getBalance());
    }

    @Test
    public void errorWhenTransferIsEmpty() throws SQLException {
        doTransferStringBody("").statusCode(500);

        Account firstAccount = getAccountById(SOURCE_ACCOUNT);
        assertEquals(BigDecimal.TEN, firstAccount.getBalance());
        Account secondAccount = getAccountById(DESTINATION_ACCOUNT);
        assertEquals(BigDecimal.ZERO, secondAccount.getBalance());
    }

    @Test
    public void errorWhenTransferIsWrongFormat() throws SQLException {
        doTransferStringBody("Not_a_json").statusCode(500);

        Account firstAccount = getAccountById(SOURCE_ACCOUNT);
        assertEquals(BigDecimal.TEN, firstAccount.getBalance());
        Account secondAccount = getAccountById(DESTINATION_ACCOUNT);
        assertEquals(BigDecimal.ZERO, secondAccount.getBalance());
    }

    private ValidatableResponse doTransfer(String amount) {
        return doTransfer(amount, SOURCE_ACCOUNT, DESTINATION_ACCOUNT);
    }

    private ValidatableResponse doTransfer(String amount, String sourceAccount, String destinationAccount) {
        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();
        moneyTransferDTO.setAmount(amount);
        moneyTransferDTO.setSourceAccount(sourceAccount);
        moneyTransferDTO.setDestinationAccount(destinationAccount);

        return doTransfer(moneyTransferDTO);
    }

    private ValidatableResponse doTransfer(MoneyTransferDTO moneyTransferDTO) {
        return given().body(moneyTransferDTO)
                      .contentType(ContentType.JSON)
                      .post("/accounts/transfers")
                      .then();
    }

    private ValidatableResponse doTransferStringBody(String body) {
        return given().body(body)
                      .contentType(ContentType.JSON)
                      .post("/accounts/transfers")
                      .then();
    }
}