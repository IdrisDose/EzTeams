package net.idrisdev.mc.ezteams.utils;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.core.entities.Team;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.option.OptionSubject;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.util.Identifiable;

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

    public static Optional<OptionSubject> getSubject(User user) {
        Subject subject = user.getContainingCollection().get(user.getIdentifier());
        return subject instanceof OptionSubject ? Optional.of((OptionSubject) subject) : Optional.empty();
    }

    public Text getTextFromOption(CommandSource src, String option) {
        if (src instanceof Player) {
            Optional<OptionSubject> oos = getSubject((Player)src);
            if (oos.isPresent()) {
                Optional<String> optionString = oos.get().getOption(option);
                if (optionString.isPresent()) {
                    return TextSerializers.FORMATTING_CODE.deserialize(optionString.get());
                }
            }
        }

        return Text.EMPTY;
    }

    public Text getOption(User usr, String option) {
            Optional<OptionSubject> oos = getSubject(usr);
            if (oos.isPresent()) {
                Optional<String> optionString = oos.get().getOption(option);
                if (optionString.isPresent()) {
                    return TextSerializers.FORMATTING_CODE.deserialize(optionString.get());
                }
            }
        return Text.EMPTY;
    }

    public Optional<Player> getPlayer(UUID uuid){
        Optional<Player> onlinePlayer = game.getServer().getPlayer(uuid);
        Optional<UserStorageService> userStorage = game.getServiceManager().provide(UserStorageService.class);

        if (onlinePlayer.isPresent()) {
            return onlinePlayer;
        }else{
            return null;
        }
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
        return EzTeams.onlineMembers.stream().filter(member -> member.getName().equals(name)).findFirst().get();
    }

    public static Member findPastMember(String uuid) {
        for(Member member : EzTeams.allPlayers){
            if(member.getUuid().equals(uuid))
                return member;
        }
        return null;
    }

    public static Optional<Team> findTeam(String teamName) {
        return EzTeams.teams.stream().filter(team -> team.getName().equals(teamName)).findFirst();
    }

    public static Team findTeam(int teamID) {
        Team tmp = EzTeams.teams.get(1);
        for(Team t : EzTeams.teams){
            tmp = t.getId()==teamID?t:tmp;
        }
        return tmp;
    }

    public static int findTeamCount(String teamName){
        int count = 0;
        Team team = findTeam(teamName).get();
        for(Member member : EzTeams.allPlayers){
            if(member.getTeam().equals(team))
                count++;
        }
        return count;
    }
}
