package asinoladro.db;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.ws.rs.WebApplicationException;

import org.joda.money.CurrencyUnit;

public class ExchangeRateDaoWrapper implements ExchangeRateDao {
	
	private ExchangeRateDao wrapped;

	public ExchangeRateDaoWrapper(ExchangeRateDao wrapped) {
		this.wrapped = wrapped;
	}
	
	/**
	 * Bidirectional exchange rates.
	 */
	@Override
	public BigDecimal getExchangeRate(CurrencyUnit from, CurrencyUnit to) {
		CurrencyUnit lowerAlpha, higherAlpha;
    		
    		boolean invert;
    		if (from.getCurrencyCode().compareTo(to.getCurrencyCode()) <= 0) {
    			lowerAlpha = from;
    			higherAlpha = to;
    			invert = false;
    		}
    		else {
    			lowerAlpha = to;
    			higherAlpha = from;
    			invert = true;
    		}
    		
    		BigDecimal exchangeRate = wrapped.getExchangeRate(
    				lowerAlpha, higherAlpha);
    		
    		if (exchangeRate == null)
    			throw new WebApplicationException(
    					String.format("Exchange rate not found: {} to {}", lowerAlpha, higherAlpha));
    		
    		if (invert)
    			exchangeRate = BigDecimal.ONE.divide(exchangeRate, 2, RoundingMode.HALF_EVEN);
    		
    		return exchangeRate;
    }
}
