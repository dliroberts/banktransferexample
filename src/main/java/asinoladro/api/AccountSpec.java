package asinoladro.api;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.money.CurrencyUnit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public boolean matches(Account account) {
	    	if (!account.getIban().equals(iban))
				return false;
	    	if (!account.getBalance().getCurrencyUnit().equals(currency))
				return false;
		
	    	String otherFullName = account.getCustomer().getFullName();
	    	
	    	return fullNameFuzzyMatches(otherFullName);
    }
    
    private static String normaliseName(String name) {
    		return name.toUpperCase().replaceAll("[^A-Z]+", " ").trim();
    }

	private boolean fullNameFuzzyMatches(String otherFullName) {
		// fuzzy match on name
	    	
	    	String name = normaliseName(fullName);
	    	String otherName = normaliseName(otherFullName);
	    	
	    	// strip out any honorific titles
	    	// FIXME: this will only work for english honorifics! should be linked to country in which the
	    	// account is held, e.g. M., Mme. and Mlle. in france.
	    	for (Title title : Title.values()) {
	    		String t = title.toString() + " ";
	    		
	    		if (name.startsWith(t))
	    			name = name.substring(t.length());
	    		if (otherName.startsWith(t))
	    			otherName = otherName.substring(t.length());
	    	}
	    	
	    	if (name.equals(otherName))
	    		return true;
			
	    	// first initial in one, full name in the other?
	    	
	    	String[] n1tokens = name.split(" ");
	    	String[] n2tokens = otherName.split(" ");
	    	
	    	if (n1tokens.length == n2tokens.length) {
	    		boolean nonInitialFound = false;
	    		for (int i = 0; i < n1tokens.length; i++) {
	    			String n1token = n1tokens[i];
	    			String n2token = n2tokens[i];
	    			
	    			if (n1token.length() > 1)
						nonInitialFound = true;
					
	    			if (n1token.equals(n2token)) {
	    				continue;
	    			}
	    			
	    			String shorter, longer;
	    			if (n1token.length() < n2token.length()) {
	    				shorter = n1token;
	    				longer = n2token;
	    			}
	    			else {
	    				longer = n1token;
	    				shorter = n2token;
	    			}
	    			
	    			if (shorter.length() == 1) {
	    				if (longer.startsWith(shorter)) {
	    					continue;
	    				}
	    			}
	    			return false;
	    		}
	    		
	    		if (nonInitialFound)
	    			return true;
	    	}
	    	
	    	return false;
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
    public CurrencyUnit getCurrency() {
		return currency;
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

enum Title {
	MR, MRS, MISS, MX, PROF, LORD, LADY, REV, SIR, JUDGE, DR,
}
