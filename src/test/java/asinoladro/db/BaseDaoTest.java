package asinoladro.db;

import org.jdbi.v3.core.Jdbi;
import org.junit.Before;
import org.junit.ClassRule;

import asinoladro.BankTransferExampleApplication;
import asinoladro.BankTransferExampleConfiguration;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

public class BaseDaoTest {
	@ClassRule
    public static final DropwizardAppRule<BankTransferExampleConfiguration> SUPPORT =
            new DropwizardAppRule<BankTransferExampleConfiguration>(
            		BankTransferExampleApplication.class,
            		ResourceHelpers.resourceFilePath("config-test.yml"));
    
    private Jdbi jdbi;
    
    @Before
    public void before() throws Throwable {
    		JdbiFactory factory = new JdbiFactory();
        jdbi = factory.build(SUPPORT.getEnvironment(),
        		SUPPORT.getConfiguration().getDataSourceFactory(), "database");
    }
    
    public Jdbi getJdbi() {
    		return jdbi;
    }
}
