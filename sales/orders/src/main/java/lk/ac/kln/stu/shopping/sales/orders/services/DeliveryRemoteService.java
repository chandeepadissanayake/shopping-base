package lk.ac.kln.stu.shopping.sales.orders.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ac.kln.stu.shopping.sales.orders.models.SalesItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeliveryRemoteService {

    private static final String REMOTE_SERVICE_NAME = "SERVICE_DELIVERY";

    private final DiscoveryClient discoveryClient;
    private final WebClient webClient;

    @Autowired
    public DeliveryRemoteService(DiscoveryClient discoveryClient, WebClient webClient) {
        this.discoveryClient = discoveryClient;
        this.webClient = webClient;
    }

    private String getServiceURL() {
        List<ServiceInstance> list = discoveryClient.getInstances(DeliveryRemoteService.REMOTE_SERVICE_NAME);

        if (list != null && list.size() > 0 ) {
            return list.get(0).getUri().toString() + "/delivery";
        }

        return null;
    }

    public Optional<Map<String, String>> requestDelivery(String deliveryMode) {
        try {
            String baseURL = this.getServiceURL();

            Map<String, String> requestData = new HashMap<>();
            requestData.put("deliveryMode", deliveryMode);

            Mono<Map<String, String>> deliveryServiceMono = this.webClient
                    .post()
                    .uri(baseURL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(new ObjectMapper().writeValueAsString(requestData)))
                    .retrieve()
                    .bodyToMono(ParameterizedTypeReference.forType(Map.class));

            return Optional.ofNullable(deliveryServiceMono.block());
        }

        catch (WebClientResponseException webClientResponseException) {
            if (webClientResponseException.getRawStatusCode() == 404) {
                return Optional.empty();
            }
        } catch (JsonProcessingException e) {
            // Won't happen as our JSON is prepared by ourselves.
            return Optional.empty();
        }

        return Optional.empty();
    }

}
