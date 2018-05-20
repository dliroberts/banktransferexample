package asinoladro.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.Duration;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;

public class TransferConfirmationTest {
	private static final CurrencyUnit BRL = CurrencyUnit.getInstance("BRL");
	
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@Test
	public void test() throws Exception {
        final TransferConfirmation expected = new TransferConfirmation(
        		3L,
        		Duration.millis(5000),
        		Money.of(BRL, new BigDecimal("1.01")),
        		Money.of(CurrencyUnit.EUR, new BigDecimal("0.23"))
        	);
        
        final String actual = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/transferconfirmation-1.json"),
                		TransferConfirmation.class));

        assertThat(MAPPER.writeValueAsString(expected)).isEqualTo(actual);
	}

}
