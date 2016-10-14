package net.idrisdev.mc.ezteams.events;

import com.pixelmonmod.pixelmon.storage.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import static net.idrisdev.mc.ezteams.utils.ETUtils.logger;
import static net.idrisdev.mc.ezteams.utils.ETUtils.names;

/**
 * Created by Idris on 15/10/2016.
 */
public class ForgeEvents {

    public ForgeEvents(){}

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        logger.info("Set onPlayerLogin event!");
        EntityPlayerMP player = (EntityPlayerMP)event.player;
        String name = player.getName();
        PlayerExtraData ped = null;
        PlayerStorage ps = null;

        try {
            //handleJoinForge(player,true);
            if(names.contains(name)) {
                logger.info("The Idris Logged in!");
                PixelmonStorage.PokeballManager.getPlayerStorage(player).getExtraData().isDeveloper = true;
                PixelmonStorage.PokeballManager.getPlayerStorage(player).getExtraData().hasRainbowSash = false;
                PixelmonStorage.PokeballManager.getPlayerStorage(player).getExtraData().hasSash = true;
                PixelmonStorage.PokeballManager.getPlayerStorage(player).getExtraData().hasCap = true;
                PixelmonStorage.PokeballManager.getPlayerStorage(player).getExtraData().setHatType(4);
                //PixelmonStorage.PokeballManager.savePlayer(ps);
                //PlayerExtras.instance.getExtras(player).isDeveloper=true;
                //PlayerExtras.instance.getExtras(player).hasRainbowSash=true;
            }

        } catch (PlayerNotLoadedException e) {
            e.printStackTrace();
        }

    }

}
