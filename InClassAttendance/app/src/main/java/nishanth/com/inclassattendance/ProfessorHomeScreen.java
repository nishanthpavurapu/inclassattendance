package nishanth.com.inclassattendance;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nisha on 4/23/2016.
 */
public class ProfessorHomeScreen extends AppCompatActivity{
    public Button buttonViewAttendance,buttonEnrollClass,buttonMyClasses;
    public String calledMethod;
    public Intent intent;
    ArrayList<String> classListAdapter = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String userID,userEmail,userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_home_screen);

        TextView welcomeText = (TextView)findViewById(R.id.welcomeTitle);

        sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userEmail=sharedPreferences.getString("useremail", null);
        userID=sharedPreferences.getString("userid", null);
        userName=sharedPreferences.getString("username",null);

        welcomeText.setText("Welcome "+userName+" !");

        buttonViewAttendance = (Button)findViewById(R.id.buttonViewAttendance);
        buttonViewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calledMethod="viewattendance";
                getClassList(userID);
            }
        });

        buttonEnrollClass = (Button)findViewById(R.id.buttonEnrollClass);
        buttonEnrollClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ProfessorHomeScreen.this,EnrollClass.class);
                startActivity(intent);
            }
        });

        buttonMyClasses = (Button)findViewById(R.id.buttonMyClasses);
        buttonMyClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calledMethod="viewmyclasses";
                getClassList(userID);
            }
        });

    }

    public void getClassList(String userID)
    {
        String classid = "000";
        String url = "http://www.inclassattendance.16mb.com/UserRegistration/attendance_retrieve.php";
        String params = "?studentid="+userID+"&classid="+classid+"&method=classlistprofessor";

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
                if (calledMethod.equalsIgnoreCase("viewmyclasses"))
                {
                    intent = new Intent(ProfessorHomeScreen.this,ViewProfessorClasses.class);
                }
                else
                {
                    classListAdapter.add(0, "None");
                    intent = new Intent(ProfessorHomeScreen.this,ViewAttendanceProfessor.class);
                }
                intent.putStringArrayListExtra("classArrayList", classListAdapter);
                startActivity(intent);
                //arrayAdapter = new ArrayAdapter<String>(HomeScreen.this,android.R.layout.simple_list_item_1,classListAdapter);
            }
        }
        GetClasses getClasses = new GetClasses();
        getClasses.execute(url + params);
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

        Intent intent = new Intent(ProfessorHomeScreen.this,StartScreen.class);
        startActivity(intent);
        finish();
    }
}
