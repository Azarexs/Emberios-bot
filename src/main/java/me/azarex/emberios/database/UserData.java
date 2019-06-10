package me.azarex.emberios.database;

import java.time.LocalDateTime;

public interface UserData {

    long getServerId();
    long getUserId();

    void setCoins(long amount);
    long getCoins();

    void setExp(long exp);
    long getExp();

    void setDailyTime(LocalDateTime localDateTime);
    LocalDateTime getDailyTime();

    void setCoinTime(LocalDateTime localDateTime);
    LocalDateTime getCoinTime();

    void setExpTime(LocalDateTime localDateTime);
    LocalDateTime getExpTime();


}
