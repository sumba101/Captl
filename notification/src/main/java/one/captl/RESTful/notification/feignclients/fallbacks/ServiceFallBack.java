package one.captl.RESTful.notification.feignclients.fallbacks;

import one.captl.RESTful.notification.feignclients.ServiceProviderFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceFallBack implements ServiceProviderFeignClient {

    @Override
    public List<Long> getSPTeam(long id) {
        return null;
    }
}
