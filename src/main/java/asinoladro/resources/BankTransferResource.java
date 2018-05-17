package asinoladro.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.joda.money.Money;
import org.joda.time.Duration;

import com.codahale.metrics.annotation.Timed;

import asinoladro.api.Account;
import asinoladro.api.TransferConfirmation;
import asinoladro.api.TransferRequest;

@Path("/transfer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BankTransferResource {
	
    @POST
    @Timed
    public TransferConfirmation transferMoney(TransferRequest req) {
    		System.out.print("hello hi");
		Account from = req.getFromAccount();
		Account to = req.getToAccount();
		Money amount = req.getAmount();
		
		long transactionId = System.currentTimeMillis(); // TODO maybe from db?
		Duration duration;
		
		if (from.isRevolutAccount() && to.isRevolutAccount()) {
			duration = Duration.millis(0); // instant!
		}
		else if (!from.isRevolutAccount() && !to.isRevolutAccount()) {
			throw new WebApplicationException(
					"Either the fromAccount or toAccount must be a Revolut account.");
		}
		else {
			duration = Duration.standardHours(2);
		}
		
		if (from.getIban().equals(to.getIban()) ) {
			throw new WebApplicationException("fromAccount and toAccount are the same.");
		}
		
		if (amount.isNegativeOrZero()) {
			throw new WebApplicationException("amount must be positive.");
		}
		
		return new TransferConfirmation(transactionId, duration);
	}
}
