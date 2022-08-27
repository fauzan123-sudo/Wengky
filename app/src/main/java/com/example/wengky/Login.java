package com.example.wengky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.wengky.helper.AppController;
import com.example.wengky.helper.LoadingCustom;
import com.example.wengky.helper.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.wengky.helper.Constans.TAG_JSON_OBJECT;
import static com.example.wengky.helper.Constans.URL_LOGIN;

public class Login extends AppCompatActivity {
    LinearLayout hidenText;
    private static final String TAG_SUCCESS = "success";
    private long backPressedTime;
    private Toast backToast;
    Boolean session = false;
    String id, email;
    int success;
    final String TAG = Login.class.getSimpleName();
    public static final String my_shared_preferences = "my_shared_preferences1";
    public static final String session_status = "session_status1";
    ConnectivityManager conMgr;
    SessionManager sessionManager;
    public final static String TAG_ID       = "id";
    public final static String TAG_IMAGE    = "image";
    public final static String TAG_USERNAME = "username";
    public final static String TAG_JABATAN = "username";
    SharedPreferences sharedpreferences;
    TextInputEditText Usename,Password;
    Button btnLogin ;
    final LoadingCustom loadingDialog =new LoadingCustom(Login.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_login);

        hidenText = findViewById(R.id.hiddenText);
        hidenText.setVisibility(View.INVISIBLE);
        sessionManager = new SessionManager(this);
        Usename = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        btnLogin    = findViewById(R.id.login);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                Log.d(TAG, "onCreate: ");
            } else {
                Intent intent = new Intent(Login.this, NoConnection.class);
                startActivity(intent);
            }
        }

// Cek session login jika TRUE maka langsung buka Beranda
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session           = sharedpreferences.getBoolean(session_status, false);
        id                = sharedpreferences.getString(TAG_ID, null);
        email             = sharedpreferences.getString(TAG_USERNAME, null);

        if (session) {
            Intent intent = new Intent(Login.this, Dashboard.class);
            intent.putExtra(TAG_ID, id);
            finish();
            startActivity(intent);
        }

        btnLogin.setOnClickListener(view -> {
            String UserName = Usename.getText().toString().trim();
            String PassWOrd = Password.getText().toString().trim();
            if (UserName.isEmpty()){
                Usename.setError("harap isi username");
            }
            else if (PassWOrd.isEmpty()){
                Password.setError("harap isi password");
            }
            else if (!UserName.isEmpty() || !PassWOrd.isEmpty()) {
                btnLogin.setEnabled(true);
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    LoginUser(UserName, PassWOrd);
                } else {
//                    Toast.makeText(Login.this, "tidak ada koneksi", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Harap isi Username dan password dulu!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoginUser(final String etEmail, final String etPassword) {
        loadingDialog.startLoadingDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN, response -> {
            try {
                JSONObject jObj = new JSONObject(response);
                success         = jObj.getInt(TAG_SUCCESS);
                if (success == 1) {
                    String name     = jObj.getString("nama").trim();
                    String email1   = jObj.getString("username").trim();
                    String id       = jObj.getString("id").trim();
                    String image    = jObj.getString("image").trim();
                    String jabatan  = jObj.getString("jabatan").trim();
                    sessionManager.createSession(name, email1, id, image, jabatan);

                    // menyimpan login ke session
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(session_status, true);
                    editor.putString(TAG_ID, id);
                    editor.putString(TAG_IMAGE, image);
                    editor.putString(TAG_JABATAN, jabatan);
                    editor.apply();

                    Intent intent = new Intent(Login.this, Dashboard.class);
                    startActivity(intent);
                    finish();

                } else {
                    hidenText.setVisibility(View.VISIBLE);
                    loadingDialog.dissmisDialog();
//                    Toast.makeText(Login.this, "periksa kembali username atau password anda", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dissmisDialog();

            } catch (JSONException e) {
                e.printStackTrace();
                loadingDialog.dissmisDialog();
//                Toast.makeText(Login.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        },error -> {
            loadingDialog.dissmisDialog();
//            Toast.makeText(Login.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", etEmail);
                params.put("password", etPassword);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, TAG_JSON_OBJECT);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}