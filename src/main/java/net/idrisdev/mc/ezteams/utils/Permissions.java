package net.idrisdev.mc.ezteams.utils;

import java.util.List;

/**
 * Created by Idris on 22/01/2017.
 */
public class Permissions {
    // TEAMS
    public static final String TEAMS_BASE = Utils.NAME +".base";
    public static final String TEAMS_LEAVE = Utils.NAME +".leave";
    public static final String TEAMS_JOIN = Utils.NAME +".join";
    public static final String TEAMS_LIST = Utils.NAME +".list";

    // POINTS
    public static final String TEAMS_POINTS_BASE = Utils.NAME +".points";
    public static final String TEAMS_POINTS_REMOVE = Utils.NAME +".points.remove";
    public static final String TEAMS_POINTS_ADD = Utils.NAME +"points.add";
    public static final String TEAMS_POINTS_VIEW = Utils.NAME+".view";

    // MEMBER
    public static final String TEAMS_MEMBER_BASE = Utils.NAME +".member.base";
    public static final String TEAMS_MEMBER_LIST = Utils.NAME +".member.list";

    // ADMIN
    public static final String TEAMS_ADMIN = Utils.NAME +".admin.base";
    public static final String TEAMS_ADMIN_REMOVE = Utils.NAME +".admin.remove";
    public static final String TEAMS_ADMIN_ADD = Utils.NAME +".admin.add";
    public static final String TEAMS_ADMIN_TEAM_POINS = Utils.NAME +".admin.team.points";
    public static final String TEAMS_ADMIN_MEMBER_POINS = Utils.NAME +".admin.member.points";

    public static final String TEAMS_ADMIN_REMOVE_TEAM = Utils.NAME +".admin.team.remove";
    public static final String TEAMS_ADMIN_ADD_TEAM = Utils.NAME +".admin.team.add";

    // OTHER
    public static List<String> names = java.util.Arrays.asList("Obeliskthegreat", "Idris_", "Ozzybuns");
    public static final String TEAMS_JOIN_STAFF = Utils.NAME +".team.staff";
}
