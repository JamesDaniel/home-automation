package jamesmcgarr.me.homeautomationandroid.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jamesmcgarr.me.homeautomationandroid.R;
import jamesmcgarr.me.homeautomationandroid.utils.AppConstants;
import jamesmcgarr.me.homeautomationandroid.utils.HttpUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button getHeatingStatusBtn;
    private Button setHeatingOnBtn;
    private Button setHeatingOffBtn;
    private TextView textView;
    private Button isHeaterActiveBtn;
    private Button getTemperatureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getHeatingStatusBtn = findViewById(R.id.getHeatingStatusBtn);
        setHeatingOnBtn = findViewById(R.id.setHeatingOnBtn);
        setHeatingOffBtn = findViewById(R.id.setHeatingOffBtn);
        textView = findViewById(R.id.outputTv);
        isHeaterActiveBtn = findViewById(R.id.isHeaterActiveBtn);
        getTemperatureBtn = findViewById(R.id.getTemperatureBtn);

        getHeatingStatusBtn.setOnClickListener(this);
        setHeatingOnBtn.setOnClickListener(this);
        setHeatingOffBtn.setOnClickListener(this);
        isHeaterActiveBtn.setOnClickListener(this);
        getTemperatureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getHeatingStatusBtn:
                isHeatingOnOff();
                break;
            case R.id.setHeatingOnBtn:
                turnHeatingOn();
                break;
            case R.id.setHeatingOffBtn:
                turnHeatingOff();
                break;
            case R.id.isHeaterActiveBtn:
                getHeaterActive();
                break;
            case R.id.getTemperatureBtn:
                getTemperature();
                break;
            default:
                Log.e(TAG, "No case for view with ID: " + v.getId());
        }
    }

    private void isHeatingOnOff() {
        HttpUtils.get(AppConstants.MAINS_APPLIANCES, AppConstants.HEATER_STATUS, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "Response: " + response);
                try {
                    if (response.toString().indexOf("on") > -1) {
                        textView.setText("Heating is On");
                    } else if (response.toString().indexOf("off") > -1) {
                        textView.setText("Heating is Off");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void turnHeatingOff() {
        HttpUtils.post(AppConstants.MAINS_APPLIANCES, AppConstants.HEATER_OFF, new RequestParams(), new JsonHttpResponseHandler());
    }

    private void turnHeatingOn() {
        HttpUtils.post(AppConstants.MAINS_APPLIANCES, AppConstants.HEATER_ON, new RequestParams(), new JsonHttpResponseHandler());
    }

    private void getHeaterActive() {
        HttpUtils.get(AppConstants.SYSTEM_HEARTBEATS, AppConstants.HEATER_ACTIVE, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "Response: " + response);
                try {
                    if (response.toString().indexOf("true") > -1) {
                        textView.setText("Heating Unit 1 is alive.");
                    } else if (response.toString().indexOf("false") > -1) {
                        textView.setText("Heating Unit 1 is dead.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTemperature() {
        HttpUtils.get(AppConstants.ENVIRONMENT_CONTROL, AppConstants.TEMPERATURE, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "Response: " + response);
                try {
                    JSONObject obj = response;
                    String temperature = (String) obj.get("temperatureC");
                    textView.setText("The temperature is: " + temperature + " degrees C.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
