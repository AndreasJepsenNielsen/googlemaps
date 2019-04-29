package androidsample.com.lortest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    final String TAG = "MainActivity";
    int check = 0;
    int spinnerValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);

        SharedPreferences sharedPref = getSharedPreferences("selection",MODE_PRIVATE);
        spinnerValue = sharedPref.getInt("userSelection",0);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        spinner.setSelection(spinnerValue);



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    if(++check > 1){



        String selectedLang = spinner.getSelectedItem().toString();
        String languageSub = selectedLang.substring(0, 2);

            if(languageSub.toLowerCase().equals("sw")){
            languageSub = "sv";
            }

            String[] languages = getResources().getStringArray(R.array.language);

            int selection = 0;
        for (int i = 0; i < languages.length ; i++) {
            if(languages[i].equals(selectedLang)){
                selection = i;
                Log.d(TAG,Integer.toString(selection));
                SharedPreferences sharedPref = getSharedPreferences("position",0);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putInt("userSelection", selection);
                prefEditor.commit();
            }
        }




                Locale locale = new Locale(languageSub);
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = locale;
                res.updateConfiguration(conf,dm);
                Intent refresh = new Intent(this,MainActivity.class);

                startActivity(refresh);
                finish();
    }else{
        check++;
        spinner.setSelection(spinnerValue);

    }






    }

    public void login(View v){

        EditText usernameEdit = findViewById(R.id.editText);
        EditText passwordEdit = findViewById(R.id.editText2);

        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        SharedPreferences sharedPref = getSharedPreferences("users",MODE_PRIVATE);

        if(sharedPref.getString(username,"-1").equals("-1")){
            Toast.makeText(this,"User does not exist",Toast.LENGTH_LONG).show();
        }else if(!sharedPref.getString(username,"-1").equals(password)){
            Toast.makeText(this,"Incorrect Credentials",Toast.LENGTH_LONG).show();
        }else if(sharedPref.getString(username,"-1").equals(password)){
            Intent loggedIn = new Intent(this,LoggedInActivity.class).putExtra("username",username);
            startActivity(loggedIn);
        }



    }

    public void register(View v){

        EditText usernameEdit = findViewById(R.id.editText);
        EditText passwordEdit = findViewById(R.id.editText2);

        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences("users",MODE_PRIVATE);
        if(username.length() < 4 || password.length() < 4){
            Toast.makeText(this, "Username and password must be atleast 4 characters long", Toast.LENGTH_LONG).show();

        }else {


            if (sharedPref.getString(username, "-1").equals("-1")) {
                sharedPref = getSharedPreferences("users", 0);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putString(username, password);
                prefEditor.commit();

                Intent loggedIn = new Intent(this, LoggedInActivity.class).putExtra("username",username);
                startActivity(loggedIn);
            } else {
                Toast.makeText(this, "User already exists!", Toast.LENGTH_LONG).show();

            }
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
