package net.idrisdev.mc.ezteams.core;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.commands.teams.admin.AdminMemberPoints;
import net.idrisdev.mc.ezteams.commands.teams.admin.AdminRemoveTeam;
import net.idrisdev.mc.ezteams.commands.teams.admin.AdminSetTeam;
import net.idrisdev.mc.ezteams.commands.teams.admin.AdminTeamPoints;
import net.idrisdev.mc.ezteams.commands.teams.core.TeamJoinCommand;
import net.idrisdev.mc.ezteams.commands.teams.core.TeamLeaveCommand;
import net.idrisdev.mc.ezteams.commands.teams.core.TeamListCommand;
import net.idrisdev.mc.ezteams.commands.teams.member.MemberCountCommand;
import net.idrisdev.mc.ezteams.config.ConfigManager;
import net.idrisdev.mc.ezteams.core.data.DAO;
import net.idrisdev.mc.ezteams.core.data.DataStorage;
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
    public static final boolean DEBUG = true;
    private EzTeams plugin;

    private CommandManager cmdSrvc = EzTeams.getGame().getCommandManager();
    private DataStorage ds;
    private ConfigManager configMan;
    private DAO dao;

    private Logger logger = EzTeams.get().getLogger();

    private File confFile;
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    public Core(EzTeams plugin, File confFile, ConfigurationLoader<CommentedConfigurationNode> configManager) {
        this.plugin = plugin;
        this.confFile = confFile;
        this.configManager = configManager;
    }
    public void initPlugin() {
        //Send init message to logger!
        logger.info("EzTeams " + VERSION + " INITIALIZING!");
        dao = new DAO();
        EzTeams.teams = dao.getTeams();
        EzTeams.allPlayers = dao.getMembers();
        if(DEBUG)
            logger.info(EzTeams.teams.toString());
        //Init objects
        ds = new DataStorage();


        //init filemanager
        Utils.fm.init();

        //init DataStorageConnection

        //Build and register commands
        registerCommands();
    }
    public void preInitPlugin() {
        logger.info("Starting "+NAME+" v"+VERSION+" reading from Config!");

        configMan = new ConfigManager(configManager,confFile);
        configMan.load();
        if(DEBUG)
            logger.debug(configMan.getConfig().toString());
    }
    private void registerCommands() {
        CommandSpec teamJoinCommand = TeamJoinCommand.buildTeamJoinCommand();
        CommandSpec teamLeaveCommand = TeamLeaveCommand.buildTeamLeaveCommand();
        CommandSpec teamListCommand = TeamListCommand.buildTeamListCommand();


        CommandSpec memberSetCommand = AdminSetTeam.buildMemberSetTeam();
        CommandSpec memberRemoveCommand = AdminRemoveTeam.buildMemberRemoveTeam();
        CommandSpec memberCountCommand = MemberCountCommand.buildMemberCountCommand();

        CommandSpec memberPointsCommand = AdminMemberPoints.buildAdminMemberPoints();
        CommandSpec teamPointsCommand = AdminTeamPoints.buildAdminTeamPoints();

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
            EzTeams.allPlayers.add(temp);
            logger.info("Player added to all member list.");
        }

        if(DEBUG){
            logger.info("------------");
            logger.info("Player connecting with: ");
            logger.info(temp.toString());
            logger.info("------------");
        }

        EzTeams.onlineMembers.add(temp);
    }
    public void ClientLeave(ClientConnectionEvent.Disconnect event){
        Player target = event.getTargetEntity();
        Member member = Utils.findMember(target.getName());
        EzTeams.onlineMembers.remove(member);
    }

    public void onServerStart() {
        logger.info("Starting AutoSave task");
        EzTeams.getGame().getScheduler().createTaskBuilder()
                .execute(new AutoSaveTask(configMan.getConfig(),plugin))
                .name("EzTeamsAutoSave")
                .interval(configMan.getConfig().db.getInterval(), TimeUnit.SECONDS)
                .submit(plugin);
        logger.info("Completed loading AutoSaveTask");
    }
}
