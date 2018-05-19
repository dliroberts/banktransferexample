package asinoladro.resources;

import java.math.BigDecimal;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.Duration;

import com.codahale.metrics.annotation.Timed;

import asinoladro.api.Account;
import asinoladro.api.AccountSpec;
import asinoladro.api.TransferConfirmation;
import asinoladro.api.TransferRequest;
import asinoladro.db.AccountDao;
import asinoladro.db.ExchangeRateDao;
import asinoladro.db.TransactionDao;

@Path("/transfer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BankTransferResource {
	
	private final AccountDao accountDao;
	private final TransactionDao transactionDao;
	private final ExchangeRateDao exchangeRateDao;

	public BankTransferResource(AccountDao accountDao, TransactionDao transactionDao, ExchangeRateDao exchangeRateDao) {
		this.accountDao = accountDao;
		this.transactionDao = transactionDao;
		this.exchangeRateDao = exchangeRateDao;
	}
	
    @POST
    @Timed
    public TransferConfirmation transferMoney(TransferRequest req) {
    		AccountSpec from = req.getFromAccount();
		AccountSpec to = req.getToAccount();
		Money amount = req.getAmount();

		if (amount.isNegativeOrZero()) {
			throw new WebApplicationException("amount must be positive.");
		}
		
		Account localFrom = getLocalAccount(from);
		Account localTo = getLocalAccount(to);
		boolean isLocalFrom = localFrom != null;
		boolean isLocalTo = localTo != null;
		
		long transactionId;
		Duration duration;
		
		// check name, currency in spec is (roughly) correct
		if (isLocalFrom && !from.matches(localFrom)) {
			throw new WebApplicationException("fromAccount details don't match our records.");
		}

		if (isLocalTo && !to.matches(localTo)) {
			throw new WebApplicationException("toAccount details don't match our records.");
		}

		if (from.getIban().equals(to.getIban()) ) {
			throw new WebApplicationException("fromAccount and toAccount are the same.");
		}
		
		if (isLocalFrom && isLocalTo) {
			duration = Duration.millis(0); // instant!
			
			transactionId = execLocalTransaction(localFrom, localTo, amount);
		}
		else if (!isLocalFrom && !isLocalTo) {
			throw new WebApplicationException(
					"Either the fromAccount or toAccount must be a local account.");
		}
		else {
//			duration = Duration.standardHours(2);
			
			/*
			 * this is perhaps a cop-out, but doing internal-to-external or external-to-internal transfers
			 * considerably complicates the implementation, because we need the transfer to be transactional.
			 * it's not acceptable for a systems failure to result in both sender and receiver to keep the
			 * money, or for the money to be debited from the sender without being (eventually) credited to
			 * the receiver.
			 * 
			 * in an internal-to-internal transfer, this is easy: the credit and debit can be wrapped in a
			 * single db transaction. when one account is external, a distributed 'XA' transaction is needed.
			 * achieving consistency across the internal db and the external system is difficult at best with
			 * a REST API call. the implementation details will depend on the API of the inter-bank payment
			 * system, but one possibility is to have an XA transaction spanning:
			 * 
			 *   1. db update to internal account with status 'pending'
			 *   2. post to jms queue (e.g. ActiveMQ) to communicate to ext system the transaction
			 *   
			 * the ext system can then confirm or reject the payment by sending a message back through
			 * another queue. this solution is eventually consistent if sysadmins on both ends ensure that
			 * all messages in both directions end up being serviced, sooner or later.
			 */
			throw new WebApplicationException(
					"Transfers from or to a non-local account are not currently supported.");
		}
		
		return new TransferConfirmation(transactionId, duration);
	}
    
    private BigDecimal getExchangeRate(CurrencyUnit from, CurrencyUnit to) {
    		CurrencyUnit lowerAlpha, higherAlpha;
    		
    		if (from.getCurrencyCode().compareTo(to.getCurrencyCode()) <= 0) {
    			lowerAlpha = from;
    			higherAlpha = to;
    		}
    		else {
    			lowerAlpha = to;
    			higherAlpha = from;
    		}
    		
    		BigDecimal exchangeRate = exchangeRateDao.getExchangeRate(
    				lowerAlpha.getCurrencyCode(), higherAlpha.getCurrencyCode());
    		
    		if (exchangeRate == null)
    			throw new WebApplicationException(
    					String.format("Exchange rate not found: {} to {}", lowerAlpha, higherAlpha));
    		
    		return exchangeRate;
    }
    
    private long execLocalTransaction(Account fromAccount, Account toAccount, Money moneyToTransfer) {
    		final CurrencyUnit fromCurrency = fromAccount.getBalance().getCurrencyUnit();
    		final CurrencyUnit toCurrency = toAccount.getBalance().getCurrencyUnit();
    		
    		Money fromMoney, toMoney;
    		
    		final BigDecimal exchangeRate;
    		
    		if (moneyToTransfer.getCurrencyUnit().equals(fromCurrency)) {
    			exchangeRate = getExchangeRate(fromCurrency, toCurrency);
    			fromMoney = moneyToTransfer;
    			toMoney = Money.of(toCurrency, moneyToTransfer.getAmount().divide(exchangeRate));
    		}
    		else if (moneyToTransfer.getCurrencyUnit().equals(toCurrency)) {
    			exchangeRate = getExchangeRate(toCurrency, fromCurrency);
    			fromMoney = Money.of(fromCurrency, moneyToTransfer.getAmount().divide(exchangeRate));
    			toMoney = moneyToTransfer;
    		}
    		else {
    			throw new WebApplicationException(
				"The amount to transfer should be in the currency of either the fromAccount or the "
				+ "toAccount.");
    		}
    		
    		if (fromAccount.getBalance().getAmount().subtract(
    				moneyToTransfer.getAmount()).compareTo(BigDecimal.ZERO) <= 0) {
    			throw new WebApplicationException(
    					"There are not enough funds in fromAccount for this transaction.");
    		}
    		
    		accountDao.addFunds(fromAccount.getIban(), moneyToTransfer.getAmount().negate());
    		accountDao.addFunds(toAccount.getIban(), moneyToTransfer.getAmount());
    		return transactionDao.addTransaction(
			fromAccount.getIban(),
			fromMoney.getAmount(),
			fromCurrency.toString(),
			toAccount.getIban(),
			toMoney.getAmount(),
			toCurrency.toString(),
			exchangeRate
		);
    }
    
    private Account getLocalAccount(AccountSpec account) {
    		return accountDao.findAccountByIban(account.getIban());
    }
}
