package net.technolords.micro.config.jaxb.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.config.jaxb.script.Script;

/**
 * Created by BeroSlingBlade on 2016-10-14
 */
public class ScriptAdapter extends XmlAdapter<String, Script> {
	 private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Override
	public Script unmarshal(String scriptSource) throws Exception {
		LOGGER.debug("Compiling script: {}", scriptSource);
		return new Script(scriptSource);
	}

	@Override
	public String marshal(Script script) throws Exception {
		return script.getScriptSource();
	}
}
