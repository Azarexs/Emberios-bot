package me.azarex.emberios.database;

public interface GuildData {

    void createGuildData(long serverId);

    void createUserData(long serverId, long userId);
    UserData getUserData(long serverId, long userId);

    void setPrefix(long serverId, String prefix);
    String getPrefix(long serverId);

    void setJoinLeaveChannel(long serverId, long channelId);
    long getJoinLeaveChannel(long serverId);

    void setBotCommandsChannel(long serverId, long channelId);
    long getBotCommandsChannel(long serverId);

}
