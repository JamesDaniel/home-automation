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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getHeatingStatusBtn = findViewById(R.id.getHeatingStatusBtn);
        setHeatingOnBtn = findViewById(R.id.setHeatingOnBtn);
        setHeatingOffBtn = findViewById(R.id.setHeatingOffBtn);
        textView = findViewById(R.id.outputTv);

        getHeatingStatusBtn.setOnClickListener(this);
        setHeatingOnBtn.setOnClickListener(this);
        setHeatingOffBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getHeatingStatusBtn:
                callApi();
                break;
            case R.id.setHeatingOnBtn:
                turnHeatingOf();
                break;
            case R.id.setHeatingOffBtn:
                turnHeatingOff();
                break;
            default:
                Log.e(TAG, "No case for view with ID: " + v.getId());
        }
    }

    private void callApi() {
        HttpUtils.get(AppConstants.HEATER_STATUS, new RequestParams(), new JsonHttpResponseHandler() {
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
        HttpUtils.post(AppConstants.HEATER_OFF, new RequestParams(), new JsonHttpResponseHandler());
    }

    private void turnHeatingOf() {
        HttpUtils.post(AppConstants.HEATER_ON, new RequestParams(), new JsonHttpResponseHandler());
    }
}
