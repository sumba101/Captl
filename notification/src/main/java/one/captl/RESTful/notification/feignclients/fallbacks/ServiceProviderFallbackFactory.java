package one.captl.RESTful.notification.feignclients.fallbacks;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ServiceProviderFallbackFactory implements FallbackFactory<ServiceFallBack> {
    @Override
    public ServiceFallBack create(Throwable cause) {
        return new ServiceFallBack();
    }
}

