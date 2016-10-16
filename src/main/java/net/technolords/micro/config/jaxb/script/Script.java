package net.technolords.micro.config.jaxb.script;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.technolords.micro.config.jaxb.adapter.ScriptAdapter;

/**
 * Created by BeroSlingBlade on 2016-10-14
 */
@XmlJavaTypeAdapter(ScriptAdapter.class)
public class Script {
    private GroovyObject groovy;
    private final GroovyClassLoader classLoader = new GroovyClassLoader();
    private String scriptSource;
    private String language;

    @SuppressWarnings("unchecked")
    public Script(String scriptSource) throws InstantiationException, IllegalAccessException {
    	this.scriptSource = scriptSource;
		Class<GroovyObject> clazz = classLoader.parseClass(scriptSource);
        this.groovy = clazz.newInstance();
    }
    
    public String getScriptSource() {
    	return this.scriptSource;
    }
    
    public GroovyObject getGroovy() {
    	return this.groovy;
    }
    
    @XmlAttribute
    public String getLanguage() {
    	return this.language;
    }
    
    public void setLanguage(String language) {
    	this.language = language;
    }
}

