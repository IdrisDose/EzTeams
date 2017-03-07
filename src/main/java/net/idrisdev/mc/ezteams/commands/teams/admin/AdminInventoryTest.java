package net.idrisdev.mc.ezteams.commands.teams.admin;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import sun.awt.CausedFocusEvent;

/**
 * Created by Idris on 25/01/2017.
 */
public class AdminInventoryTest {

    private static EzTeams plugin = EzTeams.get();

    public AdminInventoryTest() {
    }

    public static CommandSpec buildAdminInventoryTest() {
        return CommandSpec.builder()
                .permission("")
                .description(Utils.getCmdDescription("Somefuckboi"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))
                )
                .executor((src, args) -> {

                    Cause cause = Cause.builder()
                            .owner(src)
                            .build();

                    Player p = args.<Player>getOne("player").get();
                    Player victom = src instanceof Player? (Player) src:null;

                    Utils.sendPrettyMessage(src,"Shits cool sun.");
                    victom.openInventory(p.getInventory(), cause);

                    return CommandResult.success();
                })
                .build();
    }
}
