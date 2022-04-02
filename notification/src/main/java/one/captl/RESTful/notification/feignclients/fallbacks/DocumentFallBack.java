package one.captl.RESTful.notification.feignclients.fallbacks;

import one.captl.RESTful.notification.feignclients.DocumentsFeignClient;
import one.captl.RESTful.notification.model.enums.DocumentType;
import one.captl.RESTful.notification.model.enums.IdType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentFallBack implements DocumentsFeignClient {
    @Override
    public long initiateDocContainer(IdType idType, Long accessId, DocumentType documentType, long fromId, List<Long> writeIds, List<Long> readIds) {
        return -1;
    }
}
