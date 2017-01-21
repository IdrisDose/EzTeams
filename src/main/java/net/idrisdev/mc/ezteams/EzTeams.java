package net.idrisdev.mc.ezteams;

import com.google.inject.Inject;
import net.idrisdev.mc.ezteams.commands.DebugDBCmd;
import net.idrisdev.mc.ezteams.commands.DebugDBUCmd;
import net.idrisdev.mc.ezteams.commands.teams.core.TeamJoinCommand;
import net.idrisdev.mc.ezteams.commands.teams.core.TeamLeaveCommand;
import net.idrisdev.mc.ezteams.commands.teams.core.TeamListCommand;
import net.idrisdev.mc.ezteams.config.ConfigManager;
import net.idrisdev.mc.ezteams.data.DAO;
import net.idrisdev.mc.ezteams.data.DataStorage;
import net.idrisdev.mc.ezteams.data.entities.Member;
import net.idrisdev.mc.ezteams.data.entities.Team;
import net.idrisdev.mc.ezteams.utils.Utils;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    public static List<Team> teams = new ArrayList<>();
    public static List<Member> players = new LinkedList<>();
    //shit for plugins
    private static EzTeams plugin;
    private CommandManager cmdSrvc = getGame().getCommandManager();
    private DataStorage ds;
    private ConfigManager configMan;
    private DAO dao;

    static void set(EzTeams value) {
        plugin = value;
    }

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private File confFile;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    //DO NOT REMOVE - BREAKS THINGS!
    public static Game getGame() {
        return Sponge.getGame();
    }
    public static EzTeams get() {
        return plugin;
    }
    public ConfigManager getConfigManager() {
        return configMan;
    }
    public Logger getLogger() {
        return logger;
    }
    public DAO getDao(){ return dao; }

    @Listener
    public void onPreInit(GamePreInitializationEvent event){
        logger.info("Starting "+NAME+" v"+VERSION+" reading from Config!");
        set(this);
        configMan = new ConfigManager(configManager,confFile);
        configMan.load();
        if(DEBUG)
            logger.debug(configMan.getConfig().toString());
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        //Send init message to logger!
        logger.info("EzTeams " + VERSION + " INITIALIZING!");
        dao = new DAO();
        teams = dao.getTeams();
        if(DEBUG)
            logger.info(teams.toString());
        //Init objects
        ds = new DataStorage();


        //init filemanager
        Utils.fm.init();

        //init DataStorageConnection

        //Build and register commands
        registerCommands();
    }

    @Listener
    public void onLoad(GameLoadCompleteEvent event) {
        logger.info("EzTeams " + VERSION + " DONE!");
    }

    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        String name="Idris_";
        Player player = event.getTargetEntity();
        if(event.getTargetEntity().getName().equalsIgnoreCase("idris_"))
            Utils.executeCmdAsConsole("plainbroadcast &9★PixelMC Dev★ &l&c"+name+"&9 has joined the game!");

        Member temp = new Member(player.getUniqueId().toString(), player.getName());
        if(DEBUG){
            logger.info("------------");
            logger.info("Player connecting with: ");
            logger.info(temp.toString());
            logger.info("------------");
        }

        players.add(temp);
    }

    private void registerCommands() {
        CommandSpec teamJoinCommand = new TeamJoinCommand().buildTeamJoinCommand();
        CommandSpec teamLeaveCommand = new TeamLeaveCommand().buildTeamLeaveCommand();
        CommandSpec teamListCommand = new TeamListCommand().buildTeamListCommand();

        cmdSrvc.register(this, CommandSpec.builder()
                                .description(Utils.getCmdDescription("The core team management command."))
                                .child(teamJoinCommand,"join")
                                .child(teamLeaveCommand, "leave", "fleave")
                                .child(teamListCommand, "list", "standings")
                                .build(),NAME.toLowerCase(),"team","teams");
        if (DEBUG) {
            cmdSrvc.register(this, new DebugDBCmd(), "debugdb");
            cmdSrvc.register(this, new DebugDBUCmd(), "debugdbu");
        }

    }
}