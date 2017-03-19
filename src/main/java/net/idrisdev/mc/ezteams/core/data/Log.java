package net.idrisdev.mc.ezteams.core.data;


import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.utils.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static net.idrisdev.mc.ezteams.utils.Utils.NAME;

/**
 * Created by Idris on 19/03/2017.
 */
public class Log {

    private final Path conf = Paths.get("config").resolve(NAME);
    private final Path logFile = conf.resolve("log.txt");
    private BufferedWriter bw = null;
    private FileWriter fw = null;
    private EzTeams plugin;

    public Log(EzTeams plugin){
        this.plugin = plugin;
        try {
            createNewLogFile();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewLogFile() throws IOException{
        if(!logFile.toFile().exists()) {
            Utils.logger.info("Log File doesn't exist, creating new one.");
            Files.createFile(logFile);
        } else {
            writeToFile("-------------SERVER RESTARTED------------------");
            writeToFile("\n");
            writeToFile("\n");
        }
    }
    public void info(String msg){
        String time = getCurTime();
        String content = time+" [INFO]: "+msg;
        writeToFile(content);
    }
    public void error(String msg){
        String time = getCurTime();
        String content = time+" [ERROR]: "+msg;
        writeToFile(content);
    }
    public void debug(String msg){
        String time = getCurTime();
        String content = time+" [ERROR]: "+msg;
        writeToFile(content);
    }
    private String getCurTime() {
        //DAY/MONTH
        int DD = LocalDateTime.now().getDayOfMonth();
        int MMM = LocalDateTime.now().getMonth().getValue();

        //HOUR:MIN:SEC
        int HH = LocalDateTime.now().getHour();
        int MM = LocalDateTime.now().getMinute();
        int SS = LocalDateTime.now().getSecond();

        //CurTime

        return "["+DD+"/"+MMM+"]"+"["+HH+":"+MM+":"+SS+"]";
    }
    private void writeToFile(String content){
        try {
            fw = new FileWriter(logFile.toFile(),true);
            bw = new BufferedWriter(fw);
            bw.write("\n"+content);

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(bw != null )
                    bw.close();

                if(fw != null)
                    fw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
