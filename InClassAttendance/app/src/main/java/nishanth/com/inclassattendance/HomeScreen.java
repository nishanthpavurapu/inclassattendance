package nishanth.com.inclassattendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nisha on 3/24/2016.
 */
public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    IntentIntegrator intentIntegrator;
    public String AttendanceURL,deviceID;
    SharedPreferences sharedPreferences;
    public String userEmail,userID,userName;
    ArrayList<String> classListAdapter = new ArrayList<>();
    public Intent intent;
    public String classid,attenededdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        Button buttonClick = (Button)findViewById(R.id.buttonLaunch);
        Button buttonViewAttendance = (Button)findViewById(R.id.buttonViewAttendance);
        buttonViewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClassList(userID);
            }
        });

        TextView welcomeText = (TextView)findViewById(R.id.welcomeTitle);

        MyDBHandler myDBHandler = new MyDBHandler(getBaseContext());
        SQLiteDatabase db = myDBHandler.getWritableDatabase();
        db.close();
        myDBHandler.close();

        buttonClick.setOnClickListener(this);
        intentIntegrator = new IntentIntegrator(this);

        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setPrompt("Scan a QR Code");
        intentIntegrator.setCameraId(0);  // Use a specific camera of the device
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setBarcodeImageEnabled(true);

        deviceID = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

        sharedPreferences = HomeScreen.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userEmail=sharedPreferences.getString("useremail",null);
        userID=sharedPreferences.getString("userid",null);
        userName=sharedPreferences.getString("username",null);

        welcomeText.setText("Welcome "+userName+" !");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem logout = menu.findItem(R.id.action_logout);
        logout.setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout)
        {
            logOutUser();
        }
        return true;
    }

    public void logOutUser()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();

        Intent intent = new Intent(HomeScreen.this,StartScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
    intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (scanResult.getContents()!=null)
        {
            String res = requestCode+"-"+resultCode+"-"+scanResult.getContents();
            AttendanceURL=scanResult.getContents();
            AttendanceURL=AttendanceURL+"&studentid="+userID+"&deviceid="+deviceID;

            try {
                if (!AttendanceURL.isEmpty()) {
                    String temp = AttendanceURL;
                    String[] uriItems = temp.split("\\?");
                    String uriValueString = uriItems[1];
                    String[] uriValues = uriValueString.split("&");
                    classid = uriValues[2].replace("classid=", "");
                    attenededdate = uriValues[0].replace("dateselected=", "");
                }
                MyDBHandler myDBHandler = new MyDBHandler(getBaseContext());
                Cursor cursor = myDBHandler.verifyAttendance(classid, attenededdate);
                if (cursor.getCount() > 0) {
                    Toast.makeText(HomeScreen.this, "Attendance already registered for this class", Toast.LENGTH_SHORT).show();
                } else {
                    RegisterAttendance();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }

    private void RegisterAttendance()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AttendanceURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),"Received Response"+response,Toast.LENGTH_LONG);
                if (response.contains("success")) {
                    MyDBHandler myDBHandler=new MyDBHandler(getBaseContext());
                    myDBHandler.addAttendace(classid,userID,attenededdate,deviceID);
                    Toast.makeText(getApplicationContext(), "Attendance registered Successfully!", Toast.LENGTH_LONG).show();
                    myDBHandler.close();
                } else {
                    Toast.makeText(getApplicationContext(), "Attendance registration failed, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("volley error",error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void getClassList(String userID)
    {
        String classid = "000";
        String url = "http://www.inclassattendance.16mb.com/UserRegistration/attendance_retrieve.php";
        String params = "?studentid="+userID+"&classid="+classid+"&method=classlist";

        class  GetClasses extends AsyncTask<String,Void,ArrayList<String>>
        {

            @Override
            protected ArrayList<String> doInBackground(String... params) {
                String urlString = params[0];
                BufferedReader bufferedReader = null;

                try
                {
                    URL url= new URL(urlString);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;
                    result = bufferedReader.readLine();

                    String[] classItems = result.split(";");
                    classListAdapter.clear();
                    for(int i=0;i<classItems.length;i++)
                    {
                        classListAdapter.add(i,classItems[i]);
                    }
                    return classListAdapter;
                }
                catch (Exception e)
                {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ArrayList<String> classListAdapter) {
                super.onPostExecute(classListAdapter);
                classListAdapter.add(0, "None");
                intent = new Intent(HomeScreen.this,ViewAttendance.class);
                intent.putStringArrayListExtra("classArrayList", classListAdapter);
                startActivity(intent);
                //arrayAdapter = new ArrayAdapter<String>(HomeScreen.this,android.R.layout.simple_list_item_1,classListAdapter);
            }
        }
        GetClasses getClasses = new GetClasses();
        getClasses.execute(url + params);
    }
}
