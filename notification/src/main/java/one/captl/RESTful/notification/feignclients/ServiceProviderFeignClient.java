package one.captl.RESTful.notification.feignclients;

import one.captl.RESTful.notification.feignclients.fallbacks.ServiceProviderFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "serviceProvider-service", fallbackFactory = ServiceProviderFallbackFactory.class)
public interface ServiceProviderFeignClient {
    @GetMapping(value = "/service-provider/{id}/team",produces = "application/json")
    List<Long> getSPTeam(@PathVariable long id);

}
