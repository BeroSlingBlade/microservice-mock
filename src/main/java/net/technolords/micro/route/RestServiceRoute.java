package net.technolords.micro.route;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.config.ConfigurationManager;
import net.technolords.micro.processor.CommandProcessor;
import net.technolords.micro.processor.ResponseProcessor;

/**
 * Created by Technolords on 2016-Jun-23.
 */
public class RestServiceRoute extends RouteBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String DIRECT_COMMAND = "direct:command";
    private static final String DIRECT_RESPONSE = "direct:response";
    private static final String JETTY_BINDING_ADDRESS = "0.0.0.0";
    private String port = null;
    private Processor responseProcessor = null;
    private Processor commandProcessor = null;

    public RestServiceRoute(String myPort, ConfigurationManager configurationManager) {
        this.port = myPort;
        this.responseProcessor = new ResponseProcessor(configurationManager);
        this.commandProcessor = new CommandProcessor(configurationManager);
        LOGGER.info("Using port: " + this.port);
    }

    /**
     * Generates a Camel route, that listens from any HTTP request made (GET or POST) regardless
     * of the path. The response resolution is delegated towards the response processor.
     */
    @Override
    public void configure() throws Exception {
        onException(Exception.class)
            .handled(true)
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
            .transform(simple("An error occurred: ${exception.message}"));
        
        restConfiguration()
        	.component("jetty")
        	.host(JETTY_BINDING_ADDRESS)
        	.port(this.port)
        	.endpointProperty("matchOnUriPrefix", "true")
        	.bindingMode(RestBindingMode.off)
            .dataFormatProperty("prettyPrint", "true");
        
        rest("/cmd")
        	.get("/config").to(DIRECT_COMMAND);

        rest()
        	.get("/").produces("application/json").to(DIRECT_RESPONSE)
        	.post("/").to(DIRECT_RESPONSE);
                
        from(DIRECT_RESPONSE)
        	.log(LoggingLevel.DEBUG, LOGGER, "Current headers: ${headers}")
        	.log(LoggingLevel.DEBUG, LOGGER, "Current body: ${body}")
        	.process(this.responseProcessor);
        
        from(DIRECT_COMMAND)
    		.log(LoggingLevel.DEBUG, LOGGER, "Current headers: ${headers}")
    		.log(LoggingLevel.DEBUG, LOGGER, "Current body: ${body}")
    		.process(this.commandProcessor);
    }
}
