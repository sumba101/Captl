package one.captl.RESTful.notification.model.classes;

import lombok.Data;
import one.captl.RESTful.notification.model.enums.BundledInfoType;
import java.util.HashMap;
import java.util.List;

@Data
public class InfoFilling {
    private List<Info> details;
    private List<HashMap<BundledInfoType,String>> bundledDetails; // For vintage lp and portfolio network addition
}
