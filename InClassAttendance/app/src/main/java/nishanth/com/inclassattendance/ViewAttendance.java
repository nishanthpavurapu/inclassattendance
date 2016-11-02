package nishanth.com.inclassattendance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by nisha on 4/25/2016.
 */
public class ViewAttendance extends AppCompatActivity{

    private ListView attendanceListview;
    private Spinner classList;
    SharedPreferences sharedPreferences;
    String userID;
    ArrayList<String> classListAdapter= new ArrayList<>();
    ArrayList<String> attendanceListAdapter = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewattendance_screen);

        classList = (Spinner)findViewById(R.id.classDropDown);
        attendanceListview = (ListView)findViewById(R.id.attendanceListView);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listview_header,attendanceListview,false);
        attendanceListview.addHeaderView(header,null,false);

        sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userID=sharedPreferences.getString("userid", null);

        classListAdapter = (ArrayList<String>) getIntent().getStringArrayListExtra("classArrayList");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewAttendance.this,android.R.layout.simple_list_item_1,classListAdapter);
        classList.setAdapter(arrayAdapter);

      //  getClassList(userID);

        classList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!(classList.getSelectedItem().toString().equalsIgnoreCase("none"))) {
                    getAttendanceList(classList.getSelectedItem().toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void getAttendanceList(String classid)
    {
        String url = "http://www.inclassattendance.16mb.com/UserRegistration/attendance_retrieve.php";
        String params = "?studentid="+userID+"&classid="+classid+"&method=attendancelist";

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

                    String[] attendanceListItems = result.split(";");
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
                ArrayAdapter<String> attendanceArrayAdapter = new ArrayAdapter<String>(ViewAttendance.this,android.R.layout.simple_list_item_1,attendanceListAdapter);
                attendanceListview.setAdapter(attendanceArrayAdapter);
                attendanceArrayAdapter.notifyDataSetChanged();
            }
        }
        GetAttendance getAttendance = new GetAttendance();
        getAttendance.execute(url + params);
     //   updateList(attendanceJsonResult);
    }
}
