package com.ambry.riskmodels;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.BaseRisk;
import com.objex.risk.beans.charts.ChartVO;
import com.objex.risk.beans.uno.CombinedResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Utility class for calling risk service endpoints.
 * 
 * @deprecated This class is deprecated. Tests should use MockMvc instead for better integration testing.
 * This class is kept for backward compatibility and legacy test support.
 */
@Deprecated
public class UnoJerseyRiskCaller {

    private static final Logger log = LoggerFactory.getLogger(UnoJerseyRiskCaller.class);

    /**
     * Call service with BaseRisk and return BaseResult
     * 
     * @param risk The risk object to send
     * @param uri The base URI of the service
     * @param path The path endpoint
     * @param resultClass The expected result class
     * @return The result object
     * @throws RuntimeException if the service call fails
     */
    @SuppressWarnings("unchecked")
    public static BaseResult callService(BaseRisk risk, String uri, String path, Class<?> resultClass) {
        try {
            Client client = ClientBuilder.newClient();
            Response resp = client.target(uri).path(path).request().post(Entity.entity(risk, MediaType.APPLICATION_JSON));
            
            if (resp.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new RuntimeException("Service call failed with status: " + resp.getStatus());
            }
            
            BaseResult result = (BaseResult) resp.readEntity(resultClass);
            return result;
        } catch (Exception e) {
            log.error("Error calling service at {}: {}", uri + path, e.getMessage(), e);
            throw new RuntimeException("Failed to call service: " + uri + path, e);
        }
    }

    /**
     * Call service with BaseRisk and return BaseResult with custom media type
     * 
     * @param risk The risk object to send
     * @param uri The base URI of the service
     * @param path The path endpoint
     * @param resultClass The expected result class
     * @param mediaType The media type to use
     * @return The result object
     * @throws RuntimeException if the service call fails
     */
    @SuppressWarnings("unchecked")
    public static BaseResult callService(BaseRisk risk, String uri, String path, Class<?> resultClass, String mediaType) {
        try {
            Client client = ClientBuilder.newClient();
            Response resp = client.target(uri).path(path).request().accept(mediaType).post(Entity.entity(risk, mediaType));
            
            if (resp.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new RuntimeException("Service call failed with status: " + resp.getStatus());
            }
            
            BaseResult result = (BaseResult) resp.readEntity(resultClass);
            return result;
        } catch (Exception e) {
            log.error("Error calling service at {}: {}", uri + path, e.getMessage(), e);
            throw new RuntimeException("Failed to call service: " + uri + path, e);
        }
    }

    /**
     * Call service with BaseRisk and return CombinedResult using RestClient
     * 
     * @param risk The risk object to send
     * @param uri The base URI of the service
     * @param path The path endpoint
     * @param mediaType The media type to use
     * @return The CombinedResult object
     * @throws RuntimeException if the service call fails
     */
    public static CombinedResult callService(BaseRisk risk, String uri, String path, String mediaType) {
        try {
            RestClient defaultClient = RestClient.create();
            
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(risk);
            log.debug("Calling service at {} with JSON: {}", uri + path, json);

            ResponseEntity<CombinedResult> response = defaultClient.post()
                    .uri(uri + path)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(risk)
                    .retrieve()
                    .toEntity(CombinedResult.class);

            CombinedResult result = response.getBody();
            if (result == null) {
                throw new RuntimeException("Service returned null response");
            }
            return result;
        } catch (JsonProcessingException e) {
            log.error("Error serializing risk object: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to serialize risk object", e);
        } catch (RestClientException e) {
            log.error("Error calling service at {}: {}", uri + path, e.getMessage(), e);
            throw new RuntimeException("Failed to call service: " + uri + path, e);
        }
    }

    /**
     * Call service with BaseResult and return BaseResult
     * 
     * @param inresult The result object to send
     * @param uri The base URI of the service
     * @param path The path endpoint
     * @param resultClass The expected result class
     * @return The result object
     * @throws RuntimeException if the service call fails
     */
    @SuppressWarnings("unchecked")
    public static BaseResult callService(BaseResult inresult, String uri, String path, Class<?> resultClass) {
        try {
            Client client = ClientBuilder.newClient();
            Response resp = client.target(uri).path(path).request().post(Entity.entity(inresult, MediaType.APPLICATION_JSON));
            
            if (resp.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new RuntimeException("Service call failed with status: " + resp.getStatus());
            }
            
            BaseResult result = (BaseResult) resp.readEntity(resultClass);
            return result;
        } catch (Exception e) {
            log.error("Error calling service at {}: {}", uri + path, e.getMessage(), e);
            throw new RuntimeException("Failed to call service: " + uri + path, e);
        }
    }

    /**
     * Call service with ChartVO and return BaseResult
     * 
     * @param chart The chart object to send
     * @param uri The base URI of the service
     * @param path The path endpoint
     * @param resultClass The expected result class
     * @return The result object
     * @throws RuntimeException if the service call fails
     */
    @SuppressWarnings("unchecked")
    public static BaseResult callService(ChartVO chart, String uri, String path, Class<?> resultClass) {
        try {
            Client client = ClientBuilder.newClient();
            Response resp = client.target(uri).path(path).request().post(Entity.entity(chart, MediaType.APPLICATION_JSON));
            
            if (resp.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new RuntimeException("Service call failed with status: " + resp.getStatus());
            }
            
            BaseResult result = (BaseResult) resp.readEntity(resultClass);
            return result;
        } catch (Exception e) {
            log.error("Error calling service at {}: {}", uri + path, e.getMessage(), e);
            throw new RuntimeException("Failed to call service: " + uri + path, e);
        }
    }

    /**
     * Call service with ChartVO and return BaseResult with custom media type
     * 
     * @param chart The chart object to send
     * @param uri The base URI of the service
     * @param path The path endpoint
     * @param resultClass The expected result class
     * @param mediaType The media type to use
     * @return The result object
     * @throws RuntimeException if the service call fails
     */
    @SuppressWarnings("unchecked")
    public static BaseResult callService(ChartVO chart, String uri, String path, Class<?> resultClass, MediaType mediaType) {
        try {
            Client client = ClientBuilder.newClient();
            Response resp = client.target(uri).path(path).request().post(Entity.entity(chart, mediaType));
            
            if (resp.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new RuntimeException("Service call failed with status: " + resp.getStatus());
            }
            
            BaseResult result = (BaseResult) resp.readEntity(resultClass);
            return result;
        } catch (Exception e) {
            log.error("Error calling service at {}: {}", uri + path, e.getMessage(), e);
            throw new RuntimeException("Failed to call service: " + uri + path, e);
        }
    }

    /**
     * Create a Risk object from a file
     * 
     * @param fileName The file path
     * @param clazz The class of the risk object
     * @param <T> The type extending BaseRisk
     * @return The risk object, or null if an error occurs
     */
    public static <T extends BaseRisk> T createRisk(String fileName, Class<T> clazz) {
        try {
            return Utils.createRiskFromFile(fileName, clazz);
        } catch (Exception ex) {
            log.error("Error creating risk from file {}: {}", fileName, ex.getMessage(), ex);
            return null;
        }
    }
}
