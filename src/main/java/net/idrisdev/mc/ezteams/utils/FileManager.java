package net.idrisdev.mc.ezteams.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Created by Idris on 8/10/2016.
 */
public class FileManager extends Utils {


    public FileManager() {}

    public void init(){
        Path conf = Paths.get("config").resolve(NAME);

        /*
        Path confFilePath = conf.resolve(NAME +".conf");
        File confFile = confFilePath.toFile();
        try {
            URL url = getClass().getClassLoader().getResource(NAME +".conf");
            checkDir(conf);
            checkFile(confFilePath);
            checkFileContents(confFile,url);
            logger.info("All files exist and ready!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }
    public void checkDir(Path path){
        String name = String.valueOf(path);
        if (!Files.exists(path)) {
            logger.error(name+" directory doesn't exist!");
            path.toFile().mkdirs();
            logger.info("Directory made!");
        }
    }

    public void checkFile(Path file) throws IOException {
        String name = String.valueOf(file.getFileName());
        if (!Files.exists(file)) {
            logger.error(name+" Not found!... Making "+name);
            logger.info("Making "+name);
            Files.createFile(file);
        }
    }

    public void checkFileContents(File file,URL url) throws IOException {
        if(!file.exists()){
            logger.info("Filling file from "+String.valueOf(file));
            FileUtils.copyURLToFile(url,file);
            logger.info("Config creation done!");
        }
    }

    public boolean fileExists(Path file){
        return Files.exists(file);
    }
}
