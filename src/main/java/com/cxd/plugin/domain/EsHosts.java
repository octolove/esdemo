package com.cxd.plugin.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "elasticsearch")
public class EsHosts {
    private List<EsHost> esHost;

    public List<EsHost> getEsHost() {
        return esHost;
    }

    public void setEsHost(List<EsHost> esHost) {
        this.esHost = esHost;
    }

    @Override
    public String toString() {
        return "EsHosts{" +
                "esHost=" + esHost +
                '}';
    }
}
