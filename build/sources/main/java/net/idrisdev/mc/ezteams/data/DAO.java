package net.idrisdev.mc.ezteams.data;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.utils.ETUtils;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.commented.SimpleCommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Idris on 9/10/2016.
 */
public class DAO extends ETUtils {
    private final static Path conf = Paths.get("config").resolve(NAME).resolve("database");
    private final static Path dbFile = conf.resolve("ezteams.db");
    private final static Path dbDriver = conf.resolve("sqlite-jdbc-3.14.2.1.jar");
    static Connection con;
    static boolean madeConnection = false;
    private static Statement stmt;
    private HoconConfigurationLoader loader;
    private CommentedConfigurationNode root = SimpleCommentedConfigurationNode.root(ConfigurationOptions.defaults().setHeader(ETUtils.CONFIG_HEADER));
    private ObjectMapper<ConfigBase>.BoundInstance configMapper;
    private ConfigBase configBase;

    public DAO() {
    }

    public static Connection getConnection() {
        try {
            if (madeConnection && !con.isClosed()) {
                return con;
            } else {
                if (!madeConnection) {
                    File e = conf.toFile();
                    if (!e.isDirectory())
                        conf.toFile().mkdir();

                    File databaseFile = dbFile.toFile();
                    if (databaseFile.exists()) {
                        databaseFile.delete();
                    }

                    logger.info("Loading database driver.");
                    Class.forName("org.sqlite.JDBC");
                    logger.info("Establishing connection.");
                }

                if (!dbDriver.toFile().exists()) {
                    copyDriverFromJar();
                }

                con = DriverManager.getConnection("jdbc:sqlite:./config/ezteams/database/ezteams.db");
                madeConnection = true;
                return con;
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.info("Could not get a connection to database.");
            e.printStackTrace();
            return null;
        }
    }

    private static void copyDriverFromJar() {
        try {
            logger.info("Extracting driver.");
            InputStream e = EzTeams.class.getClassLoader().getResourceAsStream("sqlite-jdbc-3.14.2.1.jar");
            FileOutputStream fos2 = null;
            fos2 = new FileOutputStream("./config/ezteams/database/sqlite-jdbc-3.14.2.1.jar");
            byte[] buf = new byte[2048];

            for (int r = e.read(buf); r != -1; r = e.read(buf)) {
                fos2.write(buf, 0, r);
            }

            fos2.close();
        } catch (Exception var4) {
            logger.info("Failed to extract driver.");
        }

    }
}
