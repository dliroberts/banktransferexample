package asinoladro;

import org.jdbi.v3.core.Jdbi;

import asinoladro.db.AccountDao;
import asinoladro.db.ExchangeRateDao;
import asinoladro.db.TransactionDao;
import asinoladro.resources.BankTransferResource;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class BankTransferExampleApplication extends Application<BankTransferExampleConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BankTransferExampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "BankTransferExample";
    }

    @Override
    public void initialize(final Bootstrap<BankTransferExampleConfiguration> bootstrap) {
        
    }

    @Override
    public void run(final BankTransferExampleConfiguration config,
                    final Environment environment) {
        JdbiFactory factory = new JdbiFactory();
        Jdbi jdbi = factory.build(environment, config.getDataSourceFactory(), "database");
    		
        AccountDao accountDao = jdbi.onDemand(AccountDao.class);
        TransactionDao transactionDao = jdbi.onDemand(TransactionDao.class);
        ExchangeRateDao exchangeRateDao = jdbi.onDemand(ExchangeRateDao.class);
        
    		BankTransferResource resource =
    				new BankTransferResource(accountDao, transactionDao, exchangeRateDao);
        
        JerseyEnvironment jersey = environment.jersey();
        jersey.register(resource);
    }

}
