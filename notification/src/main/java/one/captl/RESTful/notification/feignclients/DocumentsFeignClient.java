package one.captl.RESTful.notification.feignclients;

import one.captl.RESTful.notification.feignclients.fallbacks.DocumentFallbackFactory;
import one.captl.RESTful.notification.model.enums.DocumentType;
import one.captl.RESTful.notification.model.enums.IdType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "document-service", fallbackFactory = DocumentFallbackFactory.class)
public interface DocumentsFeignClient {
    @PostMapping(value = "/document",produces = "application/json")
    long initiateDocContainer(@RequestParam("idType") IdType idType, @RequestParam("accessId") Long accessId, @RequestParam("documentType") DocumentType documentType, @RequestParam("fromId") long fromId,
                              @RequestParam("writeIds") List<Long> writeIds, @RequestParam("readIds") List<Long> readIds);

}
