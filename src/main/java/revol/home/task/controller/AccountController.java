package revol.home.task.controller;

import revol.home.task.dto.AccountDTO;
import revol.home.task.dto.MoneyTransferDTO;
import revol.home.task.manager.AccountManager;
import revol.home.task.manager.TransferManager;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/accounts")
public class AccountController {

    private final AccountManager accountManager;
    private final TransferManager transferManager;

    @Inject
    public AccountController(AccountManager accountManager, TransferManager transferManager) {
        this.accountManager = accountManager;
        this.transferManager = transferManager;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/transactions")
    public void transferMoney(MoneyTransferDTO transaction) {
        transferManager.transferMoney(transaction);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDTO createAccount(AccountDTO account) {
        return accountManager.createAccount(account);
    }

    @GET
    @Path("{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDTO getAccount(@PathParam("accountId") String accountId) {
        return accountManager.getAccount(accountId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountDTO> getAccounts() {
        return accountManager.getAccounts();
    }
}