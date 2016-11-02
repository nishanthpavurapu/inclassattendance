package nishanth.com.inclassattendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * Created by nisha on 3/23/2016.
 */
public class StartScreen extends AppCompatActivity implements View.OnClickListener{

    private Button buttonLogin,buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonLogin.setTransformationMethod(null);
        buttonRegister.setTransformationMethod(null);

        buttonRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Boolean Loggedin = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        String userType = sharedPreferences.getString("usertype", "");

        if(Loggedin)
        {
            if(userType.equalsIgnoreCase("student"))
            {
                Intent intent = new Intent(StartScreen.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
            else if (userType.equalsIgnoreCase("professor"))
            {
                Intent intent = new Intent(StartScreen.this, ProfessorHomeScreen.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {

        Button buttonClicked = (Button) v;

        if (buttonClicked.getText().toString().equalsIgnoreCase("login"))
        {
            Intent intent = new Intent(this,LoginScreen.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

    }

}
