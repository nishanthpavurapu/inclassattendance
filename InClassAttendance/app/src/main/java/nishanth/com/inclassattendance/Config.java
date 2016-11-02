package nishanth.com.inclassattendance;

import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;

/**
 * Created by nisha on 3/23/2016.
 */
public class Config {

    //URL to our login.php file
    public static final String LOGIN_URL = "http://www.inclassattendance.16mb.com/UserRegistration/loginapp.php";

    //Keys for username and password as defined in our $_POST['key'] in login.php
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "inclassattendanceapp";

    //This would be used to store the username of current logged in user
    public static final String USERNAME_SHARED_PREF = "username";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}