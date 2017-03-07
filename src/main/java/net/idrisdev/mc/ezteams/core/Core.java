package net.idrisdev.mc.ezteams.core;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.commands.teams.admin.*;
import net.idrisdev.mc.ezteams.commands.teams.core.TeamJoinCommand;
import net.idrisdev.mc.ezteams.commands.teams.core.TeamLeaveCommand;
import net.idrisdev.mc.ezteams.commands.teams.core.TeamListCommand;
import net.idrisdev.mc.ezteams.commands.teams.member.MemberCountCommand;
import net.idrisdev.mc.ezteams.config.ConfigManager;
import net.idrisdev.mc.ezteams.core.data.DAO;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.core.tasks.AutoSaveTask;
import net.idrisdev.mc.ezteams.utils.Permissions;
import net.idrisdev.mc.ezteams.utils.Utils;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Idris on 23/01/2017.
 */
public class Core {
    public static final String VERSION = "0.9a";
    public static final String NAME = "EzTeams";
    public static final String MODID = "ezteams";
    public static boolean DEBUG;
    private static EzTeams plugin;

    private CommandManager cmdSrvc = EzTeams.getGame().getCommandManager();
    private ConfigManager configMan;
    private DAO dao;

    private Logger logger = EzTeams.get().getLogger();

    private File confFile;
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    public Core(EzTeams plugin, File confFile, ConfigurationLoader<CommentedConfigurationNode> configManager) {
        this.plugin = plugin;
        this.confFile = confFile;
        this.configManager = configManager;
        configMan = new ConfigManager(configManager,confFile);
    }
    public void preInitPlugin() {
        logger.info("Starting "+NAME+" v"+VERSION+" reading from Config!");
        configMan.load();
        DEBUG = configMan.getConfig().getDebug();
    }

    public void initPlugin() {
        //Send init message to logger!
        logger.info("EzTeams " + VERSION + " INITIALIZING!");
        dao = new DAO();

        logger.info("Initializing DAO.");
        dao.initDB();

        logger.info("Loading Teams");
        EzTeams.setTeams(dao.getTeams());

        logger.info("Loading Past Players.");
        EzTeams.setAllPlayers(dao.getMembers());

        debug(EzTeams.getTeams().toString());

        //init filemanager
        Utils.fm.init();

        //init DataStorageConnection

        //Build and register commands
        registerCommands();
    }

    private void registerCommands() {
        CommandSpec teamJoinCommand = TeamJoinCommand.buildTeamJoinCommand();
        CommandSpec teamLeaveCommand = TeamLeaveCommand.buildTeamLeaveCommand();
        CommandSpec teamListCommand = TeamListCommand.buildTeamListCommand();


        CommandSpec memberSetCommand = AdminSetTeam.buildMemberSetTeam();
        CommandSpec memberRemoveCommand = AdminRemoveFromTeam.buildMemberRemoveTeam();
        CommandSpec memberCountCommand = MemberCountCommand.buildMemberCountCommand();

        CommandSpec memberPointsCommand = AdminMemberPoints.buildAdminMemberPoints();
        CommandSpec teamPointsCommand = AdminTeamPoints.buildAdminTeamPoints();
        CommandSpec adminInvenCommand = AdminInventoryTest.buildAdminInventoryTest();
        CommandSpec memberCommand = CommandSpec.builder()
                .child(memberCountCommand,"count")
                .permission(Permissions.TEAMS_MEMBER_BASE)
                .description(Utils.getCmdDescription("Team member base command"))
                .build();

        CommandSpec adminCommand = CommandSpec.builder()
                .permission(Permissions.TEAMS_ADMIN)
                .child(memberSetCommand, "set","add")
                .child(memberRemoveCommand,"remove","delete")
                .description(Utils.getCmdDescription("ADMIN ONLY - team member management"))
                .build();

        CommandSpec points = CommandSpec.builder()
                .permission(Permissions.TEAMS_POINTS_BASE)
                .child(memberCommand,"member","player")
                .child(teamPointsCommand,"team")
                .description(Utils.getCmdDescription("STAFF ONLY - Team points management"))
                .build();

        cmdSrvc.register(plugin, CommandSpec.builder()
                .description(Utils.getCmdDescription("The core team management command."))
                .permission(Permissions.TEAMS_BASE)
                .child(teamJoinCommand,"join")
                .child(teamLeaveCommand, "leave", "fleave")
                .child(teamListCommand, "list", "standings")
                .child(memberCommand,"member")
                .child(adminCommand,"admin")
                .child(teamJoinCommand,"points")
                .build(),NAME.toLowerCase(),"team","teams");
        cmdSrvc.register(plugin,teamPointsCommand,"teampoints");
        cmdSrvc.register(plugin,memberPointsCommand,"memberpoints","playerpoints");
        cmdSrvc.register(plugin,adminInvenCommand,"invsee");


    }
    public DAO getDao(){ return dao; }
    public ConfigManager getConfigManager() {
        return configMan;
    }
    public void ClientJoin(ClientConnectionEvent.Join event){
        Player player = event.getTargetEntity();
        String name = player.getName();
        String uuid = player.getUniqueId().toString();
        if(Permissions.names.contains(name))
            Utils.executeCmdAsConsole("plainbroadcast &9★PixelMC Dev★ &l&c"+name+"&9 has joined the game!");

        Member temp = Utils.findPastMember(player.getUniqueId().toString());

        if(temp == null) {
            logger.info("New Player joining!");
            temp = new Member(uuid, name);
            plugin.getAllPlayers().add(temp);
            logger.info("Player added to all member list.");
        }

        if(DEBUG){
            logger.info("------------");
            logger.info("Player connecting with: ");
            logger.info(temp.toString());
            logger.info("------------");
        }

        plugin.getOnlineMembers().add(temp);
    }
    public void ClientLeave(ClientConnectionEvent.Disconnect event){
        Player target = event.getTargetEntity();
        Member member = Utils.findMember(target.getName());
        plugin.getOnlineMembers().remove(member);
    }

    public void onServerStart() {
        logger.info("Starting AutoSave task");
        plugin.getGame().getScheduler().createTaskBuilder()
                .execute(new AutoSaveTask(configMan.getConfig(),plugin))
                .name("EzTeamsAutoSave")
                .interval(configMan.getConfig().db.getInterval(), TimeUnit.SECONDS)
                .submit(plugin);
        logger.info("Completed loading AutoSaveTask");
    }

    public void gameStoppingServer() {
        new AutoSaveTask(configMan.getConfig(),plugin).run();
    }

    public static void debug(String msg) {
        if(DEBUG){
            EzTeams plugin = EzTeams.get();
            plugin.getLogger().info(msg);
        }

    }
}
