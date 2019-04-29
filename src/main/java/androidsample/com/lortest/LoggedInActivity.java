package androidsample.com.lortest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class LoggedInActivity extends AppCompatActivity {
    private GoogleMap myMap;
    private static final String TAG = "LoggedInActivity";

    private static int ERROR_DIALOG_REQUEST = 9001;
    EditText editText;
    Button newString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        String CurrentlyLoggedInUserName = intent.getStringExtra("username");


        AsyncTaskClass asyncTaskClass = new AsyncTaskClass();

        asyncTaskClass.execute();

        newString = findViewById(R.id.button3);

        newString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskClass asyncTaskClass = new AsyncTaskClass();

                asyncTaskClass.execute();
            }
        });

    init();


    }



    private void init(){
        Button btnMap = findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection() && isServicesOK()) {
                    Intent intent = new Intent(LoggedInActivity.this, MapActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoggedInActivity.this, "Connect to the internet to use the map", Toast.LENGTH_LONG).show();
                }
            }
        });





    }

    private void consumeTask(){

        String url = "https://www.random.org/integers/?num=10&min=1&max=6&col=1&base=10&format=plain&rnd=new";

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        final String result = restTemplate.getForObject(url, String.class);

        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                editText = findViewById(R.id.editText3);

                editText.setText(result);
            }
        });



    }

    public boolean isServicesOK(){
        Log.d(TAG,"isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(LoggedInActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //Everything is fine and the user can make the request
            Log.d(TAG,"isServicesOK: Google play services is working");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //error occured but can be resolved
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(LoggedInActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "Cant connect to the map", Toast.LENGTH_SHORT).show();
        }
        return true;

    }

    public class AsyncTaskClass extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            consumeTask();

            return "ok";
        }


    }



    public boolean checkConnection(){
        ConnectivityManager check = (ConnectivityManager)this.getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo[] info = check.getAllNetworkInfo();

        for (int i = 0; i <info.length ; i++) {
            if(info[i].getState() == NetworkInfo.State.CONNECTED){
                return true;
            }

        }

        return false;

    }



}
