package asinoladro.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

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
		
		long transactionId = System.currentTimeMillis(); // TODO maybe from db?
		Duration duration;
		
		if (from.isRevolutAccount() && to.isRevolutAccount()) {
			duration = Duration.millis(0); // instant!
		}
		else if (!from.isRevolutAccount() && !to.isRevolutAccount()) {
			throw new WebApplicationException("Either the from or to account must be a Revolut account.");
		}
		else {
			duration = Duration.standardHours(2);
		}
		
		return new TransferConfirmation(transactionId, duration);
	}
}
