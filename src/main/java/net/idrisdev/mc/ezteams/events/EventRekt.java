package net.idrisdev.mc.ezteams.events;

import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PlayerBattleEndedEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.storage.PlayerExtraData;
import net.idrisdev.mc.ezteams.data.PlayerDataStorage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;
import java.util.UUID;

import static net.idrisdev.mc.ezteams.utils.ETUtils.*;


/**
 * Created by Idris on 10/10/2016.
 */
public class EventRekt {
    public EntityLivingBase participant1;
    public BattleParticipant participant2;

    public EventRekt(){}

    @SubscribeEvent
    public void onBattleEnd(PlayerBattleEndedEvent event) {
        String Result = event.result.toString();
        String ply = event.player.getName();
        String cmd = "broadcast "+ply+" has just won a battle!";
        EntityPlayerMP player = event.player;
        UUID id = player.getUniqueID();
        Optional<Player> spongePlayer = PlayerDataStorage.getOnlinePlayer(id);

        if(participant1!=null)
            logger.info("Part1 not null!"+participant1.getDisplayName());
        else
            logger.info("Probs try a new one ye?");

        logger.info(ply+" just scored a "+Result+" in a battle!");
        if(Result.equals("VICTORY")) {
            game.getCommandManager().process(getConsoleSrc(), cmd);
            game.getCommandManager().process(getConsoleSrc(), "team points add rocket 10");
        }
    }

    @SubscribeEvent
    public void onBattleStartedEvent(BattleStartedEvent event){
        String name1 = event.participant1[0].getDisplayName();
        String name2 = event.participant2[0].getDisplayName();
        game.getCommandManager().process(getConsoleSrc(),"broadcast "+name1+" is fighting "+name2);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayerMP player = (EntityPlayerMP)event.player;
        PlayerExtraData ped = null;
        if(player.getDisplayNameString().equals("Idris)_"))
            ped = new PlayerExtraData(player);

        if(ped != null)
            ped.isDeveloper=true;

        handleJoin(player,ped.isDeveloper);
    }

    public static void handleJoin(EntityPlayer player, boolean isDeveloper) {
        if(isDeveloper) {
           ChatHandler.sendServerMessage((new StringBuilder()).append("").append(EnumChatFormatting.AQUA).append("\u2605PixelMC Dev\u2605 ").append(EnumChatFormatting.UNDERLINE).append(player.getDisplayNameString()).append(EnumChatFormatting.AQUA).append(" has joined.").toString());
        }

    }
}
