package one.captl.RESTful.notification.model.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Pairing<T, T1> {
    private T left;
    private T1 right;

    public static Pairing<Integer, BigDecimal> of(int sharesNumber, BigDecimal amountNumber) {
        return new Pairing<>(sharesNumber,amountNumber);
    }

    public static Pairing<Long, MutableTriple<String, Integer, BigDecimal>> of(Long fundFirmId, MutableTriple<String, Integer, BigDecimal> allotment) {
        return new Pairing<>(fundFirmId,allotment);
    }
}
