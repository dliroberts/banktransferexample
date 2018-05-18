package asinoladro.api;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.money.CurrencyUnit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import asinoladro.api.serialization.CurrencyUnitDeserializer;
import asinoladro.api.serialization.CurrencyUnitSerializer;

public class AccountSpec {
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
    public AccountSpec(
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

	@Override
	public String toString() {
		return "AccountSpec [iban=" + iban + ", fullName=" + fullName + ", currency=" + currency + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((iban == null) ? 0 : iban.hashCode());
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
		AccountSpec other = (AccountSpec) obj;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (iban == null) {
			if (other.iban != null)
				return false;
		} else if (!iban.equals(other.iban))
			return false;
		return true;
	}
}
