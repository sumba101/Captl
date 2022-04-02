package one.captl.RESTful.notification.feignclients.fallbacks;

import one.captl.RESTful.notification.feignclients.VintageFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FundFallBack implements VintageFeignClient {
    @Override
    public List<Long> getPortfolioRoundTeamWithAccessId(long id) {
        return null;
    }
}
