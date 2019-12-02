package revol.home.task.config;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import revol.home.task.converter.AccountToDtoConverter;
import revol.home.task.converter.DtoToAccountConverter;
import revol.home.task.converter.DtoToMoneyTransferConverter;
import revol.home.task.db.ConnectionPoolProvider;
import revol.home.task.db.H2ConnectionPoolProvider;
import revol.home.task.db.PreparedStatementExecutor;
import revol.home.task.db.PreparedStatementProvider;
import revol.home.task.db.dao.AccountDAO;
import revol.home.task.manager.AccountManager;
import revol.home.task.manager.TransferManager;

public class ContainerConfig extends ResourceConfig {
    public ContainerConfig() {
        packages("revol.home.task");
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(AccountToDtoConverter.class).to(AccountToDtoConverter.class);
                bind(DtoToAccountConverter.class).to(DtoToAccountConverter.class);
                bind(DtoToMoneyTransferConverter.class).to(DtoToMoneyTransferConverter.class);
                bind(AccountDAO.class).to(AccountDAO.class);
                bind(PreparedStatementExecutor.class).to(PreparedStatementExecutor.class);
                bind(PreparedStatementProvider.class).to(PreparedStatementProvider.class);
                bind(H2ConnectionPoolProvider.class).to(ConnectionPoolProvider.class);
                bind(AccountManager.class).to(AccountManager.class);
                bind(TransferManager.class).to(TransferManager.class);
            }
        });
    }
}
