package com.ylab.walletservice.configuration.utils;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Custom property source factory for loading configuration from YAML files.
 */
public class YamlFactory extends DefaultPropertySourceFactory {
    /**
     * Creates a PropertySource for the specified YAML resource.
     *
     * @param name     The name of the property source.
     * @param resource YAML resource.
     * @return PropertySource from YAML.
     * @throws RuntimeException If failed to load properties from YAML.
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
        try {
            Properties propertiesFromYaml = loadYamlIntoProperties(resource);
            String sourceName = name != null ? name : determineSourceName(resource);
            return new PropertiesPropertySource(Objects.requireNonNull(sourceName), propertiesFromYaml);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from YAML: " + resource.getResource(), e);
        }
    }

    /**
     * Loads properties from the specified YAML resource into a Properties.
     *
     * @param resource The YAML resource to be loaded.
     * @return Properties from the YAML.
     * @throws IOException If an I/O error occurs.
     */
    private Properties loadYamlIntoProperties(EncodedResource resource) throws IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    /**
     * Determines the name of the property source based on the YAML resource.
     *
     * @param resource The YAML resource.
     * @return The name of the property source.
     */
    private String determineSourceName(EncodedResource resource) {
        return resource.getResource().getFilename();
    }
}