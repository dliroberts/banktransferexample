package asinoladro.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.jdbi.v3.core.Jdbi;
import org.joda.money.Money;
import org.joda.time.Duration;

import com.codahale.metrics.annotation.Timed;

import asinoladro.api.Account;
import asinoladro.api.AccountSpec;
import asinoladro.api.TransferConfirmation;
import asinoladro.api.TransferRequest;
import asinoladro.db.AccountDao;

@Path("/transfer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BankTransferResource {
	
	private AccountDao accountDao;

	public BankTransferResource(AccountDao accountDao) {
		this.accountDao = accountDao;
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
		
		long transactionId = System.currentTimeMillis(); // TODO maybe from db?
		Duration duration;
		
		if (isLocalFrom && isLocalTo) {
			duration = Duration.millis(0); // instant!
		}
		else if (!isLocalFrom && !isLocalTo) {
			throw new WebApplicationException(
					"Either the fromAccount or toAccount must be a local account.");
		}
		else {
			duration = Duration.standardHours(2);
		}
		
		if (from.getIban().equals(to.getIban()) ) {
			throw new WebApplicationException("fromAccount and toAccount are the same.");
		}

		return new TransferConfirmation(transactionId, duration);
	}
    
    private Account getLocalAccount(AccountSpec account) {
    		return accountDao.findAccountByIban(account.getIban());
    }
}
