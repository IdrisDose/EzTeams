package net.idrisdev.mc.ezteams.core.data;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.config.Config;
import net.idrisdev.mc.ezteams.core.Core;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.core.entities.Team;
import net.idrisdev.mc.ezteams.utils.Utils;
import org.slf4j.Logger;

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

    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;

    public DAO() {}

    public void initDB(){
        try {

            conn=getConnection();
            st = conn.createStatement();
            rs = conn.getMetaData().getTables(null, null, playerTable, null);
            if (!rs.next()) {
                Utils.logger.error("Players table doesn't exist, making table");
                //Temporarily ID is Varchar for later testing
                st.execute("CREATE TABLE IF NOT EXISTS PLAYERS(UUID VARCHAR(36), NAME VARCHAR(255), TEAM INT(1), POINTS INT(10), PRIMARY KEY(UUID));");
                Utils.logger.info("Made new PLAYER table.");
            }

            conn=getConnection();
            st=conn.createStatement();
            rs = conn.getMetaData().getTables(null,null,teamTable, null );
            if(!rs.next()){
                Utils.logger.error("Teams table doesn't exist, making new table");
                st.execute("CREATE TABLE IF NOT EXISTS TEAMS(ID INT(10), NAME VARCHAR(25), POINTS INT(10), PREFIX VARCHAR(200), PRIMARY KEY(ID));");
                Utils.logger.info("Made new TEAMS table");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().error("Shits fuck in "+DAO.class.getName());
        }
    }
    public Connection getConnection() {
        try {
            if (conn!=null && !conn.isClosed()) {
                logger.info("Connection is not closed.");
                return conn;
            } else {

                File e = conf.toFile();
                if (!e.isDirectory())
                    conf.toFile().mkdir();

                logger.info("Loading database driver.");
                Class.forName("org.h2.Driver");
                logger.info("Establishing connection.");


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
            FileOutputStream fos2 = null;
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

            ResultSet resultSet = st.executeQuery(arguments);

            Core.debug("Closing connections.");

            rs.close();
            conn.close();
            st.close();

            Core.debug("Connections Closed.");

            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                Core.debug("ERROR OCCURRED executeQuery, Connection Closing.");

                if(rs!=null)
                    rs.close();
                if(conn!=null)
                    conn.close();
                if(st!=null)
                    st.close();

                Core.debug("ERROR OCCURRED executeQuery, Connection Closed.");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

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
            try {

                if (rs != null) {
                    rs.close();
                }

                if (st != null) {
                    st.close();
                }

                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                plugin.getLogger().error(DAO.class.getName()+" is broken, report to developer!");
            }
        }
    }
    public Member getMemberData(String uuid){
        //ResultSet rs = executeQuery("select name,team,points from players where uuid='"+uuid+"'");
        ResultSet rs = executeQuery("SELECT NAME,TEAM,POINTS FROM PLAYERS WHERE UUID = '"+uuid+"'");
        String name = "";
        int team = 0;
        int points = 0;

        try {
            if(rs.next()) {
                name = rs.getString("NAME");
                team = rs.getInt("TEAM");
                points = rs.getInt("POINTS");
            }

            Core.debug("getMemData: Connection Closing.");

            rs.close();
            conn.close();
            st.close();

            Core.debug("getMemData: Connection Closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Member(uuid,name,team,points);
    }
    public List<Team> getTeams(){
        ResultSet rs = executeQuery("SELECT * FROM TEAMS");
        List<Team> tmpList = new ArrayList<>();
        try {
            while(rs.next()){
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                int points = rs.getInt("POINTS");
                String prefix = rs.getString("PREFIX");
                tmpList.add(new Team(id,name,points,prefix));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tmpList;
    }
    public List<Member> getMembers(){
        ResultSet rs = executeQuery("SELECT * FROM PLAYERS");
        List<Member> tmpList = new ArrayList<>();
        try {
            while(rs.next()){
                String id = rs.getString("UUID");
                String name = rs.getString("NAME");
                int team = rs.getInt("TEAM");
                int points = rs.getInt("POINTS");
                tmpList.add(new Member(id,name,team,points));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tmpList;
    }
    public boolean ifExists(String query){
        ResultSet rs= executeQuery(query);
        try {
            return rs.next()?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (rs != null) {
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
        PreparedStatement pst = null;
        try {
            if (conn == null || conn.isClosed())
                conn = getConnection();


            pst = conn.prepareStatement("UPDATE PLAYERS SET NAME=?,TEAM=?,POINTS=? WHERE UUID=?");
            pst.setString(1,name);
            pst.setInt(2,team);
            pst.setInt(3,points);
            pst.setString(4,uuid);

            int i = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {

                if (rs != null) {
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

            pst = conn.prepareStatement("INSERT INTO PLAYERS (UUID,NAME,TEAM,POINTS) VALUES(?,?,?,?)");
            pst.setString(1,uuid);
            pst.setString(2,name);
            pst.setInt(3,team);
            pst.setInt(4,points);

            int i = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (rs != null) {
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

        PreparedStatement pst;
        try {
            if(conn == null || conn.isClosed())
                conn = getConnection();

            pst = conn.prepareStatement("UPDATE TEAM SET NAME=?,POINTS=? WHERE id=?");
            pst.setString(1,name);
            pst.setInt(2,points);
            pst.setInt(3,id);

            int i = pst.executeUpdate();
            Core.debug("Team saving returned with result: "+i);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(DAO.class.getName()+" is broken, report to developer!");
        }
    }

    public void saveAll() {
        plugin.getTeams().forEach(this::saveTeam);
        plugin.getOnlineMembers().forEach(member -> member.savePlayer());
    }
}
