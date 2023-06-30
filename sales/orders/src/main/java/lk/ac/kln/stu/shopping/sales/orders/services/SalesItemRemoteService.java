package lk.ac.kln.stu.shopping.sales.orders.services;

import lk.ac.kln.stu.shopping.sales.orders.models.SalesItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class SalesItemRemoteService {

    private static final String REMOTE_SERVICE_NAME = "SERVICE_SALES_ITEMS";

    private final DiscoveryClient discoveryClient;
    private final WebClient webClient;

    @Autowired
    public SalesItemRemoteService(DiscoveryClient discoveryClient, WebClient webClient) {
        this.discoveryClient = discoveryClient;
        this.webClient = webClient;
    }

    private String getServiceURL() {
        List<ServiceInstance> list = discoveryClient.getInstances(SalesItemRemoteService.REMOTE_SERVICE_NAME);

        if (list != null && list.size() > 0 ) {
            return list.get(0).getUri().toString() + "/sales/items";
        }

        return null;
    }

    public Optional<SalesItem> getSalesItemById(Long id) {
        try {
            String baseURL = this.getServiceURL();

            Mono<SalesItem> salesItemMono = this.webClient
                    .get()
                    .uri(baseURL + "/id=" + String.valueOf(id))
                    .retrieve()
                    .bodyToMono(SalesItem.class);

            return Optional.ofNullable(salesItemMono.block());
        }

        catch (WebClientResponseException webClientResponseException) {
            if (webClientResponseException.getRawStatusCode() == 404) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

}
