package net.idrisdev.mc.ezteams.data;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.utils.ETUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.mutable.entity.InvisibilityData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.service.user.UserStorageService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Idris on 9/10/2016.
 */
public class PlayerDataStorage extends DataStorage {
    public PlayerDataStorage(){
        super();
    }
    private static final String playerTable = "PLAYERDATA";
    private static final String teamTable = "TEAMS";
    private static Statement st;
    private static Connection conn;
    private ResultSet rs;
    private static List<PlayerData> tmpPlys = new ArrayList<>();

    public void initPDS() throws SQLException {
        logInitMsg("PlayerDataStorage");
        logCompMsg("PlayerDataStorage");
    }

    public Optional<User> getUser(UUID uuid){
        Optional<Player> onlinePlayer = Sponge.getServer().getPlayer(uuid);
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);

        return userStorage.get().get(uuid);
    }



    public static Optional<Player> getOnlinePlayer(UUID id){
        Optional<Player> online = EzTeams.getGame().getServer().getPlayer(id);
        if(online.isPresent())
            return online;
        else
            return null;
    }
}
