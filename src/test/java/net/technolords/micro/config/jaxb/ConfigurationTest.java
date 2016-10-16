package net.technolords.micro.config.jaxb;

import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by BeroSlingBlade on 2016-Jul-21.
 */
public class ConfigurationTest {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    private final String CONFIGURATION_UNMARSHAL = "data/mockConfigurations/configuration-test2.xml";
    
    @Test (enabled = false)
    public void testGroovyScriptMarshalling() {
    	
    }
    
    @Test
    public void testGroovyScriptUnmarshalling() throws JAXBException, IOException {
        LOGGER.debug("About to initialize the configuration...");
        InputStream inputStreamForConfig = this.getClass().getClassLoader().getResourceAsStream(CONFIGURATION_UNMARSHAL);
        Unmarshaller unmarshaller = JAXBContext.newInstance(Configurations.class).createUnmarshaller();
        Configurations configurations = (Configurations) unmarshaller.unmarshal(inputStreamForConfig);
    	GroovyObject groovy = configurations.getConfigurations().get(0).getScript().getGroovy();
    	Assert.assertNotNull(groovy, "Expected the groovy script to be compiled and present..");
    	String output = (String) groovy.invokeMethod("testMethod", new Object[] { "BOE" });
    	Assert.assertEquals(output, "OOTE OOTE BOE", "Expected the groovy script to produce the correct output..");
    	LOGGER.debug("Test of unmarshalling of configuration completed, no errors...");
    }
}