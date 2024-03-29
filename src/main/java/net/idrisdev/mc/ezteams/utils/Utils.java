package net.idrisdev.mc.ezteams.utils;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.Core;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.core.entities.Team;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.spongepowered.api.text.format.TextColors.*;


/**
 * Created by Idris on 6/10/2016.
 */
public abstract class Utils {
    private static final String consoleFakeUUID = "00000000-0000-0000-0000-000000000000";
    public static Logger logger = EzTeams.get().getLogger();
    public static final String NAME = EzTeams.NAME;
    static final String PERM_NAME = "ezteams";
    private static final Text PLUGIN_NAME=Text.of(DARK_RED,"[",BLUE, NAME,DARK_RED,"]");
    private static final String NOCMDPERM = "You dont have the correct permissions!";
    public static String getVersion(){return EzTeams.VERSION; }
    private static final Game game = EzTeams.getGame();
    public static FileManager fm = new FileManager();
    private static CommandSource getConsoleSrc(){ return game.getServer().getConsole().getCommandSource().get(); }
    private static List<String> blacklist = Arrays.asList("trump","terrorist","isis","daesh","cunt","faggots","fags");

    public static final String CONFIG_HEADER = "1.0.0\n"
            + "# This is the basic config for EzTeams... More will be added later!,\n"
            + "# message me on skype for help :)";

      public static void sendSrcErrorMessage(CommandSource src, String message){
        src.sendMessage(PLUGIN_NAME.concat(Text.of(" ",RED,message)));
    }
    public static void sendPrettyMessage(CommandSource src, String message){
        src.sendMessage(PLUGIN_NAME.concat(Text.of(" ",GREEN,message)));
    }

    public static void executeCmdAsConsole(String cmd) {
        game.getCommandManager().process(getConsoleSrc(), cmd);
    }

    public static Text getCmdDescription(String text) {
        return PLUGIN_NAME.concat(Text.of(TextColors.GREEN).concat(Text.of(text)));
    }

    public static void plainbroadcastAsConsole(String msg){
        executeCmdAsConsole("plainbroadcast &4[&9"+NAME+"&4] &c"+msg);
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
            sendSrcErrorMessage(src,teamname+" is not a currently available team.");
            return true;
        } else if(teamname.equals("staff")&& !src.hasPermission(Permissions.TEAMS_JOIN_STAFF)){
            sendSrcErrorMessage(src,"You are not allowed to join the staff team.");
            return true;
        } if(!(src instanceof Player)){
            sendSrcErrorMessage(src,"Only online members allowed to execute this command!");
            return true;
        } else if(teamname.equals("default")){
            sendSrcErrorMessage(src,"One does not join team default, one must use team leave.");
            return true;
        }else if(teamname.equals("developer")||teamname.equals("dev")){
            if(!((Player) src).getUniqueId().toString().equals("4316aa07-c6a4-4c91-8fc4-9df02465e279")) {
                sendSrcErrorMessage(src, "One does not join team developer, one must be a developer (Idris_ :P).");
                return true;
            }
        }else if(blacklist.contains(teamname)){
            sendSrcErrorMessage(src,"This team name has been blacklisted.");
            return true;
        }else if(checkBL(((Player) src).getUniqueId().toString())){
            sendSrcErrorMessage(src,"You have been blacklisted");
            return true;
        }
        return false;
    }

    public static List<String> getBlacklist(){
        return blacklist;
    }

    public static boolean checkBL(String UUID) {
        return Core.getBlackList().contains(UUID);
    }

    public static boolean joinValidate(CommandSource src, Member mem, Team team) {
        Team defTeam = findTeam("default").get();

        if(!mem.getTeam().equals(defTeam)){
            sendSrcErrorMessage(src,"You are not in team default, you have to use team leave first!");
            return true;
        }else if(checkBL(mem.getUuid())) {
            sendSrcErrorMessage(src,"You have been blacklisted!");
            return true;
        }else if(mem == null || team == null) {
            sendSrcErrorMessage(src, "An error occurred while joining a team. Msg Idris_.");
            if (Core.DEBUG) {
                sendSrcErrorMessage(src, "mem: " + mem);
                sendSrcErrorMessage(src, "team: " + team);
            }
            return true;
        } else if(team.equals(defTeam)){
            sendSrcErrorMessage(src,"Cannot join team default.");
            return true;
        }
        return false;
    }

    public static boolean leaveValidate(CommandSource src, Member mem, Team team) {
        if(checkBL(mem.getUuid())) {
            sendSrcErrorMessage(src,"You have been blacklisted!");
            return true;
        }else if(mem.getTeam().equals(team)){
            sendSrcErrorMessage(src,"You cannot leave team default.");
            return true;
        } else if(checkNull(mem,team)){
            sendSrcErrorMessage(src,"An error occured while joining a team. Msg Idris_.");
            Core.getTeamsLog().error("mem: " + mem);
            Core.getTeamsLog().error("team: " + team);
            return true;
        }
        return false;
    }

    private static boolean checkNull(Member mem, Team team) {
        return mem == null || team == null;
    }

    public static boolean validateOther(CommandSource src, Member mem, Team team) {
        Team defTeam = findTeam("default").get();

        if(checkBL(mem.getUuid())) {
            sendSrcErrorMessage(src,"Target has been blacklisted!");
            return true;
        }else if(mem == null || team == null) {
            sendSrcErrorMessage(src, "An error occured while joining a team. Msg Idris_.");
            if (Core.DEBUG) {
                sendSrcErrorMessage(src, "mem: " + mem);
                sendSrcErrorMessage(src, "team: " + team);
            }
            return true;
        } else if(team.equals(defTeam)) {
            sendSrcErrorMessage(src,"You cannot set someone to team default.");
            return true;
        }

        return false;
    }

    public static boolean validateRemoveOther(CommandSource src, Member mem, Team team){
        if(checkBL(mem.getUuid())) {
            sendSrcErrorMessage(src,"Target has been blacklisted!");
            return true;
        } else if(mem == null || team == null){
            sendSrcErrorMessage(src,"An error occured while joining a team. Msg Idris_.");
            if(Core.DEBUG) {
                sendSrcErrorMessage(src, "mem: " + mem);
                sendSrcErrorMessage(src, "team: " + team);
            }
            return true;
        }
        return false;
    }

    public static boolean validatePoints(CommandSource src, int points, String name) {
        if(points<0){
            sendSrcErrorMessage(src,"You cannot use negative numbers.");
            return true;
        }else  if(name.equals("none")){
            sendSrcErrorMessage(src,"You must specify a target");
            return true;
        }
        return false;
    }
}
