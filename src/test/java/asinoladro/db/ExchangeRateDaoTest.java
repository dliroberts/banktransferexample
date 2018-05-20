package asinoladro.db;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.joda.money.CurrencyUnit;
import org.junit.Before;
import org.junit.Test;

public class ExchangeRateDaoTest extends BaseDaoTest {
	
    private ExchangeRateDao dao;

    @Before
    public void before() {
    		dao = getJdbi().onDemand(ExchangeRateDao.class);
    }

    @Test
    public void eurGbpExchangeRate() {
    		BigDecimal expected = new BigDecimal("0.875415693");
    		BigDecimal actual = dao.getExchangeRate(CurrencyUnit.EUR, CurrencyUnit.GBP);
    		
    		assertEquals(expected, actual);
    }
}
