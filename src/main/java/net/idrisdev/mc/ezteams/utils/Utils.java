package net.idrisdev.mc.ezteams.utils;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.core.entities.Team;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Identifiable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.spongepowered.api.text.format.TextColors.*;


/**
 * Created by Idris on 6/10/2016.
 */
public abstract class Utils {
    public static final UUID consoleFakeUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static Logger logger = EzTeams.get().getLogger();
    public static final String NAME = EzTeams.NAME;
    public static final String PERM_NAME = "ezteams";
    protected static final Text PLUGIN_NAME=Text.of(DARK_RED,"[",BLUE, NAME,DARK_RED,"]");
    public static final String NOCMDPERM = "You dont have the correct permissions!";
    public static final String getVersion(){return EzTeams.VERSION; }
    public static final Game game = EzTeams.getGame();
    public static FileManager fm = new FileManager();
    public static CommandSource getConsoleSrc(){ return game.getServer().getConsole().getCommandSource().get(); }

    public static final String CONFIG_HEADER = "1.0.0\n"
            + "# This is the basic config for EzTeams... More will be added later!,\n"
            + "# message me on skype for help :)";

    public static void sendSrcPlainMessage(CommandSource src, String message){
        src.sendMessage(PLUGIN_NAME.concat(Text.of(" ",message)));
    }
    public static void sendSrcPlainMessage(Optional<CommandSource> src, String message){
        src.get().sendMessage(PLUGIN_NAME.concat(Text.of(" ",message)));
    }

    public static void sendSrcErrorMessage(CommandSource src, String message){
        src.sendMessage(PLUGIN_NAME.concat(Text.of(" ",RED,message)));
    }

    public static void sendNoPermsMsg(CommandSource src){
        src.sendMessage(PLUGIN_NAME.concat(Text.of(" ",RED,NOCMDPERM)));
    }

    public static void sendNYIMessage(CommandSource src){
        src.sendMessage(PLUGIN_NAME.concat(Text.of(" ",RED,"Command not yet implemented.")));
    }

    protected void sendColorMessage(Text color, CommandSource src, String message){
        src.sendMessage(PLUGIN_NAME.concat(Text.of(" ",color,message)));
    }
    public static void sendPrettyMessage(CommandSource src, String message){
        src.sendMessage(PLUGIN_NAME.concat(Text.of(" ",GREEN,message)));
    }


    protected static UUID getUUID(CommandSource src) {
        if (src instanceof Identifiable) {
            return ((Identifiable) src).getUniqueId();
        }

        return consoleFakeUUID;
    }


    public void logInitMsg(String name){
        logger.info("Initializing "+name+".");
    }
    public void logCompMsg(String name){
        logger.info(name+" Complete!");
    }

    public static void executeCmdAsConsole(String cmd) {
        game.getCommandManager().process(getConsoleSrc(), cmd);
    }

    public static Text getCmdDescription(String text) {
        return PLUGIN_NAME.concat(Text.of(TextColors.GREEN).concat(Text.of(text)));
    }

    public static void plainbroadcastAsConsole(String msg){
        executeCmdAsConsole("plainbroadcast &4[&9"+Utils.NAME+"&4] &c"+msg);
    }

    public static CommandElement stringarg(String name){
        return GenericArguments.string(Text.of(name));
    }

    public static Member findMember(String name) {
        return EzTeams.getOnlineMembers().stream().filter(member -> member.getName().equals(name)).findFirst().get();
    }

    public static Member findPastMember(String uuid) {
        for(Member member : EzTeams.getAllPlayers()){
            if(member.getUuid().equals(uuid))
                return member;
        }
        return null;
    }

    public static Optional<Team> findTeam(String teamName) {
        return EzTeams.getTeams().stream().filter(team -> team.getName().equals(teamName)).findFirst();
    }

    public static Team findTeam(int teamID) {
        Team tmp = EzTeams.getTeams().get(0);
        for(Team t : EzTeams.getTeams()){
            tmp = t.getId()==teamID?t:tmp;
        }
        return tmp;
    }

    public static int findTeamCount(String teamName){
        int count = 0;
        Team team = findTeam(teamName).get();
        for(Member member : EzTeams.getAllPlayers()){
            if(member.getTeam().equals(team))
                count++;
        }
        return count;
    }

    public static Optional<Player> getPlayer(UUID uuid) {
        Optional<Player> onlinePlayer = Sponge.getServer().getPlayer(uuid);

        if (onlinePlayer.isPresent()) {
            return onlinePlayer;
        }

        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);

        return userStorage.isPresent()?userStorage.get().get(uuid).get().getPlayer():null;
    }


    public static boolean searchTeamsForName(String team) {
        List<Team> tmpList = EzTeams.getTeams();
        for(Team t : tmpList){
            if(t.getName().equalsIgnoreCase(team)){
                return true;
            }
        }
        return false;
    }

    public static Team getTeam(String teamName) {
        List<Team> tmpList = EzTeams.getTeams();
        for(Team t : tmpList){
            if(t.getName().equalsIgnoreCase(teamName))
                return t;
        }
        return null;
    }

    public static boolean teamValidCheck(CommandSource src,String teamname) {
        if(!searchTeamsForName(teamname)){
            Utils.sendSrcErrorMessage(src,teamname+" is not a currently available team.");
            return true;
        } else if(teamname.equals("staff")&& !src.hasPermission(Permissions.TEAMS_JOIN_STAFF)){
            Utils.sendSrcErrorMessage(src,"You are not allowed to join the staff team.");
            return true;
        } if(!(src instanceof Player)){
            Utils.sendSrcErrorMessage(src,"Only online members allowed to execute this command!");
            return true;
        } else if(teamname.equals("default")){
            Utils.sendSrcErrorMessage(src,"One does not join team default, one must use team leave.");
            return true;
        }else if(teamname.equals("developer")||teamname.equals("dev")){
            if(!((Player) src).getUniqueId().toString().equals("4316aa07-c6a4-4c91-8fc4-9df02465e279")) {
                Utils.sendSrcErrorMessage(src, "One does not join team developer, one must be a developer (Idris_ :P).");
                return true;
            }
        }
        return false;
    }
}
