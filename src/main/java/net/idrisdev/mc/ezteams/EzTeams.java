package net.idrisdev.mc.ezteams;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import net.idrisdev.mc.ezteams.commands.DebugDBCmd;
import net.idrisdev.mc.ezteams.commands.DebugDBUCmd;
import net.idrisdev.mc.ezteams.commands.TeamCommands;
import net.idrisdev.mc.ezteams.data.DataStorage;
import net.idrisdev.mc.ezteams.data.PlayerData;
import net.idrisdev.mc.ezteams.data.PlayerDataStorage;
import net.idrisdev.mc.ezteams.data.TeamData;
import net.idrisdev.mc.ezteams.events.ForgeEvents;
import net.idrisdev.mc.ezteams.events.PixelmonEvents;
import net.idrisdev.mc.ezteams.utils.ETUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static net.idrisdev.mc.ezteams.utils.ETUtils.handleJoin;
import static net.idrisdev.mc.ezteams.utils.ETUtils.names;
import static net.idrisdev.mc.ezteams.utils.ETUtils.sendSrcPlainMessage;

/**
 * Created by Idris_ on 06/10/2016.
 */

@Plugin(id = EzTeams.MODID,
        name = EzTeams.NAME,
        version = EzTeams.VERSION,
        authors = "Idris",
        dependencies = @Dependency(id = "pixelmon"))
public class EzTeams {
    public static final String VERSION = "1.0.0";
    public static final String NAME = "EzTeams";
    public static final String MODID = "ezteams";
    public static final boolean DEBUG = true;
    public static List<TeamData> teams = new ArrayList<>();
    public static List<PlayerData> players = new LinkedList<>();
    //shit for plugins
    private static EzTeams plugin;
    private TeamCommands tc;
    private CommandManager cmdSrvc = getGame().getCommandManager();
    private DataStorage ds;
    private PlayerDataStorage pds;

    @Inject
    private Logger logger;

    //DO NOT REMOVE - BREAKS THINGS!
    public static Game getGame() {
        return Sponge.getGame();
    }

    public static EzTeams get() {
        return plugin;
    }

    static void set(EzTeams value) {
        plugin = value;
    }

    public Logger getLogger() {
        return logger;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event){
        Pixelmon.EVENT_BUS.register(new PixelmonEvents());
        MinecraftForge.EVENT_BUS.register(new ForgeEvents());
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        //Send init message to logger!
        logger.info("EzTeams " + VERSION + " INITIALIZING!");
        set(this);

        //Init objects
        tc = new TeamCommands();
        ds = new DataStorage();
        pds = new PlayerDataStorage();

        //init filemanager
        ETUtils.fm.init();

        //init DataStorageConnection
        try { ds.initDS(); } catch (SQLException e) { logger.error("DATABASE INIT FAILED!"); e.printStackTrace(); }

        //Start Player Data Storage;
        try{ pds.initPDS(); } catch (SQLException e) { logger.error("PLAYER DATAINIT FAILED!"); e.printStackTrace(); }

        //Build and register commands
        tc.buildCommands();
        registerCommands();
    }

    @Listener
    public void onLoad(GameLoadCompleteEvent event) {
        logger.info("EzTeams " + VERSION + " DONE!");
    }

    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        Player ply = event.getTargetEntity();
        UUID plyID = ply.getUniqueId();
        String name = ply.getName();




    }



    private void registerCommands() {
        cmdSrvc.register(this, tc.teams, "teams", "team");
        cmdSrvc.register(this, tc.teamCredits, "teamcredits", "tcredits", "tcreds");
        cmdSrvc.register(this, tc.teamLeave, "leaveteam", "tleave", "tquit");
        cmdSrvc.register(this, tc.teamJoin, "jointeam", "tjoin");
        cmdSrvc.register(this, tc.teamsList, "listteams", "tlist");
        cmdSrvc.register(this, tc.teamPoints, "tpoints","points");

        if (DEBUG) {
            cmdSrvc.register(this, new DebugDBCmd(), "debugdb");
            cmdSrvc.register(this, new DebugDBUCmd(), "debugdbu");
        }

    }
}