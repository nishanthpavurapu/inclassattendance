package nishanth.com.inclassattendance;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nisha on 3/23/2016.
 */
public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    EditText LoginUsername,Password;
    Button buttonLogin;
    public static String deviceID;
    public static String userEmail,userID,userType,userName;

    private boolean Loggedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        LoginUsername = (EditText)findViewById(R.id.editLoginUsername);
        Password = (EditText)findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        deviceID = Settings.Secure.ANDROID_ID;

    }


    @Override
    public void onClick(View v) {
        login();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        Loggedin = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF,false);
        String userType = sharedPreferences.getString("usertype","");

        if(Loggedin)
        {
            if(userType.equalsIgnoreCase("student"))
            {
                Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
            else if (userType.equalsIgnoreCase("professor"))
            {
                Intent intent = new Intent(LoginScreen.this, ProfessorHomeScreen.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void login()
    {
        final String username = LoginUsername.getText().toString().trim();
        final String password = Password.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"Received Response"+response,Toast.LENGTH_LONG);
                if (response.contains(Config.LOGIN_SUCCESS)) {

                    String[] responseItems = response.split(",");
                    userEmail=responseItems[1];
                    userID=responseItems[2];
                    userType=responseItems[3];
                    userName=responseItems[4];

                    SharedPreferences sharedPreferences = LoginScreen.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                    editor.putString(Config.USERNAME_SHARED_PREF, username);
                    editor.putString("useremail",userEmail);
                    editor.putString("userid",userID);
                    editor.putString("usertype",userType);
                    editor.putString("username",userName);
                    editor.apply();

                    Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();

                    Intent intent;
                    if (userType.equalsIgnoreCase("student"))
                    {
                        intent = new Intent(LoginScreen.this,HomeScreen.class);
                    }
                    else
                    {
                        intent = new Intent(LoginScreen.this,ProfessorHomeScreen.class);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put(Config.KEY_PASSWORD,password);
                params.put(Config.KEY_USERNAME,username);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
