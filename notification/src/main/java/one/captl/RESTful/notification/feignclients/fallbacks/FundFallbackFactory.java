package one.captl.RESTful.notification.feignclients.fallbacks;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class FundFallbackFactory implements FallbackFactory<FundFallBack> {
    @Override
    public FundFallBack create(Throwable cause) {
        return new FundFallBack();
    }
}
