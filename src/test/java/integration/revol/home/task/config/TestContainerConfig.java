package integration.revol.home.task.config;

import integration.TestConnectionPoolProvider;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import revol.home.task.converter.AccountToDtoConverter;
import revol.home.task.converter.DtoToAccountConverter;
import revol.home.task.converter.DtoToMoneyTransferConverter;
import revol.home.task.db.ConnectionPoolProvider;
import revol.home.task.db.dao.AccountDAO;
import revol.home.task.manager.AccountManager;
import revol.home.task.manager.TransferManager;

public class TestContainerConfig extends ResourceConfig {
    public TestContainerConfig() {
        packages("revol.home.task");
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(AccountToDtoConverter.class).to(AccountToDtoConverter.class);
                bind(DtoToAccountConverter.class).to(DtoToAccountConverter.class);
                bind(DtoToMoneyTransferConverter.class).to(DtoToMoneyTransferConverter.class);
                bind(AccountDAO.class).to(AccountDAO.class);
                bind(TestConnectionPoolProvider.class).to(ConnectionPoolProvider.class);
                bind(AccountManager.class).to(AccountManager.class);
                bind(TransferManager.class).to(TransferManager.class);
            }
        });
    }
}
