package me.azarex.emberios.utility;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateUtility {

    public static Date fromDateTime(LocalDateTime date) {
        ZonedDateTime zoned = date.atZone(ZoneId.systemDefault());
        return Date.from(zoned.toInstant());
    }

    public static LocalDateTime fromDate(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

}
