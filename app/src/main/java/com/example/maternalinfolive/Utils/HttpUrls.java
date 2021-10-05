package com.example.maternalinfolive.Utils;

public class HttpUrls {
    private final static  String local_server_address= "http://172.16.0.118:8080/onhealth/mobile/";
    private final static  String remote_server_address= "https://demagp.com/onhealth/mobile/";

    private final  static String server_address = ""+remote_server_address;
    public  final  static String AUTHENTICATION_SERVER_PATH = server_address+ "user_login.php";
    public  final  static  String VERIFY_CODE_PATH = server_address+ "manage_code.php";
    public  final  static  String VERIFY_CODE__FINAL_PATH = server_address+ "verify_code.php";
    public  final  static  String SAVE_PROFILE_PATH = server_address+ "save_profile.php";
    public  final  static  String URL_FETCH_MESSAGES = server_address+ "messages.php";
    public  final  static  String SAVE_MESSAGE_TO_SINGLE_USER = server_address+ "save_message.php";
    public  final  static  String MESSAGE_IMAGE_FILE_PATH = server_address+ "message/files/";
    public  final  static  String NEW_POST = server_address+ "new_post.php";
    public  final  static  String VIEW_POST = server_address+ "view_info.php";
    public  final  static  String VIEW_MESSAGES_ADMIN = server_address+ "view_messages_admin.php";

    public  final  static  String VIEW_USERS = server_address+ "view_users.php";
    public  final  static  String REPLY_MESSAGE = server_address+ "reply_message.php";

    public  final  static  String FIND_RANKING = server_address+ "find_ranking.php";

    public  final  static  String SAVE_RANKING = server_address+ "save_ranking.php";

    public  final  static  String TRACKER_PATH = server_address+ "tracker.php";

    public  final  static  String RECOMMENDATION_PATH = server_address+ "recommended_info.php";

    public  final  static  String DELETE_PATH = server_address+ "delete.php";

    public  final  static  String UPDATE_ROLE = server_address+ "new_role.php";

}
