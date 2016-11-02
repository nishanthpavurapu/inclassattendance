package nishanth.com.inclassattendance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by nisha on 4/30/2016.
 */
public class ViewProfessorClasses extends AppCompatActivity{
    ListView professorClassListView;
    ArrayList<String> classListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewprofessorclasses);
        professorClassListView = (ListView)findViewById(R.id.professorClassesListView);

        classListAdapter = (ArrayList<String>) getIntent().getStringArrayListExtra("classArrayList");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewProfessorClasses.this,android.R.layout.simple_list_item_1,classListAdapter);
        professorClassListView.setAdapter(arrayAdapter);
    }
}
