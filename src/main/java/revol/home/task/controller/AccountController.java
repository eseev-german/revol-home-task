package revol.home.task.controller;

import revol.home.task.dto.AccountDTO;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

@Path("/accounts")
public class AccountController {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDTO createAccount(AccountDTO account) {
        return account;
    }

    @GET
    @Path("{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDTO getAccount(@PathParam("accountId") String accountId) {
        return new AccountDTO();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountDTO> getAccounts() {
        return Collections.emptyList();
    }

    @DELETE
    @Path("{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDTO deleteTransaction(@PathParam("accountId") String accountId) {
        return new AccountDTO();
    }
}