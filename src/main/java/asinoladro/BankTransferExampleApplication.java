package asinoladro;

import org.jdbi.v3.core.Jdbi;

import asinoladro.db.AccountDao;
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
        return "BankTransferRevolut";
    }

    @Override
    public void initialize(final Bootstrap<BankTransferExampleConfiguration> bootstrap) {
        
    }

    @Override
    public void run(final BankTransferExampleConfiguration config,
                    final Environment environment) {
        JdbiFactory factory = new JdbiFactory();
        Jdbi jdbi = factory.build(environment, config.getDataSourceFactory(), "database");
    		
        AccountDao dao = jdbi.onDemand(AccountDao.class);
        
    		BankTransferResource resource = new BankTransferResource(dao);
        
        JerseyEnvironment jersey = environment.jersey();
        jersey.register(resource);
    }

}
