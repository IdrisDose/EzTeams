package net.idrisdev.mc.ezteams.events;

import com.pixelmonmod.pixelmon.api.enums.BattleResults;
import com.pixelmonmod.pixelmon.api.events.PlayerBattleEndedEvent;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Idris on 10/10/2016.
 */
public class EventRekt extends PlayerBattleEndedEvent {
    public EventRekt(Player player, BattleControllerBase battleControllerBase, BattleResults result) {
        super(player, battleControllerBase, result);
    }
}
