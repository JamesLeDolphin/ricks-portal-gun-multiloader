package com.jdolphin.ricksportalgun.common.util;

public class PGPacketType {
    private PacketType type;
    private Object arg1;
    private Object arg2;

    public PGPacketType(PacketType type, Object arg1, Object arg2) {
        this.type = type;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public PGPacketType(PacketType type, Object arg1) {
        this(type, arg1, null);
    }

    public PacketType getType() {
        return type;
    }

    public Object getArg1() {
        return arg1;
    }

    public Object getArg2() {
        return arg2;
    }

    public enum PacketType {
        OPEN_GUI,
        CHANGE_GUN_TYPE,
        COLOUR,
        COORD_CHECK,
        DESTINATION_SET,
        LOCATE_PLAYER,
        MANAGE_WAYPOINTS,
        SETTINGS,
        ;
    }
}
