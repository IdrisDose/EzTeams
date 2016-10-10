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
import java.sql.*;

/**
 * Created by Idris on 9/10/2016.
 */
public class DAOH2 extends ETUtils {
    private final static Path conf = Paths.get("config").resolve(NAME).resolve("database");
    private final static Path dbFile = conf.resolve("ezteams.trace.db");
    private final static Path dbDriver = conf.resolve("h2-1.3.173.jar");
    public static Connection con;
    static boolean madeConnection = false;
    private static Statement stmt;
    private HoconConfigurationLoader loader;
    private CommentedConfigurationNode root = SimpleCommentedConfigurationNode.root(ConfigurationOptions.defaults().setHeader(ETUtils.CONFIG_HEADER));
    private ObjectMapper<ConfigBase>.BoundInstance configMapper;
    private ConfigBase configBase;

    public DAOH2() {
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
                    Class.forName("org.h2.Driver");
                    logger.info("Establishing connection.");
                }

                if (!dbDriver.toFile().exists()) {
                    copyDriverFromJar();
                }

                con = DriverManager.getConnection("jdbc:h2:./config/ezteams/database/ezteams;MVCC=true");
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
            InputStream e = EzTeams.class.getClassLoader().getResourceAsStream("h2-1.3.173.jar");
            FileOutputStream fos2 = null;
            fos2 = new FileOutputStream("./config/ezteams/database/sh2-1.3.173.jar");
            byte[] buf = new byte[2048];

            for (int r = e.read(buf); r != -1; r = e.read(buf)) {
                fos2.write(buf, 0, r);
            }

            fos2.close();
        } catch (Exception var4) {
            logger.info("Failed to extract driver.");
        }

    }

    public static ResultSet executeQuery(String arguments) {
        Statement st;
        try {
            if (con == null || con.isClosed())
                con = getConnection();
            st = con.createStatement();

            ResultSet resultSet = st.executeQuery(arguments);
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int executeUQuery(String arguments) {
        Statement st;
        try {
            if (con == null || con.isClosed())
                con = getConnection();

            st = con.createStatement();
            int i = st.executeUpdate(arguments);
            con.close();
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
