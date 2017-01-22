package net.idrisdev.mc.ezteams.core.data;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.config.Config;
import net.idrisdev.mc.ezteams.core.entities.Member;
import net.idrisdev.mc.ezteams.core.entities.Team;
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

    private final String dbname="mysql-connector-java-6.0.5.jar";
    private final Path conf = Paths.get("config").resolve(NAME).resolve("database");
    private final Path dbDriver = conf.resolve(dbname);
    private static boolean madeConnection = false;

    private String url = config.db.getDbURL();
    private String uname = config.db.getDbUsername();
    private String pword = config.db.getDbPassword();


    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;

    public DAO() {}

    public void initDB(){
        logger.info("Initializing Database Connectivity, ye cunt.");
        try {
            if(plugin.DEBUG)
                logger.info("USERNAME: "+uname+" PWORD: "+pword+" URL: "+url);


            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT VERSION()");

            if(rs.next()){
                logger.info("Version is: " + rs.getString(1));
            } else {

                logger.info("Nothing showed up fam");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().error("Shits fuck in "+DAO.class.getName());
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
                plugin.getLogger().error("Shits fuck in "+DAO.class.getName());
            }
        }
    }

    public Connection getConnection() {
        try {
            if (madeConnection && !conn.isClosed()) {
                return conn;
            } else {
                if (!madeConnection) {
                    File e = conf.toFile();
                    if (!e.isDirectory())
                        conf.toFile().mkdir();

                    logger.info("Loading database driver.");
                    Class.forName("com.mysql.jdbc.Driver");
                    logger.info("Establishing connection.");
                }

                if (!dbDriver.toFile().exists()) {
                    copyDriverFromJar();
                }

                conn = DriverManager.getConnection(url,uname,pword);;
                madeConnection = true;
                return conn;
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

    private ResultSet executeQuery(String arguments) {
        try {
            if (conn == null || conn.isClosed())
                conn = getConnection();

            logger.info("CONN isClosed()??: "+conn.isClosed());
            st = conn.createStatement();

            ResultSet resultSet = st.executeQuery(arguments);

            conn.close();
            st.close();

            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if(conn !=null){
                    conn.close();
                }

                if(st !=null){
                    st.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                logger.error(DAO.class.getName()+" is broken, report to developer!");
            }
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
        ResultSet rs = executeQuery("select name,team,points from players where uuid='"+uuid+"'");
        String name = "";
        int team = 0;
        int points = 0;

        try {
            if(rs.next()) {
                name = rs.getString("name");
                team = rs.getInt("team");
                points = rs.getInt("points");
            }
            rs.close();
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
        return new Member(uuid,name,team,points);
    }
    public List<Team> getTeams(){
        ResultSet rs = executeQuery("select * from teams");
        List<Team> tmpList = new ArrayList<>();
        try {
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int points = rs.getInt("points");
                tmpList.add(new Team(id,name,points));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tmpList;
    }
    public List<Member> getMembers(){
        ResultSet rs = executeQuery("select * from players");
        List<Member> tmpList = new ArrayList<>();
        try {
            while(rs.next()){
                String id = rs.getString("uuid");
                String name = rs.getString("name");
                int team = rs.getInt("team");
                int points = rs.getInt("points");
                tmpList.add(new Member(id,name,team,points));
            }
            rs.close();
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
        logger.info("UPDATING PLAYER DATA!");


        String uuid = member.getUuid();
        String name = member.getName();
        int team = member.getTeam().getId();
        int points = member.getPoints();
        PreparedStatement pst = null;
        try {
            if (conn == null || conn.isClosed())
                conn = getConnection();


            pst = conn.prepareStatement("UPDATE players SET name=?,team=?,points=? where uuid=?");
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
        logger.info("Inserting new player!");

        String uuid = member.getUuid();
        String name = member.getName();
        int team = member.getTeam()==null?1:member.getTeam().getId();
        int points = member.getPoints();
        PreparedStatement pst;
        try {
            if (conn == null || conn.isClosed())
                conn = getConnection();

            pst = conn.prepareStatement("INSERT INTO players (uuid,name,team,points) VALUES(?,?,?,?)");
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
        logger.info("Saving team with data: "+team);
        int id = team.getId();
        String name = team.getName();
        int points = team.getPoints();

        PreparedStatement pst;
        try {
            if(conn == null || conn.isClosed())
                conn = getConnection();

            pst = conn.prepareStatement("UPDATE teams SET name=?,points=? WHERE id=?");
            pst.setString(1,name);
            pst.setInt(2,points);
            pst.setInt(3,id);

            int i = pst.executeUpdate();
            logger.info("Team saving returned with result: "+i);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(DAO.class.getName()+" is broken, report to developer!");
        }
    }

    public void saveAll() {
        plugin.teams.forEach(this::saveTeam);
        plugin.onlineMembers.forEach(member -> member.savePlayer());
    }
}
