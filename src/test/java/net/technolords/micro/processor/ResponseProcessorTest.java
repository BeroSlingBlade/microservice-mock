package net.technolords.micro.processor;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import net.technolords.micro.TestSupport;
import net.technolords.micro.config.ConfigurationManager;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResponseProcessorTest extends TestSupport{
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String DATASET_FOR_CONFIGURATIONS = "dataSetMockExpectation";

    @DataProvider(name = DATASET_FOR_CONFIGURATIONS)
    public Object[][] dataSetMock(){
        return new Object[][] {
            { "configuration-test1.xml", ConfigurationManager.HTTP_POST, "/mock/post", "sample-post1-request.xml", "sample-post1.txt"},
            { "configuration-test1.xml", ConfigurationManager.HTTP_GET, "/mock/get", null, "sample-get.txt"}
        };
    }

    @Test(dataProvider = DATASET_FOR_CONFIGURATIONS, enabled = true)
    public void testPostResponses(String configFile, String method, String uri, String body, String expectedResponse) throws Exception {
        // Create a path to the file
        Path pathToConfigFile = FileSystems.getDefault().getPath(getPathToDataFolder() + File.separator + "mockConfigurations" + File.separator);
        System.out.println(pathToConfigFile.toString());
        Path pathToResponseFile = FileSystems.getDefault().getPath(getPathToDataFolder() + File.separator + "mockResponses");
        System.out.println(pathToResponseFile.toString());
        String responseContent = new String(Files.readAllBytes(Paths.get(pathToResponseFile + File.separator + expectedResponse)));
        System.out.println(responseContent);
        String pathToConfig = pathToConfigFile + File.separator +configFile;
        System.out.println(pathToConfig.toString());

        Exchange exchange = this.generateExchange(method, uri, body);
        ConfigurationManager configurationManager = new ConfigurationManager(pathToConfig, null);
        ResponseProcessor responseProcessor = new ResponseProcessor(configurationManager);
        responseProcessor.process(exchange);

        //Assertions
        String actualResponse = exchange.getIn().getBody(String.class);
        AssertJUnit.assertEquals(actualResponse, responseContent);
    }

    private Exchange generateExchange(String method, String uri, String body) throws IOException {
        Path pathToRequestFile = FileSystems.getDefault().getPath(getPathToDataFolder() + File.separator + "mockRequests");
        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        exchange.getIn().setHeader(Exchange.HTTP_METHOD, method);
        exchange.getIn().setHeader(Exchange.HTTP_URI, uri);
        if (body != null) {
            String requestContent = new String(Files.readAllBytes(Paths.get(pathToRequestFile + File.separator + body)));
            System.out.println(requestContent);
            exchange.getIn().setBody(requestContent);
        }
        return exchange;
    }

}