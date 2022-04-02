package one.captl.RESTful.notification.feignclients;

import one.captl.RESTful.notification.feignclients.fallbacks.FundFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "fund-service", fallbackFactory = FundFallbackFactory.class)
public interface VintageFeignClient {
    @GetMapping(value = "/portfolio-round/{id}/team/access", produces = "application/json")
    List<Long> getPortfolioRoundTeamWithAccessId(@PathVariable long id);
}
