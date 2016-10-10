package net.idrisdev.mc.ezteams.data;

import net.idrisdev.mc.ezteams.EzTeams;
import net.idrisdev.mc.ezteams.utils.ETUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Idris on 9/10/2016.
 */
public class DataStorage extends ETUtils {
    private static final String playerTable = "PLAYERDATA";
    private static final String teamTable = "TEAMS";
    private static Statement st;
    private static Connection conn;
    private ResultSet rs;
    private List<TeamData> tmpTeams = new ArrayList<>();

    public DataStorage() {
    }

    public static void insertPlayer(UUID uuid, String name, int team) {
        try {
            if (conn == null || conn.isClosed())
                conn = DAOH2.getConnection();

            logger.info("DS: ADDING PLAYER: "+name+" with ID= "+uuid+" WITH TEAM: "+team);
            PreparedStatement prep = conn.prepareStatement("INSERT INTO PLAYERDATA VALUES(?,?,?);");
            prep.setString(1, ""+uuid);
            prep.setString(2, name);
            prep.setInt(3, team);

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initDS() throws SQLException {
        logInitMsg("DataStorage");
        //had to clear the list because some garbage happened
        EzTeams.teams.clear();
        tmpTeams.clear();
        if(conn == null || conn.isClosed())
            conn = DAOH2.getConnection();
        st = conn.createStatement();
        rs = conn.getMetaData().getTables(null, null, playerTable, null);

        if (!rs.next()) {
            logger.info("DS: Creating " + playerTable);
            st.execute("CREATE TABLE IF NOT EXISTS " + playerTable + "(ID VARCHAR(255), NAME VARCHAR(255), TEAM INTEGER, PRIMARY KEY(id));");
        }
        logger.info("DS: "+playerTable + " EXISTS! ");

        rs = conn.getMetaData().getTables(null, null, teamTable, null);
        if (!rs.next()) {
            logger.info("DS: Creating " + teamTable);
            st.execute("CREATE TABLE IF NOT EXISTS  " + teamTable + "(ID INTEGER, NAME VARCHAR(10), POINTS INTEGER, PRIMARY KEY(id));");
        }
        logger.info("DS: "+teamTable + " EXISTS! ");


        //Execute the query and store in generic resultset
        rs = st.executeQuery("SELECT ID,NAME,POINTS FROM TEAMS");
        //While ResultSet has items fill tmp list with teams
        while (rs.next()) {
            int id = rs.getInt("ID");
            String name = rs.getString("NAME");
            int points = rs.getInt("POINTS");
            if (EzTeams.DEBUG)
                logger.info("ID: " + id + " NAME: " + name + " POINTS: " + points);

            TeamData t = new TeamData(id, name, points);
            tmpTeams.add(t);

        }

        //if the temp list is empty, fill it with generics
        if (tmpTeams.isEmpty()) {
            logger.error("DS: PROBLEM LOADING TEAMS FROM DATABASE!!! - using generic");
            tmpTeams.add(new TeamData(0, "Default", 0));
            tmpTeams.add(new TeamData(1, "Rocket", 0));
            tmpTeams.add(new TeamData(2, "Magma", 0));
            tmpTeams.add(new TeamData(3, "Aqua", 0));
            tmpTeams.add(new TeamData(4, "Plasma", 0));
            tmpTeams.add(new TeamData(5, "Galactic", 0));
            tmpTeams.add(new TeamData(6, "Staff", 0));
            insertTeams(tmpTeams);
        }

        //Add the all from the tmplist to the main list!
        tmpTeams.forEach(team -> {
            EzTeams.teams.add(team);
            logger.info("DS: ADDING: " + team.toString());
        });
        logger.info("DS: ALL TEAMS LOADED!");

        if (EzTeams.DEBUG)
            EzTeams.teams.forEach(team -> sendSrcPlainMessage(getConsoleSrc(), team.toString()));

        logCompMsg("DataStorage");
        conn.close();

    }

    private void insertTeams(List<TeamData> tmpTeams) {
        try {
            if(conn==null||conn.isClosed())
                conn = DAOH2.getConnection();
            tmpTeams.forEach(team -> {
                int id = team.getID();
                String name = team.getNAME();
                int points = team.getPOINTS();

                try {
                    PreparedStatement prep = conn.prepareStatement("INSERT INTO TEAMS VALUES(?,?,?);");
                    prep.setInt(1, id);
                    prep.setString(2,name);
                    prep.setInt(3, points);
                    prep.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public final void syncTeamsDB() {
        try {
            if (conn == null || conn.isClosed())
                conn = DAOH2.getConnection();

            EzTeams.teams.stream().forEach(team -> {
                int id = team.getID();
                int points = team.getPOINTS();

                try {
                    PreparedStatement prep = conn.prepareStatement("UPDATE TEAMS SET POINTS=? WHERE ID=?");
                    prep.setInt(1, points);
                    prep.setInt(2, id);
                    prep.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public final void reloadPlz() {
        try {
            initDS();
        } catch (SQLException e) {
            logger.error("DATABASE INIT FAILED!");
            e.printStackTrace();
        }
    }

    public final void syncPlayersDB() {
        sendNYIMessage(getConsoleSrc());
    }
}
