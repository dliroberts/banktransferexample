package asinoladro;

import com.fasterxml.jackson.databind.ObjectMapper;

import asinoladro.resources.BankTransferResource;
import io.dropwizard.Application;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class BankTransferRevolutApplication extends Application<BankTransferRevolutConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BankTransferRevolutApplication().run(args);
    }

    @Override
    public String getName() {
        return "BankTransferRevolut";
    }

    @Override
    public void initialize(final Bootstrap<BankTransferRevolutConfiguration> bootstrap) {
        
    }

    @Override
    public void run(final BankTransferRevolutConfiguration configuration,
                    final Environment environment) {
        final BankTransferResource resource = new BankTransferResource();
        
        ObjectMapper objectMapper = environment.getObjectMapper();
        
        JerseyEnvironment jersey = environment.jersey();
        jersey.register(resource);
    }

}
