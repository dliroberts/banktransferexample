package asinoladro;

import com.fasterxml.jackson.databind.ObjectMapper;

import asinoladro.resources.BankTransferResource;
import io.dropwizard.Application;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class BankTransferExampleApplication extends Application<BankTransferExampleConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BankTransferExampleApplication().run(args);
    }

    @Override
    public String getName() {
        return "BankTransferRevolut";
    }

    @Override
    public void initialize(final Bootstrap<BankTransferExampleConfiguration> bootstrap) {
        
    }

    @Override
    public void run(final BankTransferExampleConfiguration configuration,
                    final Environment environment) {
        final BankTransferResource resource = new BankTransferResource();
        
        ObjectMapper objectMapper = environment.getObjectMapper();
        
        JerseyEnvironment jersey = environment.jersey();
        jersey.register(resource);
    }

}
