package net.idrisdev.mc.ezteams.core;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.commands.teams.admin.*;
import net.idrisdev.mc.ezteams.commands.teams.core.HelpCommand;
import net.idrisdev.mc.ezteams.commands.teams.core.TeamJoinCommand;
import net.idrisdev.mc.ezteams.commands.teams.core.TeamLeaveCommand;
import net.idrisdev.mc.ezteams.commands.teams.core.TeamListCommand;
import net.idrisdev.mc.ezteams.commands.teams.member.MemberCountCommand;
import net.idrisdev.mc.ezteams.commands.teams.member.MemberPointsCommand;
import net.idrisdev.mc.ezteams.commands.teams.sudo.AdminCreateTeam;
import net.idrisdev.mc.ezteams.commands.teams.sudo.AdminDeleteTeam;
import net.idrisdev.mc.ezteams.commands.teams.sudo.SudoResetTeams;
import net.idrisdev.mc.ezteams.commands.teams.sudo.TeamSetPrefix;
import net.idrisdev.mc.ezteams.config.Config;
import net.idrisdev.mc.ezteams.config.ConfigManager;
import net.idrisdev.mc.ezteams.core.data.DAO;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.core.tasks.AutoSaveTask;
import net.idrisdev.mc.ezteams.core.tasks.JoinEvent;
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

        //Instantiate new DAO
        dao = new DAO();

        //Intialize the Data access object.
        logger.info("Initializing DAO.");
        dao.initDB();

        //Get teams if there are any.
        logger.info("Loading Teams");
        EzTeams.setTeams(dao.getTeams());

        //Get players if exist n table.
        logger.info("Loading Past Players.");
        EzTeams.setAllPlayers(dao.getMembers());

        debug(EzTeams.getTeams().toString());

        Utils.fm.init();

        registerCommands();
    }

    private void registerCommands() {

        //Player Team Commands
        /**
         * teamJoinCommand - Join a specific team
         * teamLeavecommand - Leaves a specific team
         * teamListCommand - Lists the team rankings
         */
        CommandSpec teamJoinCommand = TeamJoinCommand.buildTeamJoinCommand();
        CommandSpec teamLeaveCommand = TeamLeaveCommand.buildTeamLeaveCommand();
        CommandSpec teamListCommand = TeamListCommand.buildTeamListCommand();
        CommandSpec memberMyPoints = MemberPointsCommand.buildMemberPointsCommand();
        CommandSpec helpCommand = HelpCommand.buildHelpCommand();

        //Admin Team Commands
        /**
         * memberSetCommand - Add|Set a member to a team
         * memberRemoveCommand - Remove a member from a team
         * memberPointsCommand - Set|Add|Remove points from a Member
         * teamPointsCommand - Set|Add|Remove points from a Team
         * memberCountCommand - Count how many members a team has -UNUSED-
         * teamAdminListCommand - List ALL of the teams including default and staff.
        */
        CommandSpec memberSetCommand = AdminSetTeam.buildMemberSetTeam();
        CommandSpec memberRemoveCommand = AdminRemoveFromTeam.buildMemberRemoveTeam();
        CommandSpec memberPointsCommand = AdminMemberPoints.buildAdminMemberPoints();
        CommandSpec teamPointsCommand = AdminTeamPoints.buildAdminTeamPoints();
        CommandSpec memberCountCommand = MemberCountCommand.buildMemberCountCommand();
        CommandSpec teamAdminListCommand = AdminListCommand.buildAdminListCommand();
        CommandSpec memberWinPoints = AdminPointsWin.buildAdminPointsWin();

        //Sudo Commands
        /**
         * teamCreate - Create a team with prefix
         * teamDelete - Remove a Team
         */
        CommandSpec teamCreate = AdminCreateTeam.buildAdminCreateTeam();
        CommandSpec teamDelete = AdminDeleteTeam.buildAdminDeleteTeam();
        CommandSpec teamReset = SudoResetTeams.buildSudoResetTeams();
        CommandSpec teamPrefix = TeamSetPrefix.buildTeamSetPrefix();

        /* CommandSpec registration */

        CommandSpec points = CommandSpec.builder()
                .permission(Permissions.TEAMS_POINTS_BASE)
                .child(memberPointsCommand,"member","player")
                .child(teamPointsCommand,"team")
                .description(Utils.getCmdDescription("STAFF ONLY - Team points management"))
                .build();

        CommandSpec adminCommand = CommandSpec.builder()
                .permission(Permissions.TEAMS_ADMIN)
                .child(memberSetCommand, "add","set")
                .child(memberRemoveCommand,"remove")
                .child(teamAdminListCommand,"list")
                .child(points,"points")
                .child(memberWinPoints,"win")
                .description(Utils.getCmdDescription("ADMIN ONLY - team member management"))
                .build();

        CommandSpec sudoCommand = CommandSpec.builder()
                .permission(Permissions.TEAMS_SUDO)
                .child(teamCreate, "create")
                .child(teamDelete, "delete")
                .child(teamReset, "reset")
                .child(teamPrefix, "setprefix","prefix")
                .description(Utils.getCmdDescription("SUDO ONLY - Team Creation and Deletion"))
                .build();


        /* Registering CommandSpecs in the CommandManager */
        cmdSrvc.register(plugin, CommandSpec.builder()
                .description(Utils.getCmdDescription("The core team management command."))
                .permission(Permissions.TEAMS_BASE)
                .child(teamJoinCommand,"join")
                .child(teamLeaveCommand, "leave")
                .child(teamListCommand, "list", "standings")
                .child(memberMyPoints,"mypoints","points")
                .child(helpCommand,"help","?")
                .build(),NAME.toLowerCase(),"team","teams");

        cmdSrvc.register(plugin,adminCommand,"teamadmin","tadmin");
        cmdSrvc.register(plugin,sudoCommand, "tsudo","teamsudo");

    }

    /**
     * Gets the Data Access Object
     * @return dao
     */
    public DAO getDao(){ return dao; }

    /**
     * Gets the Config Manager
     * @return configMan
     */
    public ConfigManager getConfigManager() {
        return configMan;
    }

    /**
     * Operates the ClientJoinEvent
     *
     * @param event
     */
    public void ClientJoin(ClientConnectionEvent.Join event){
        Player player = event.getTargetEntity();
        JoinEvent.runJoinEvent(player,plugin,DEBUG);
    }
    public void ClientLeave(ClientConnectionEvent.Disconnect event){
        Player target = event.getTargetEntity();
        Member member = Utils.findMember(target.getName());

        plugin.getOnlineMembers().remove(member);
    }
    public void onServerStart() {
        logger.info("Starting AutoSave task");
        EzTeams.getGame().getScheduler().createTaskBuilder()
                .execute(new AutoSaveTask(configMan.getConfig(),plugin))
                .name("EzTeamsAutoSave")
                .interval(configMan.getConfig().getInterval(), TimeUnit.SECONDS)
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
