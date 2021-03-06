package net.technolords.micro.config;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

public class ConfigurationManagerTest {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Test
    public void testInitializationOfConfiguration() throws JAXBException, IOException, SAXException {
        ConfigurationManager configurationManager = new ConfigurationManager(null, null);
        Assert.assertNotNull(configurationManager, "Expected the configuration manager to be initialized...");
        LOGGER.debug("Test of initialization of config completed, no errors...");
    }

    @Test (enabled = false)
    public void testRandom() throws JAXBException, IOException, SAXException {
        ConfigurationManager configurationManager = new ConfigurationManager(null, null);
        for (int i = 0; i < 100; i++) {
            LOGGER.debug("Generated random: {}", configurationManager.generateRandom());
        }
    }
}