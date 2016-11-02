package nishanth.com.inclassattendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nisha on 4/28/2016.
 */
public class EnrollClass extends AppCompatActivity {
    Spinner yearPicker,semesterPicker;
    SharedPreferences sharedPreferences;
    String userEmail,userID;
    EditText editClassID;

    private static final String ENROLL_URL="http://www.inclassattendance.16mb.com/UserRegistration/enrollclass.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enroll_class);

        yearPicker = (Spinner) findViewById(R.id.classYearPicker);
        semesterPicker = (Spinner)findViewById(R.id.spinnerSemester);
        editClassID = (EditText)findViewById(R.id.editClassID);
        Button enrollButton = (Button)findViewById(R.id.buttonEnrollClass);

        sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userEmail=sharedPreferences.getString("useremail", null);
        userID=sharedPreferences.getString("userid",null);

        populate_years();

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enroll();
            }
        });
    }

    private void populate_years()
    {
        int curr_year= Calendar.getInstance().get(Calendar.YEAR);
        ArrayList<Integer> yearsArray=new ArrayList<Integer>();
        yearsArray.add(curr_year);
        for(int i=1;i<6;i++)
        {
            yearsArray.add(curr_year+i);
        }
        ArrayAdapter<Integer> yearsArrayAdapter = new ArrayAdapter<Integer>(EnrollClass.this,android.R.layout.simple_list_item_1,yearsArray);
        yearPicker.setAdapter(yearsArrayAdapter);
    }

    private void enroll()
    {
        String classID,semester,year_selected;
        semester = semesterPicker.getSelectedItem().toString();
        classID = editClassID.getText().toString();
        MyDBHandler myDBHandler=new MyDBHandler(getBaseContext());
        Cursor cursor=myDBHandler.findClass(classID);
        year_selected = yearPicker.getSelectedItem().toString();
        if (classID != "" && semester != "" && year_selected != "")
        {
            if(cursor.getCount()>0)
            {
                Toast.makeText(EnrollClass.this,"A class has been already enrolled with the same ID",Toast.LENGTH_LONG).show();

            }
            else
            {
                enrollClass(classID, semester, year_selected, userID);
            }

        }
        else
        {
            Toast.makeText(EnrollClass.this,"Please provide complete details!",Toast.LENGTH_LONG).show();
        }
    }

    private void enrollClass(String classID, String Semester, String Year,String userID)
    {
        String urlSuffix = "?classid="+classID+"&semester="+Semester+"&year="+Year+"&userid="+userID;

        class EnrollSemesterClass extends AsyncTask<String,Void,String>
        {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EnrollClass.this,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try
                {
                    URL url= new URL(ENROLL_URL+s);
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
        EnrollSemesterClass enrollSemesterClass = new EnrollSemesterClass();
        enrollSemesterClass.execute(urlSuffix);
        MyDBHandler myDBHandler=new MyDBHandler(getBaseContext());
        myDBHandler.addClass(classID,Semester,Year,userID);

    }
}
