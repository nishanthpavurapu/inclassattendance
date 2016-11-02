package nishanth.com.inclassattendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.audiofx.BassBoost;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextStudentID;
    private EditText editTextDepartment;
    private EditText editTextMobile;
    private EditText editTextEmail;
    private EditText editTextPassword;
    public String deviceID;
    public RadioGroup userType;

    private Button buttonSubmit;

    private static final String REGISTER_URL="http://www.inclassattendance.16mb.com/UserRegistration/registration.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextStudentID = (EditText)findViewById(R.id.editTextStudentid);
        editTextDepartment = (EditText)findViewById(R.id.editTextDepartment);
        editTextMobile = (EditText)findViewById(R.id.editTextmobile);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);

        userType = (RadioGroup) findViewById(R.id.userType);

        deviceID = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);


        buttonSubmit = (Button) findViewById(R.id.buttonRegister);

        buttonSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == buttonSubmit)
        {
            registerUser();
        }
    }

    private void registerUser()
    {
        String name = editTextName.getText().toString().trim().toLowerCase();
        String studentId = editTextStudentID.getText().toString().trim().toLowerCase();
        String department = editTextDepartment.getText().toString().trim().toLowerCase();
        String mobile = editTextMobile.getText().toString().trim().toLowerCase();
        String email = editTextEmail.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim().toLowerCase();
        String userTypeString;

        if (userType.getCheckedRadioButtonId() == 0)
        {
            userTypeString= "student";
        }
        else
        {
            userTypeString="professor";
        }

        register(name,studentId,department,mobile,email,password,userTypeString);
    }

    private void register(String name, String studentId, String department, String mobile, String email, String password, String userTypeString)
    {
        String urlSuffix = "?name="+name+"&studentid="+studentId+"&department="+department+"&mobile="+mobile+"&email="+email+"&password="+password+"&deviceid="+deviceID+"&usertype="+userTypeString;

        class RegisterUser extends AsyncTask<String,Void,String>
        {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try
                {
                    URL url= new URL(REGISTER_URL+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;
                    result = bufferedReader.readLine();
                    return result;
                }
                catch (Exception e)
                {
                    return null;
                }
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }
}
