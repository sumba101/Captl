package one.captl.RESTful.notification.model.classes;

import lombok.Data;
import one.captl.RESTful.notification.model.enums.Freq;

import java.time.LocalDateTime;

@Data
public class Frequencies {
    private LocalDateTime localDateTime;
    private Freq freq;

    public void nextSet(){
        if (freq==null)
            return;

        switch (freq){
            case Half -> localDateTime=localDateTime.plusMonths(6);
            case None -> localDateTime=null;
            case Week -> localDateTime=localDateTime.plusWeeks(1);
            case Daily -> localDateTime=localDateTime.plusDays(1);
            case Quarter -> localDateTime=localDateTime.plusMonths(3);
            case Year -> localDateTime=localDateTime.plusYears(1);
        }
    }
    public void backSet(){
        if (freq==null)
            return;

        switch (freq){
            case Half -> localDateTime=localDateTime.minusMonths(6);
            case None -> localDateTime=null;
            case Week -> localDateTime=localDateTime.minusWeeks(1);
            case Daily -> localDateTime=localDateTime.minusDays(1);
            case Quarter -> localDateTime=localDateTime.minusMonths(3);
            case Year -> localDateTime=localDateTime.minusYears(1);
        }

    }
}
