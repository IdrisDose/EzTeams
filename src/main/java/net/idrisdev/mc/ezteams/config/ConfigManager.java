package net.idrisdev.mc.ezteams.config;

import net.idrisdev.mc.ezteams.EzTeams;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Created by Idris on 30/12/2016.
 */
public class ConfigManager {
    private EzTeams plugin = EzTeams.get();
    private Logger logger = plugin.getLogger();

    private final ConfigurationLoader<CommentedConfigurationNode> configManager;
    private final File defaultFile;

    private ObjectMapper<Config>.BoundInstance configMapper;
    private CommentedConfigurationNode rootNode;


    public ConfigManager(ConfigurationLoader<CommentedConfigurationNode> configManager, File defaultFile) {
        this.configManager = configManager;
        this.defaultFile = defaultFile;

        try{
            configMapper = ObjectMapper.forClass(Config.class).bindToNew();
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        if(!defaultFile.exists()){
            try{
                logger.info("CREATING NEW FILE @ "+defaultFile.getPath());
                defaultFile.createNewFile();
            } catch (IOException e) {
                logger.error("Error when creating config file.", e);
                return;
            }
        }

        rootNode = configManager.createEmptyNode();
        if(configMapper != null){
            try{
                rootNode = configManager.load();

                configMapper.populate(rootNode);
                configMapper.serialize(rootNode);

                configManager.save(rootNode);

            } catch (IOException e) {
                logger.error("Error saving the configuration", e);
            } catch (ObjectMappingException objMappingE) {
               logger.error("Error loading the configuration", objMappingE);
            }
        }
    }

    public void save(){
        if(configMapper != null && rootNode != null){
            try{
                configMapper.serialize(rootNode);
                configManager.save(rootNode);
            } catch (ObjectMappingException objMappingE) {
                logger.error("Error loading the configuration", objMappingE);
            } catch (IOException e) {
                logger.error("Error saving the configuration", e);
            }
        }
    }

    public Config getConfig(){ return configMapper.getInstance()!=null?configMapper.getInstance():null; }
}
