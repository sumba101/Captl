package one.captl.RESTful.notification.feignclients.fallbacks;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DocumentFallbackFactory implements FallbackFactory<DocumentFallBack> {
    @Override
    public DocumentFallBack create(Throwable cause) {
        return new DocumentFallBack();
    }
}
