package revol.home.task.controller;

import revol.home.task.dto.MoneyTransactionDTO;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

@Path("/")
public class MoneyTransferController {
    @POST
    @Path("/accounts/{accountId}/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public MoneyTransactionDTO transferMoney(@PathParam("accountId") String accountId, MoneyTransactionDTO transaction) {
        return transaction;
    }

    @GET
    @Path("accounts/transactions/{transactionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public MoneyTransactionDTO getTransaction(@PathParam("transactionId") String transactionId) {
        return new MoneyTransactionDTO();
    }
    @GET
    @Path("accounts/{accountId}/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MoneyTransactionDTO> getAccountTransactions(@PathParam("accountId") String accountId) {
        return Collections.emptyList();
    }

    @GET
    @Path("accounts/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MoneyTransactionDTO> getTransactions() {
        return Collections.emptyList();
    }

    @DELETE
    @Path("accounts/transactions/{transactionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public MoneyTransactionDTO deleteTransaction(@PathParam("transactionId") String transactionId) {
        return new MoneyTransactionDTO();
    }
}