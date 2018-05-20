package asinoladro.api;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;

public class TransferRequestTest {
	private static final CurrencyUnit BRL = CurrencyUnit.getInstance("BRL");
	
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@Test
	public void deserializesFromJson() throws Exception {
        TransferRequest actual = new TransferRequest(
        		Money.of(BRL, new BigDecimal("1.01")),
        		new AccountSpec("BR8712345678123451234567890C1", "prof r. da Silva", BRL),
        		new AccountSpec("FR9476231310567227640169067", "mr Duncan    RobertS ", CurrencyUnit.EUR)
        	);

        TransferRequest expected = MAPPER.readValue(fixture("fixtures/transferrequest-1.json"), TransferRequest.class);

        assertThat(actual).isEqualTo(expected);
	}

}
