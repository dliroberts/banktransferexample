package asinoladro.resources;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
	
	/**
	 * Transfers funds from one account to another.
	 * 
	 * <p>Features of note:</p>
	 * 
	 * <ol>
	 *  <li>Supports transfers between different currencies.</li>
	 *  <li>Requester must specify the IBANs, account holder names and account currencies of both accounts;
	 *  there is fuzzy match logic on the names so 'MR D. ROBERTS' will match an account held by 'Duncan
	 *  Roberts'.</li>
	 * </ol>
	 * 
	 * <p>Limitations:</p>
	 * 
	 * <ol>
	 * 	<li>Accounts are identified by IBANs, but as of May 2018, many countries (including the US) have not
	 *  yet adopted the IBAN standard.</li>
	 *  <li>Both the from and to accounts must be 'internal', i.e. held by this bank. See notes below on
	 * 	how consistency might be achieved for inter-bank transfers through distributed transactions.</li>
	 * 	<li>The transfer is rejected if it would result in a negative balance in the from account:
	 *  no support for overdrafts.</li>
	 *  <li>No payment references, account addresses...</li>
	 *  <li>No tracking of historic exchange rate data (this would best be kept in a separate table to avoid
	 *  slowing down the rate lookup.)</li>
	 * </ol>
	 */
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
			return execLocalTransaction(localFrom, localTo, amount);
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
	}
    
    TransferConfirmation execLocalTransaction(
    			Account fromAccount, Account toAccount, Money moneyToTransfer) {
    		final CurrencyUnit fromCurrency = fromAccount.getBalance().getCurrencyUnit();
    		final CurrencyUnit toCurrency = toAccount.getBalance().getCurrencyUnit();
    		
    		Money fromMoney, toMoney;
    		
    		final BigDecimal exchangeRate;
    		
    		if (moneyToTransfer.getCurrencyUnit().equals(fromCurrency)) {
    			exchangeRate = exchangeRateDao.getExchangeRate(fromCurrency, toCurrency);
    			fromMoney = moneyToTransfer;
    			toMoney = moneyToTransfer.convertedTo(toCurrency, exchangeRate, RoundingMode.HALF_EVEN);
    		}
    		else if (moneyToTransfer.getCurrencyUnit().equals(toCurrency)) {
    			exchangeRate = exchangeRateDao.getExchangeRate(toCurrency, fromCurrency);
    			fromMoney = moneyToTransfer.convertedTo(fromCurrency, exchangeRate, RoundingMode.HALF_EVEN);
    			toMoney = moneyToTransfer;
    		}
    		else {
    			throw new WebApplicationException(
				"The amount to transfer should be in the currency of either the fromAccount or the "
				+ "toAccount.");
    		}
    		
    		if (fromAccount.getBalance().getAmount().subtract(
    				fromMoney.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
    			throw new WebApplicationException(
    					"There are not enough funds in fromAccount for this transaction.");
    		}
    		
    		long transactionId = transactionDao.execTransaction(
			fromAccount, fromMoney, toAccount, toMoney, exchangeRate);
    		Duration duration = Duration.millis(0); // instant!
		
    		return new TransferConfirmation(transactionId, duration, fromMoney, toMoney);
    }
    
    Account getLocalAccount(AccountSpec account) {
    		return accountDao.findAccountByIban(account.getIban());
    }
}
