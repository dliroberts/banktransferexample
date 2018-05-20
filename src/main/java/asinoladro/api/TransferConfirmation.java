package asinoladro.api;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.money.Money;
import org.joda.time.Duration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import asinoladro.api.serialization.CurrencyUnitDeserializer;
import asinoladro.api.serialization.Iso8601DurationSerializer;
import asinoladro.api.serialization.MoneyDeserializer;
import asinoladro.api.serialization.MoneySerializer;

public class TransferConfirmation {
	@NotEmpty
    private final long transactionId;
	
	@NotEmpty
	private final Duration estimatedTransferDuration;

	@NotEmpty
	private final Money amountDebited;
	
	@NotEmpty
	private final Money amountCredited;
	
	@JsonCreator
    public TransferConfirmation(
	    		@JsonProperty("transactionId") long transactionId,
	    		@JsonProperty("estimatedTransferDuration") Duration estimatedTransferDuration,
	    		@JsonProperty("amountDebited") @JsonDeserialize(using = MoneyDeserializer.class)
	    		Money amountDebited,
	    		@JsonProperty("amountCredited") @JsonDeserialize(using = MoneyDeserializer.class)
	    		Money amountCredited) {
		this.transactionId = transactionId;
		this.estimatedTransferDuration = estimatedTransferDuration;
		this.amountDebited = amountDebited;
		this.amountCredited = amountCredited;
	}

	@JsonProperty @JsonSerialize(using = MoneySerializer.class)
    public Money getAmountDebited() {
		return amountDebited;
	}

	@JsonProperty @JsonSerialize(using = MoneySerializer.class)
    public Money getAmountCredited() {
		return amountCredited;
	}

	@JsonProperty
    public long getTransactionId() {
		return transactionId;
	}

	@JsonProperty @JsonSerialize(using = Iso8601DurationSerializer.class)
    public Duration getEstimatedTransferDuration() {
		return estimatedTransferDuration;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amountCredited == null) ? 0 : amountCredited.hashCode());
		result = prime * result + ((amountDebited == null) ? 0 : amountDebited.hashCode());
		result = prime * result + ((estimatedTransferDuration == null) ? 0 : estimatedTransferDuration.hashCode());
		result = prime * result + (int) (transactionId ^ (transactionId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransferConfirmation other = (TransferConfirmation) obj;
		if (amountCredited == null) {
			if (other.amountCredited != null)
				return false;
		} else if (!amountCredited.equals(other.amountCredited))
			return false;
		if (amountDebited == null) {
			if (other.amountDebited != null)
				return false;
		} else if (!amountDebited.equals(other.amountDebited))
			return false;
		if (estimatedTransferDuration == null) {
			if (other.estimatedTransferDuration != null)
				return false;
		} else if (!estimatedTransferDuration.equals(other.estimatedTransferDuration))
			return false;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TransferConfirmation [transactionId=" + transactionId + ", estimatedTransferDuration="
				+ estimatedTransferDuration + ", amountDebited=" + amountDebited + ", amountCredited=" + amountCredited
				+ "]";
	}
}
