package asinoladro.db;

import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;

import org.jdbi.v3.core.Jdbi;
import org.joda.money.CurrencyUnit;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asinoladro.BankTransferExampleApplication;
import asinoladro.BankTransferExampleConfiguration;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

public class ExchangeRateDaoTest {
//    @Rule
//    public DbSetupRule dbSetupRule = new DbSetupRule();

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
        exchangeRateDao = jdbi.onDemand(ExchangeRateDao.class);
    }
	
    private ExchangeRateDao exchangeRateDao;

//    @Before
//    public void before() {
//    		exchangeRateDao = dbSetupRule.getJdbi().onDemand(ExchangeRateDao.class);
//    }

    @Test
    public void eurGbpExchangeRate() {
    		BigDecimal expected = new BigDecimal("0.875415693");
    		BigDecimal actual = exchangeRateDao.getExchangeRate(CurrencyUnit.EUR, CurrencyUnit.GBP);
    		
    		assertEquals(expected, actual);
    }

}
