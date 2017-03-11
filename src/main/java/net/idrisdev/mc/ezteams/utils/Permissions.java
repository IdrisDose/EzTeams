package net.idrisdev.mc.ezteams.utils;

import java.util.List;

/**
 * Created by Idris on 22/01/2017.
 */
public class Permissions {
    // TEAMS
    public static final String TEAMS_BASE = Utils.PERM_NAME +".base";
    public static final String TEAMS_LEAVE = Utils.PERM_NAME +".base.leave";
    public static final String TEAMS_JOIN = Utils.PERM_NAME +".base.join";
    public static final String TEAMS_LIST = Utils.PERM_NAME +".base.list";

    // POINTS
    public static final String TEAMS_POINTS_BASE = Utils.PERM_NAME +".points";
    public static final String TEAMS_POINTS_REMOVE = Utils.PERM_NAME +".points.remove";
    public static final String TEAMS_POINTS_ADD = Utils.PERM_NAME +"points.add";
    public static final String TEAMS_POINTS_VIEW = Utils.PERM_NAME+".view";

    // MEMBER
    public static final String TEAMS_MEMBER_BASE = Utils.PERM_NAME +".member.base";
    public static final String TEAMS_MEMBER_COUNT = Utils.PERM_NAME +".member.list";

    // ADMIN
    public static final String TEAMS_ADMIN = Utils.PERM_NAME +".admin.base";
    public static final String TEAMS_ADMIN_REMOVE = Utils.PERM_NAME +".admin.remove";
    public static final String TEAMS_ADMIN_ADD = Utils.PERM_NAME +".admin.add";
    public static final String TEAMS_ADMIN_TEAM_POINS = Utils.PERM_NAME +".admin.team.points";
    public static final String TEAMS_ADMIN_MEMBER_POINS = Utils.PERM_NAME +".admin.member.points";
    public static final String TEAMS_ADMIN_LIST_TEAMS = Utils.PERM_NAME +".admin.list";

    // SUDO
    public static final String TEAMS_SUDO = Utils.PERM_NAME +".sudo.base";
    public static final String TEAMS_SUDO_REMOVE_TEAM = Utils.PERM_NAME+".sudo.team.remove";
    public static final String TEAMS_SUDO_ADD_TEAM = Utils.PERM_NAME +".sudo.team.add";
    public static final String TEAMS_SUDO_RESET = Utils.PERM_NAME+".sudo.reset";

    // OTHER
    public static List<String> names = java.util.Arrays.asList("Obeliskthegreat", "Idris_", "Ozzybuns");
    public static final String TEAMS_JOIN_STAFF = Utils.PERM_NAME+".team.staff";
}
