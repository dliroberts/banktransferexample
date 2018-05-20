package asinoladro.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;

import asinoladro.api.Account;
import asinoladro.api.Customer;

public class TransactionDaoTest extends BaseDaoTest {
	private static final Account ACCOUNT_DUNCAN_GBP = new Account(
			"GB55ZAFY89851748597528",
			Money.of(CurrencyUnit.GBP, new BigDecimal("333")),
			new Customer(1, "d roberts"));
	    private static final Account ACCOUNT_NACNUD_EUR = new Account(
			"ES2364265841767173822054",
			Money.of(CurrencyUnit.EUR, new BigDecimal("31")),
			new Customer(2, "mr nacnud strebor"));

	private TransactionDao transactionDao;
	private AccountDao accountDao;

    @Before
    public void before() {
    		transactionDao = getJdbi().onDemand(TransactionDao.class);
    		accountDao = getJdbi().onDemand(AccountDao.class);
    }

    @Test
    public void transferAPound() {
//    		BigDecimal expected = new BigDecimal("0.875415693");
    		long transId = transactionDao.execTransaction(
			ACCOUNT_DUNCAN_GBP,
			Money.of(CurrencyUnit.GBP, BigDecimal.ONE),
			ACCOUNT_NACNUD_EUR,
			Money.of(CurrencyUnit.EUR, new BigDecimal("1.14")),
			new BigDecimal("1.14231445472"));
    		
    		assertTrue(transId > 0L);
    		
    		BigDecimal expected1 = new BigDecimal("2.14");
    		BigDecimal actual1 =
    				accountDao.findAccountByIban(ACCOUNT_DUNCAN_GBP.getIban()).getBalance().getAmount();
    		assertEquals(expected1, actual1);
    		
    		BigDecimal expected2 = new BigDecimal("124.15");
    		BigDecimal actual2 =
    				accountDao.findAccountByIban(ACCOUNT_NACNUD_EUR.getIban()).getBalance().getAmount();
    		assertEquals(expected2, actual2);
    }
}
