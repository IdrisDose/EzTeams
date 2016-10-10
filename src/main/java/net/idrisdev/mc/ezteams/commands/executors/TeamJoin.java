package net.idrisdev.mc.ezteams.commands.executors;

import net.idrisdev.mc.ezteams.utils.ETUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

/**
 * Created by Idris on 7/10/2016.
 */
public class TeamJoin extends ETUtils implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String team = args.<String>getOne("team").get().toLowerCase();
        String s = "NULL";
        String f = "NULL";
        Player p;
        if(src != getConsoleSrc()) {
            p = (Player) src;
        } else{
            src.sendMessage(Text.of("Only players are allowed to execute this command!"));
            return CommandResult.success();
        }

        switch(team){
            case "rocket": s="ROCKET"; break;
            case "aqua": s="AQUA"; break;
            case "magma": s="MAGMA"; break;
            case "plasma": s="PLASMA"; break;
            case "galactic": s="GALACTIC"; break;
            default: f="Please select a valid team: rocket, aqua, magma, plasma or galactic.";
        }
        if(!s.equals("NULL"))
            src.sendMessage(Text.of("Congratulations! you joined "+s));
        else
            src.sendMessage(Text.of(f));

        return CommandResult.success();
    }
}
