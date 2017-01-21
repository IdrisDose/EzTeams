package net.idrisdev.mc.ezteams.data;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

/**
 * Created by Idris on 9/10/2016.
 */
public class DAOH2 extends Utils {
    private final static String h2name="h2-1.4.191.jar";
    private final static Path conf = Paths.get("config").resolve(NAME).resolve("database");
    private final static Path dbFile = conf.resolve("ezteams.trace.db");
    private final static Path dbDriver = conf.resolve(h2name);
    public static Connection con;
    static boolean madeConnection = false;
    private static Statement stmt;
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
            InputStream e = EzTeams.class.getClassLoader().getResourceAsStream(h2name);
            FileOutputStream fos2 = null;
            fos2 = new FileOutputStream("./config/ezteams/database/"+h2name);
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
