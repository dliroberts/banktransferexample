package asinoladro.db;

import org.jdbi.v3.core.Jdbi;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;

import asinoladro.BankTransferExampleApplication;
import asinoladro.BankTransferExampleConfiguration;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

public class DbSetupRule extends ExternalResource {
	@ClassRule
    public static final DropwizardAppRule<BankTransferExampleConfiguration> SUPPORT =
            new DropwizardAppRule<BankTransferExampleConfiguration>(
            		BankTransferExampleApplication.class,
            		ResourceHelpers.resourceFilePath("config-test.yml")//,
            		// use a randomly selected port
//            		ConfigOverride.config("server.applicationConnectors[0].port", "0")
            );
    
    private Jdbi jdbi;
    
    @Before
    protected void before() throws Throwable {
    		JdbiFactory factory = new JdbiFactory();
        jdbi = factory.build(SUPPORT.getEnvironment(),
        		SUPPORT.getConfiguration().getDataSourceFactory(), "database");
    }
    
    public Jdbi getJdbi() {
    		return jdbi;
    }
}
