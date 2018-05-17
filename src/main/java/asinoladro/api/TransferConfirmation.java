package asinoladro.api;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.Duration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import asinoladro.api.serialization.Iso8601DurationSerializer;

public class TransferConfirmation {
	@NotEmpty
    private final long transactionId;
	
	@NotEmpty
	private final Duration estimatedTransferDuration;

	public TransferConfirmation(long transactionId, Duration estimatedTransferDuration) {
		this.transactionId = transactionId;
		this.estimatedTransferDuration = estimatedTransferDuration;
	}

	@JsonProperty
    public long getTransactionId() {
		return transactionId;
	}

	@JsonProperty @JsonSerialize(using = Iso8601DurationSerializer.class)
    public Duration getEstimatedTransferDuration() {
		return estimatedTransferDuration;
	}
}
