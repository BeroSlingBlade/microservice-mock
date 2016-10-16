package net.technolords.micro.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.ResponseContext;
import net.technolords.micro.config.ConfigurationManager;

/**
 * Created by BeroSlingBlade on 2016-Jul-20.
 */
public class CommandProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String CONTENT_TYPE = "Content-Type";
    private ConfigurationManager configurationManager = null;

    public CommandProcessor(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    /**
     * Processor that calls the configuration manager to find a response, based URI as well as request type
     * GET or POST (and a message when it was a POST).
     *
     * @param exchange
     *  The exchange associated with the request.
     * @throws Exception
     *  When the configuration manager has an error.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
    	exchange.getIn().setBody("I'm sorry Dave, I'm afraid I can't do that..." );
    	exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
        LOGGER.debug("Finished processing command");

    }

}
