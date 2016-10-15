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
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.idrisdev.mc.ezteams.utils.ETUtils.*;


/**
 * Created by IdrisDev on 10/10/2016 for EzTeams for EzTeams.
 */
public class PixelmonEvents {
    private EntityLivingBase participant1;
    private BattleParticipant participant2;
    private String[] shit = {"Obeliskthegreat", "Idris_", "Ozzybuns"};


    public PixelmonEvents(){}

    @SubscribeEvent
    public void onBattleEnd(PlayerBattleEndedEvent event) {
        EntityPlayerMP player = event.player;
        String Result = event.result.toString();
        String ply = player.getName();
        String cmd = "broadcast "+ply+" has just won a battle!";
        UUID id = player.getUniqueID();
        Optional<Player> spongePlayer = PlayerDataStorage.getOnlinePlayer(id);

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
        if(!name1.isEmpty())
            game.getCommandManager().process(getConsoleSrc(),"broadcast "+name1+" is fighting "+name2);
    }


}
