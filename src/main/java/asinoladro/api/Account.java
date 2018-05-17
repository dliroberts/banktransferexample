package asinoladro.api;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import asinoladro.api.serialization.CurrencyUnitDeserializer;
import asinoladro.api.serialization.CurrencyUnitSerializer;

public class Account {
	/**
	 * Made up of:
	 * 2-char country code (ISO 3166-1 alpha-2)
	 * 2-char check digits
	 * Up to 30 alphanumeric characters (length and details country-specific)
	 * 
	 * https://en.wikipedia.org/wiki/International_Bank_Account_Number
	 */
    @Length(min = 6, max = 34)
    @NotEmpty
	private final String iban;
	
	@NotEmpty
    private final String fullName;
    
    @NotEmpty
    private final CurrencyUnit currency;

    @JsonCreator
    public Account(
    			@JsonProperty("iban") String iban,
    			@JsonProperty("fullName") String fullName,
    			@JsonProperty("currency") @JsonDeserialize(using = CurrencyUnitDeserializer.class) CurrencyUnit currency
    	) {
		this.iban = iban;
		this.fullName = fullName;
		this.currency = currency;
	}

	@JsonProperty
    public String getIban() {
    		return iban;
    }

	@JsonProperty
    public String getFullName() {
		return fullName;
	}

	@JsonProperty @JsonSerialize(using = CurrencyUnitSerializer.class) 
    public String getCurrencyCode() {
		return fullName;
	}

	@JsonIgnore
	public boolean isRevolutAccount() {
		// FIXME this:
		// a. might not be true of all accounts in all currencies
		// b. might be a false positive in some countries - e.g. a local bank with a similar name
		return iban.length() > 8 && iban.substring(4, 8).equals("REVO");
	}
}
