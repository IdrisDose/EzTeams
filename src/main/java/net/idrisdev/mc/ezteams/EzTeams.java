package net.idrisdev.mc.ezteams;

import com.google.inject.Inject;
import net.idrisdev.mc.ezteams.core.Core;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.core.entities.Team;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.*;
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
    public static final String MODID = Core.MODID;
    public static final String NAME = Core.NAME;
    public static final String VERSION = Core.VERSION;
    public static final boolean DEBUG = Core.DEBUG;
    public static List<Team> teams = new ArrayList<>();
    public static List<Member> onlineMembers = new LinkedList<>();
    public static List<Member> allPlayers = new LinkedList<>();

    //shit for plugins
    private static EzTeams plugin;
    static void set(EzTeams value) {
        plugin = value;
    }
    public Core core;

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
    public Logger getLogger() {
        return logger;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event){
        set(this);
        core = new Core(this,confFile,configManager);
        core.preInitPlugin();
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        core.initPlugin();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event){
        core.onServerStart();
    }

    @Listener
    public void onLoad(GameLoadCompleteEvent event) {
        logger.info("EzTeams " + VERSION + " DONE!");
    }

    @Listener(order = Order.LAST)
    public void onJoin(ClientConnectionEvent.Join event) {
        core.ClientJoin(event);
    }

    @Listener
    public void onLeave(ClientConnectionEvent.Disconnect event){
        core.ClientLeave(event);
    }

    @Listener
    public void onReload(GameReloadEvent event){
        core.preInitPlugin();
        core.initPlugin();
    }

    @Listener
    public void onStopping(GameStoppingEvent event){
        logger.info("Stopping Server fam. 1");
    }

    @Listener
    public void onStoppingServer(GameStoppingServerEvent event){
        logger.info("Stopping Server fam. 2");
    }
}