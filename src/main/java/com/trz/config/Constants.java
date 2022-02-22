package com.trz.config;

public final class Constants {

    public static final String POINTS_DOES_NOT_MATCH = "the trade can't be completed the the price in points doesn't match";
    public static final String DO_NOT_HAVE_ITEM = "%s doesn't have %s, the trade can't be completed";
    public static final String DO_NOT_HAVE_ENOUGH_ITEM = "%s doesn't have enough %s, the trade can't be completed";
    public static final String SURVIVOR_TRADING_IS_A_ZOMBIE = "can't complete the trade, %s is now on the zombie team";
    public static final String SURVIVOR_TRADING_NOT_FOUND = "one of the parts in this trade does not exist in database";
    public static final String SURVIVOR_SELF_REPORT_NOT_ALLOWED = "you can't report yourself";
    public static final String DOUBLE_REPORT_NOT_ALLOWED = "you already report mark this person as a zombie";
    public static final String SURVIVOR_REPORTS = "this survivor has been marked as a zombie %d times";
    public static final String SURVIVOR_IS_A_ZOMBIE = "this survivor is on the dead side now, don`t hangout with him";
    public static final String ITEM_NOT_FOUND = "%s not found in database, make sure you're writing everything correct";
    public static final String SURVIVOR_NOT_FOUND = "survivor not found in database";
    public static final String LATITUDE_OUT_OF_BOUND = "there's no such a place with this latitude";
    public static final String LONGITUDE_OUT_OF_BOUND = "there's no such a place with this longitude";
    public static final String SURVIVOR_IS_UNDERAGE = "unfortunately minors can't be considered survivors in this apocalypse";

    private Constants() {
    }
}
