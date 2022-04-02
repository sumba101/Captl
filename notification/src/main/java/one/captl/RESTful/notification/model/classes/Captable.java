package one.captl.RESTful.notification.model.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class Captable {
    private long vintageId;

    private List<String> shareTypes; // This is entered in the beginning and is used as the type set

    private String currency; // Provide iso 4217 code

    private HashMap<String, Pairing<Integer, BigDecimal>> initialSetting; // Type-> No.ofShares,Amount

    private Integer totalNumberOfShares;

    private BigDecimal totalAmount;

    private HashMap<Long, MutableTriple<String,Integer, BigDecimal>> gps;

    private HashMap<Long,MutableTriple<String,Integer,BigDecimal>> anchorLps;

    private HashMap<Long,MutableTriple<String,Integer,BigDecimal>> lps;

    private HashMap<Long,MutableTriple<String,Integer, BigDecimal>> trustees;

    private Pairing<Long, MutableTriple<String,Integer,BigDecimal>> fundFirms; // // Type, No.ofShares,Amount
}

//Todo: Note This is not the exact object and captable, when handling delegation acceptance ya have to do accordingly