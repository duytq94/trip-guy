package com.dfa.vinatrip.utils;

/**
 * Created by duytq on 9/19/2017.
 */

public class Constants {

    public static final String KEY_USER_AUTH = "khoaluan_vinatrip";
    public static final String LOGENTRIES_APP_KEY = "5c68f295-8f87-4444-9e64-e72483c8f7f7";

    public static final int REQUEST_PERMISSION_VIDEO_CALL = 3;
    public static final int REQUEST_PERMISSION_LOCATION = 2;
    public static final int REQUEST_BACKGROUND = 1;

    public static final int PAGE_SIZE = 20;

    // Chat
    public static final String JOIN_ROOM = "join_room";
    public static final String RECEIVE_MESSAGE = "receive_message";
    public static final String A_USER_JOIN_ROOM = "a_user_join_room";
    public static final String A_USER_LEAVE_ROOM = "a_user_leave_room";
    public static final String EMAIL = "email";
    public static final String SEND_MESSAGE = "send_message";
    public static final int MAX_LENGTH_MESSAGE = 1000;

    // Location
    public static final String A_USER_TURN_OFF = "a_user_turn_off";
    public static final String RECEIVE_LOCATION = "receive_location";
    public static final String SEND_LOCATION = "send_location";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    // FireBase storage
    public static final String URL_STORAGE = "gs://tripguy-10864.appspot.com";
    public static final String FOLDER_STORAGE_CHAT = "Chat";
    public static final String FOLDER_AVATAR_USER = "AvatarProfileUser";

    // Trend
    public static final String MY_TOUR = "mytour";

    // Time
    public static final int MILLISECOND_IN_DAY = 86400000;
    public static final String FORMAT_DAY_VN = "dd/MM/yyyy";

    // Friend
    public static final String MAKE_REQUEST = "MakeRequest";
    public static final String CANCEL_REQUEST = "CancelRequest";
    public static final String UN_FRIEND = "Unfriend";
    public static final String ACCEPT_REQUEST = "AcceptRequest";

    // Plan
    public static final int REQUEST_MAKE_PLAN = 3;
    public static final String KEY_NEW_PLAN = "newPlan";

    // Video call
    public static final String SIGN_OUT_VIDEO_CALL = "SignOutVideoCall";

    public static final int TYPE_EVENT = 101;
    public static final int TYPE_HOTEL = 102;
    public static final int TYPE_FOOD = 103;
    public static final int TYPE_PLACE = 104;

    public static final int FEEDBACK_PAGE_SIZE = 5;
}
