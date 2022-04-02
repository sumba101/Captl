package one.captl.RESTful.notification.model.classes;

import lombok.Data;
import one.captl.RESTful.notification.model.enums.InfoFieldType;

@Data
public class Info {
    private InfoFieldType infoFieldType;
    private String value;
}
