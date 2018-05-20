package asinoladro.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import asinoladro.api.Account;
import asinoladro.api.AccountSpec;
import asinoladro.api.Customer;
import asinoladro.api.TransferConfirmation;
import asinoladro.api.TransferRequest;
import asinoladro.db.AccountDao;
import asinoladro.db.ExchangeRateDao;
import asinoladro.db.TransactionDao;
import io.dropwizard.testing.junit.ResourceTestRule;

public class BankTransferResourceTest {

    private static final AccountDao accountDao = mock(AccountDao.class);
    private static final ExchangeRateDao exchangeRateDao = mock(ExchangeRateDao.class);
    private static final TransactionDao transactionDao = mock(TransactionDao.class);
	
    private static final AccountSpec ACCOUNTSPEC_DUNCAN_GBP =
    		new AccountSpec("GB55ZAFY89851748597528", "duncan roberts", CurrencyUnit.GBP);
    private static final AccountSpec ACCOUNTSPEC_NACNUD_EUR =
    		new AccountSpec("ES2364265841767173822054", "Mr Nacnud Strebor", CurrencyUnit.EUR);
    
    private static final Account ACCOUNT_DUNCAN_GBP = new Account(
		"GB55ZAFY89851748597528",
		Money.of(CurrencyUnit.GBP, new BigDecimal("333")),
		new Customer(1, "d roberts"));
    private static final Account ACCOUNT_NACNUD_EUR = new Account(
		"ES2364265841767173822054",
		Money.of(CurrencyUnit.EUR, new BigDecimal("31")),
		new Customer(2, "mr nacnud strebor"));

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new BankTransferResource(accountDao, transactionDao, exchangeRateDao))
            .build();
    
    @Before
    public void setup() {
        when(accountDao.findAccountByIban(eq(ACCOUNTSPEC_DUNCAN_GBP.getIban())))
        		.thenReturn(ACCOUNT_DUNCAN_GBP);
        when(accountDao.findAccountByIban(eq(ACCOUNTSPEC_NACNUD_EUR.getIban())))
			.thenReturn(ACCOUNT_NACNUD_EUR);
        
        when(exchangeRateDao.getExchangeRate(eq(CurrencyUnit.GBP), eq(CurrencyUnit.EUR)))
			.thenReturn(new BigDecimal("1.25"));
        when(exchangeRateDao.getExchangeRate(eq(CurrencyUnit.EUR), eq(CurrencyUnit.GBP)))
			.thenReturn(new BigDecimal("0.8"));
        
        when(transactionDao.execTransaction(any(), any(), any(), any(), any())).thenReturn(1L);
    }
    
    @After
    public void tearDown(){
        reset(accountDao);
        reset(exchangeRateDao);
        reset(transactionDao);
    }
    
    private TransferConfirmation post(TransferRequest req) {
    		Entity<?> entity = Entity.entity(req, MediaType.APPLICATION_JSON_TYPE);
		
    		return resources.target("/transfer").request().post(entity).readEntity(TransferConfirmation.class);
    }

    /**
	 * successful eur to gbp transfer
	 */
	@Test
	public void happyPathEurToGbp() {
		TransferRequest req = new TransferRequest(
			Money.of(CurrencyUnit.EUR, new BigDecimal("23.1")),
			ACCOUNTSPEC_NACNUD_EUR,
			ACCOUNTSPEC_DUNCAN_GBP
		);
		
		TransferConfirmation expected = new TransferConfirmation(
			1L,
			Duration.ZERO,
			Money.of(CurrencyUnit.EUR, new BigDecimal("23.10")),
			Money.of(CurrencyUnit.GBP, new BigDecimal("18.48"))
		);
		
		TransferConfirmation actual = post(req);
		
		assertThat(actual).isEqualTo(expected);
		
        verify(accountDao).findAccountByIban("GB55ZAFY89851748597528");
        verify(accountDao).findAccountByIban("ES2364265841767173822054");
        verify(exchangeRateDao).getExchangeRate(CurrencyUnit.EUR, CurrencyUnit.GBP);
        verify(transactionDao).execTransaction(
        		ACCOUNT_NACNUD_EUR,
        		Money.of(CurrencyUnit.EUR, new BigDecimal("23.10")),
        		ACCOUNT_DUNCAN_GBP,
        		Money.of(CurrencyUnit.GBP, new BigDecimal("18.48")),
        		new BigDecimal("0.8")
        	);
	}

	/**
	 * successful gbp to eur transfer
	 */
	@Test
	public void happyPathGbpToEur() {
		TransferRequest req = new TransferRequest(
			Money.of(CurrencyUnit.GBP, new BigDecimal("40")),
			ACCOUNTSPEC_DUNCAN_GBP,
			ACCOUNTSPEC_NACNUD_EUR
		);
		
		TransferConfirmation expected = new TransferConfirmation(
			1L,
			Duration.ZERO,
			Money.of(CurrencyUnit.GBP, new BigDecimal("40")),
			Money.of(CurrencyUnit.EUR, new BigDecimal("50"))
		);
		
		TransferConfirmation actual = post(req);
		
		assertThat(actual).isEqualTo(expected);
		
        verify(accountDao).findAccountByIban("GB55ZAFY89851748597528");
        verify(accountDao).findAccountByIban("ES2364265841767173822054");
        verify(exchangeRateDao).getExchangeRate(CurrencyUnit.GBP, CurrencyUnit.EUR);
        verify(transactionDao).execTransaction(
        		ACCOUNT_DUNCAN_GBP,
        		Money.of(CurrencyUnit.GBP, new BigDecimal("40")),
        		ACCOUNT_NACNUD_EUR,
        		Money.of(CurrencyUnit.EUR, new BigDecimal("50")),
        		new BigDecimal("1.25")
        	);
	}

	/**
	 * request to transfer GBP 400 from an account holding GBP 333
	 */
	@Test(expected = ProcessingException.class)
	public void notEnoughFunds() {
		TransferRequest req = new TransferRequest(
			Money.of(CurrencyUnit.GBP, new BigDecimal("400")),
			ACCOUNTSPEC_DUNCAN_GBP,
			ACCOUNTSPEC_NACNUD_EUR
		);
		
		post(req);
	}

	/**
	 * request to transfer negative funds
	 */
	@Test(expected = ProcessingException.class)
	public void transferNegative() {
		TransferRequest req = new TransferRequest(
			Money.of(CurrencyUnit.GBP, new BigDecimal("-94.03")),
			ACCOUNTSPEC_DUNCAN_GBP,
			ACCOUNTSPEC_NACNUD_EUR
		);
		
		post(req);
	}

	/**
	 * request to transfer... nothing
	 */
	@Test(expected = ProcessingException.class)
	public void transferZero() {
		TransferRequest req = new TransferRequest(
			Money.of(CurrencyUnit.GBP, BigDecimal.ZERO),
			ACCOUNTSPEC_DUNCAN_GBP,
			ACCOUNTSPEC_NACNUD_EUR
		);
		
		post(req);
	}

	/**
	 * transfer from gbp account to a eur account, and yet specified amount to transfer is in a third
	 * currency
	 */
	@Test(expected = ProcessingException.class)
	public void transferUsd() {
		TransferRequest req = new TransferRequest(
			Money.of(CurrencyUnit.USD, BigDecimal.TEN),
			ACCOUNTSPEC_DUNCAN_GBP,
			ACCOUNTSPEC_NACNUD_EUR
		);
		
		post(req);
	}

	/**
	 * to account and from account the same
	 */
	@Test(expected = ProcessingException.class)
	public void transferToSameAccount() {
		TransferRequest req = new TransferRequest(
			Money.of(CurrencyUnit.GBP, BigDecimal.TEN),
			ACCOUNTSPEC_DUNCAN_GBP,
			ACCOUNTSPEC_DUNCAN_GBP
		);
		
		post(req);
	}

	/**
	 * toAccount spec with incorrect currency
	 */
	@Test(expected = ProcessingException.class)
	public void wrongAccountCurrency() {
		TransferRequest req = new TransferRequest(
			Money.of(CurrencyUnit.GBP, BigDecimal.ONE),
			ACCOUNTSPEC_DUNCAN_GBP,
			new AccountSpec("ES2364265841767173822054", "Mr Nacnud Strebor", CurrencyUnit.JPY)
		);
		
		post(req);
	}

	/**
	 * fromAccount spec with incorrect name
	 */
	@Test(expected = ProcessingException.class)
	public void wrongFromAccountName() {
		TransferRequest req = new TransferRequest(
			Money.of(CurrencyUnit.GBP, BigDecimal.ONE),
			ACCOUNTSPEC_DUNCAN_GBP,
			new AccountSpec("ES2364265841767173822054", "john smith", CurrencyUnit.EUR)
		);
		
		post(req);
	}
}
