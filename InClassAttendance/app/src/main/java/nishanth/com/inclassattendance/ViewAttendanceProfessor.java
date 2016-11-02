package nishanth.com.inclassattendance;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by nisha on 4/27/2016.
 */
public class ViewAttendanceProfessor extends AppCompatActivity {
    public EditText classDate;
    public Spinner classList;
    public ListView attendanceListView;
    public String userID;
    SharedPreferences sharedPreferences;
    Calendar myCalendar;
    ArrayList<String> classListAdapter = new ArrayList<>();
    ArrayList<String> attendanceListAdapter = new ArrayList<>();
    ArrayAdapter<String> attendanceArrayAdapter;
    String[] proxyAttendanceListItems;

    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewattendance_prof);

        classList = (Spinner)findViewById(R.id.classDropDown);
        classDate = (EditText)findViewById(R.id.editTextClassDate);
        attendanceListView = (ListView)findViewById(R.id.attendanceListView);
        Button buttonLoad = (Button)findViewById(R.id.buttonLoad);
        Button buttonProxy = (Button)findViewById(R.id.buttonProxy);

        sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userID=sharedPreferences.getString("userid", null);

        myCalendar = Calendar.getInstance();

        buttonProxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(proxyAttendanceListItems==null))
                {
                    updateProxyList();
                }
            }
        });

        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classList.getSelectedItem().toString().equalsIgnoreCase("none")||classList.getSelectedItem().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(ViewAttendanceProfessor.this,"Please select class id to proceed !",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(attendanceArrayAdapter!=null) {
                        attendanceArrayAdapter.clear();
                    }
                    if(proxyAttendanceListItems!=null) {
                        proxyAttendanceListItems = null;
                    }
                    getAttendanceListProfessor(classList.getSelectedItem().toString());
                }
            }
        });

        classDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                };
                DatePickerDialog dp = new DatePickerDialog(ViewAttendanceProfessor.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });

        classListAdapter = (ArrayList<String>) getIntent().getStringArrayListExtra("classArrayList");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewAttendanceProfessor.this,android.R.layout.simple_list_item_1,classListAdapter);
        classList.setAdapter(arrayAdapter);

    }
    private void updateLabel()
    {
        //Updating the selected date of calendar dialog in the edittext field
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        classDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void getAttendanceListProfessor(String classid)
    {
        String attendedDate=classDate.getText().toString();
        if (attendedDate.equalsIgnoreCase("select date")) {
            Toast.makeText(ViewAttendanceProfessor.this,"Please select a date!",Toast.LENGTH_LONG).show();
            return;
        }
        String url = "http://www.inclassattendance.16mb.com/UserRegistration/attendance_retrieve.php";
        String params = "?studentid="+userID+"&classid="+classid+"&attendeddate="+attendedDate+"&method=attendancelistprofessor";

        class  GetAttendance extends AsyncTask<String,Void,String>
        {

            @Override
            protected String doInBackground(String... params) {
                String urlString = params[0];
                BufferedReader bufferedReader = null;
                try
                {
                    URL url= new URL(urlString);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();

                    String result;
                    result = bufferedReader.readLine();

                    String[] resultItems=result.split(",");

                    String[] attendanceListItems = resultItems[0].split(";");
                    proxyAttendanceListItems = resultItems[1].split(";");

                    attendanceListAdapter.clear();
                    for(int i=0;i<attendanceListItems.length;i++)
                    {
                        attendanceListAdapter.add(i,attendanceListItems[i]);
                    }
                    return "";
                }
                catch (Exception e)
                {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                attendanceArrayAdapter = new ArrayAdapter<String>(ViewAttendanceProfessor.this,android.R.layout.simple_list_item_1,attendanceListAdapter);
                attendanceListView.setAdapter(attendanceArrayAdapter);
                attendanceArrayAdapter.notifyDataSetChanged();
                //updateProxyList();
            }
        }
        GetAttendance getAttendance = new GetAttendance();
        getAttendance.execute(url + params);
        //   updateList(attendanceJsonResult);
    }
    public void updateProxyList()
    {
        for(int i=0;i<proxyAttendanceListItems.length;i++)
        {
            try {
                int listposition = attendanceArrayAdapter.getPosition(proxyAttendanceListItems[i].toString());
                attendanceListView.getChildAt(listposition).setBackgroundColor(Color.RED);
            }catch (Exception e)
            {e.printStackTrace();}
        }
    }
}

