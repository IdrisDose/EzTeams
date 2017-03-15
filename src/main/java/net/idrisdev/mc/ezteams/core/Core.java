package net.idrisdev.mc.ezteams.core;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.commands.teams.admin.*;
import net.idrisdev.mc.ezteams.commands.teams.core.*;
import net.idrisdev.mc.ezteams.commands.teams.member.MemberCountCommand;
import net.idrisdev.mc.ezteams.commands.teams.member.MemberPointsCommand;
import net.idrisdev.mc.ezteams.commands.teams.sudo.AdminCreateTeam;
import net.idrisdev.mc.ezteams.commands.teams.sudo.AdminDeleteTeam;
import net.idrisdev.mc.ezteams.commands.teams.sudo.SudoResetTeams;
import net.idrisdev.mc.ezteams.commands.teams.sudo.TeamSetPrefix;
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
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Idris on 23/01/2017.
 */
public class Core {
    public static final String VERSION = "1.0";
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
        CommandSpec bugReport = BugReport.buildbugreport();

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
                .executor((src, args) -> executeAdminHelp(src,args))
                .child(memberSetCommand, "add","set")
                .child(memberRemoveCommand,"remove")
                .child(teamAdminListCommand,"list")
                .child(points,"points")
                .child(memberWinPoints,"win")
                .description(Utils.getCmdDescription("ADMIN ONLY - team member management"))
                .build();

        CommandSpec sudoCommand = CommandSpec.builder()
                .permission(Permissions.TEAMS_SUDO)
                .executor((src, args) -> executeSudoHelp(src,args))
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
                .executor((src, args) -> executeBaseHelp(src,args))
                .child(teamJoinCommand,"join")
                .child(teamLeaveCommand, "leave")
                .child(teamListCommand, "list", "standings")
                .child(memberMyPoints,"mypoints","points")
                .child(bugReport,"bug","bugreport", "report")
                .build(),NAME.toLowerCase(),"team","teams");

        cmdSrvc.register(plugin,adminCommand,"teamadmin","tadmin");
        cmdSrvc.register(plugin,sudoCommand, "tsudo","teamsudo");
        cmdSrvc.register(plugin,points,"points","mypoints");

    }

    private CommandResult executeBaseHelp(CommandSource src, CommandContext args) {

        Utils.sendPrettyMessage(src,"----EzTeams Help----");
        Utils.sendPrettyMessage(src,"-Commands-");
        Utils.sendPrettyMessage(src,"-join : Allows you to join a team.");
        Utils.sendPrettyMessage(src, "You have perm? "+convertBoolToString(src.hasPermission(Permissions.TEAMS_JOIN)));
        Utils.sendPrettyMessage(src," ");

        Utils.sendPrettyMessage(src,"-leave : Allows you to leave a team.");
        Utils.sendPrettyMessage(src, "You have perm? "+convertBoolToString(src.hasPermission(Permissions.TEAMS_LEAVE)));
        Utils.sendPrettyMessage(src," ");

        Utils.sendPrettyMessage(src,"-list|standings : List all teams and their points");
        Utils.sendPrettyMessage(src,"You have perm? "+convertBoolToString(src.hasPermission(Permissions.TEAMS_LEAVE)));
        Utils.sendPrettyMessage(src," ");

        Utils.sendPrettyMessage(src,"-points : List your current points");
        Utils.sendPrettyMessage(src,"You have perm? "+convertBoolToString(src.hasPermission(Permissions.TEAMS_POINTS_VIEW)));
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-------------------");
        return CommandResult.success();
    }

    private CommandResult executeAdminHelp(CommandSource src, CommandContext args){
        Boolean setTeam = src.hasPermission(Permissions.TEAMS_ADMIN_ADD);
        Boolean removeTeam = src.hasPermission(Permissions.TEAMS_ADMIN_REMOVE);
        Boolean points = src.hasPermission(Permissions.TEAMS_ADMIN_MEMBER_POINS);
        Boolean win = src.hasPermission(Permissions.TEAMS_ADMIN_MEMBER_WIN);

        Utils.sendPrettyMessage(src,"----EzTeams Help----");
        Utils.sendPrettyMessage(src,"-Commands-");
        Utils.sendPrettyMessage(src,"-set|add : Allows you to set a members team");
        Utils.sendPrettyMessage(src,"You have perm? "+convertBoolToString(setTeam));
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-remove : Allows you to remove a player from a team");
        Utils.sendPrettyMessage(src,"You have perm? "+convertBoolToString(removeTeam));
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-points player add|remove : Allows you to modify a members points");
        Utils.sendPrettyMessage(src,"You have perm? "+convertBoolToString(points));
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-win position : Adds a preset amount of points to a player");
        Utils.sendPrettyMessage(src,"You have perm? "+convertBoolToString(win));
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-------------------");
        return CommandResult.success();
    }

    private CommandResult executeSudoHelp(CommandSource src, CommandContext args){
        Utils.sendPrettyMessage(src,"----EzTeams Help----");
        Utils.sendPrettyMessage(src,"-Commands-");
        Utils.sendPrettyMessage(src,"-create <name> <prefix> : Allows you to create a new team.");
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-delete <name> : Allows you to delete a team.");
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-reset all|<name> : Allows you to reset a specific team and it's members or resets all points.");
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-prefix|setprefix <name> | Allows you to change a teams prefix.");
        Utils.sendPrettyMessage(src," ");
        Utils.sendPrettyMessage(src,"-------------------");
        return CommandResult.success();
    }

    private String convertBoolToString(boolean b) {
        return b ?"Yes":"No";
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
