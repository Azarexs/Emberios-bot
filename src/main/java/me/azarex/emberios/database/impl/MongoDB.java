package me.azarex.emberios.database.impl;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.azarex.emberios.database.GuildData;
import me.azarex.emberios.database.UserData;
import me.azarex.emberios.utility.DateUtility;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.util.Date;

public class MongoDB implements GuildData {

    private static final String DATABASE_URI = "mongodb+srv://azarex:Zd6xvfacWBIdMkuC@cluster0-9wcdt.mongodb.net/test";

    private final MongoCollection<Document> userCollection;
    private final MongoCollection<Document> guildCollection;

    public MongoDB() {
        MongoClientURI uriClient = new MongoClientURI(DATABASE_URI);
        MongoClient client = new MongoClient(uriClient);
        MongoDatabase mainDatabase = client.getDatabase("Main");

        userCollection = mainDatabase.getCollection("Users");
        guildCollection = mainDatabase.getCollection("Guilds");
    }

    @Override
    public void createGuildData(long serverId) {
        getGuildDocument(serverId);
    }

    @Override
    public void createUserData(long serverId, long userId) {
        getUserDocument(serverId, userId);
    }

    public UserData getUserData(long serverId, long userId) {
        Document queryDocument = getUserDocument(serverId, userId);

        return new UserData() {
            @Override
            public long getServerId() {
                return serverId;
            }

            @Override
            public long getUserId() {
                return userId;
            }

            @Override
            public void setCoins(long amount) {
                Bson updatedValue = new Document("coins", amount);
                Bson updateOperation = new Document("$set", updatedValue);

                userCollection.updateOne(queryDocument, updateOperation);
            }

            @Override
            public long getCoins() {
                return queryDocument.getLong("coins");
            }

            @Override
            public void setExp(long exp) {
                Bson updatedValue = new Document("exp", exp);
                Bson updateOperation = new Document("$set", updatedValue);

                userCollection.updateOne(queryDocument, updateOperation);
            }

            @Override
            public long getExp() {
                return queryDocument.getLong("exp");
            }

            @Override
            public void setDailyTime(LocalDateTime localDateTime) {
                Document queryDocument = getUserDocument(serverId, userId);

                Date date = DateUtility.fromDateTime(localDateTime);
                Bson updatedValue = new Document("dailyTime", date);
                Bson updateOperation = new Document("$set", updatedValue);

                userCollection.updateOne(queryDocument, updateOperation);
            }

            @Override
            public LocalDateTime getDailyTime() {
                return DateUtility.fromDate(queryDocument.getDate("dailyTime"));
            }

            @Override
            public void setCoinTime(LocalDateTime localDateTime) {
                Document queryDocument = getUserDocument(serverId, userId);
                Date dateToSet = DateUtility.fromDateTime(localDateTime);

                Bson updatedValue = new Document("coinTime", dateToSet);
                Bson updateOperation = new Document("$set", updatedValue);

                userCollection.updateOne(queryDocument, updateOperation);
            }

            @Override
            public LocalDateTime getCoinTime() {
                return DateUtility.fromDate(queryDocument.getDate("coinTime"));
            }

            @Override
            public void setExpTime(LocalDateTime localDateTime) {
                Document queryDocument = getUserDocument(serverId, userId);

                Date date = DateUtility.fromDateTime(localDateTime);
                Bson updatedValue = new Document("expTime", date);
                Bson updateOperation = new Document("$set", updatedValue);

                userCollection.updateOne(queryDocument, updateOperation);
            }

            @Override
            public LocalDateTime getExpTime() {
                return DateUtility.fromDate(queryDocument.getDate("expTime"));
            }
        };
    }

    @Override
    public void setPrefix(long serverId, String prefix) {
        Document queryDocument = getGuildDocument(serverId);

        Bson updatedValue = new Document("prefix", prefix);
        Bson updateOperation = new Document("$set", updatedValue);

        guildCollection.updateOne(queryDocument, updateOperation);
    }

    @Override
    public String getPrefix(long serverId) {
        return getGuildDocument(serverId).getString("prefix");
    }

    @Override
    public void setJoinLeaveChannel(long serverId, long channelId) {
        Document queryDocument = getGuildDocument(serverId);

        Bson updatedValue = new Document("join-leave-channel", channelId);
        Bson updateOperation = new Document("$set", updatedValue);

        guildCollection.updateOne(queryDocument, updateOperation);
    }

    @Override
    public long getJoinLeaveChannel(long serverId) {
        return getGuildDocument(serverId).getLong("join-leave-channel");
    }

    @Override
    public void setBotCommandsChannel(long serverId, long channelId) {
        Document queryDocument = getGuildDocument(serverId);

        Bson updatedValue = new Document("bot-command-channel", channelId);
        Bson updateOperation = new Document("$set", updatedValue);

        guildCollection.updateOne(queryDocument, updateOperation);
    }

    @Override
    public long getBotCommandsChannel(long serverId) {
        return getGuildDocument(serverId).getLong("bot-command-channel");
    }

    private Document getUserDocument(long serverId, long userId) {
        Document queryDocument = new Document()
                .append("serverId", serverId)
                .append("userId", userId);

        Document foundDocument = userCollection.find(queryDocument).first();

        if (foundDocument == null) {
            userCollection.insertOne(queryDocument
                    .append("coins", 0L)
                    .append("exp", 0L)
                    .append("dailyTime", DateUtility.fromDateTime(LocalDateTime.now()))
                    .append("coinTime", DateUtility.fromDateTime(LocalDateTime.now()))
                    .append("expTime", DateUtility.fromDateTime(LocalDateTime.now())));
        } else {
            queryDocument
                    .append("coins", foundDocument.getLong("coins"))
                    .append("exp", foundDocument.getLong("exp"))
                    .append("dailyTime", foundDocument.getDate("dailyTime"))
                    .append("coinTime", foundDocument.getDate("coinTime"))
                    .append("expTime", foundDocument.getDate("expTime"));
        }

        return queryDocument;
    }

    private Document getGuildDocument(long serverId) {
        Document queryDocument = new Document()
                .append("serverId", serverId);

        Document found = guildCollection.find(queryDocument).first();

        if (found == null) {
            guildCollection.insertOne(queryDocument
                    .append("prefix", "!")
                    .append("join-leave-channel", 0L)
                    .append("bot-command-channel", 0L));
        } else {
            queryDocument
                    .append("prefix", found.getString("prefix"))
                    .append("join-leave-channel", found.getLong("join-leave-channel"))
                    .append("bot-command-channel", found.getLong("bot-command-channel"));
        }
        return queryDocument;
    }


}
