package com.example.wengky;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Person;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wengky.helper.InternetCheckService;
import com.example.wengky.helper.LoadingCustom;
import com.example.wengky.helper.SessionManager;
import com.example.wengky.ui.fragment.Absensi;
import com.example.wengky.ui.fragment.BeritaAcara;
import com.example.wengky.ui.fragment.Gaji;
import com.example.wengky.ui.fragment.Home;
import com.example.wengky.ui.fragment.Profile;
import com.example.wengky.ui.fragment.Scanning;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import ru.nikartm.support.ImageBadgeView;

import static com.example.wengky.Login.TAG_ID;
import static com.example.wengky.helper.Constans.urlImagePegawai;

public class Dashboard extends AppCompatActivity {
    final LoadingCustom loadingDialog =new LoadingCustom(Dashboard.this);
    public String TAG = "Dashboard";
    ConnectivityManager conMgr;
    ImageBadgeView badgeView;
    int jumlahBadge;
    int jumlah;
    private static FloatingActionButton Fab;
    private static BottomNavigationView bottomNav;
    private static BottomAppBar bottomAppBar;
    private static final String CHANNEL_ID ="101" ;
    CircleImageView profile_image;
    BroadcastReceiver broadcastReceiver = null;
    SessionManager sessionManager;
    String id, getImage, getName;
    SharedPreferences sharedpreferences;
    TextView Nama;
    String scores = "scores";
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_dashboard);
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");
        Paper.init(this);
        badgeView = findViewById(R.id.badgeBeritaAcara);
        Pusher pusher = new Pusher("caf6f438aeb76ce29ca9", options);
//        badgeView.setBadgeValue(0);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
                Log.i("info", "onConnectionStateChange: " + change.getPreviousState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe("my-channel");

        channel.bind("my-event", event -> {
            jumlahBadge++;
//            jumlah = jumlahBadge;
//            Log.d(TAG, "datass" + jumlah);
            Paper.book().write(scores, jumlahBadge);
            Paper.book().read(scores);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    badgeView.setBadgeValue(jumlahBadge);
                }
            });

//                modelBeritaAcara.clear();
                    //NOTIFICATION
                    String data = event.getData();

                    String gambar;
                    String judul;
                    String keterangan;
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(data);
//                        judul = jsonObject.getString("title");
//                        gambar = jsonObject.getString("image");
//                        keterangan = jsonObject.getString("isi");
//
//                        Log.i("info", "onEvent: " + gambar + judul + keterangan);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                });

        sharedpreferences       = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        sessionManager          = new SessionManager(Dashboard.this);
        sessionManager.checkLogin();


//        badgeView.setBadgeValue(0);
//        Log.d("badge", "onCreate: "+0);
        badgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().delete("scores");
                badgeView.clearBadge();
                BeritaAcara fragment2 = new BeritaAcara();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment2)
                        .commit();

            }
        });
        Fab = findViewById(R.id.fab);
        Fab.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Scanning()).commit();

            bottomNav.setSelectedItemId(R.id.scan);
        });
//        broadcastReceiver = new InternetCheckService();
        checkInternet();
        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomNav    = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setBackground(null);
        bottomNav.getMenu().getItem(2).setEnabled(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profile_image           = toolbar.findViewById(R.id.profile_image);
        Nama                    = toolbar.findViewById(R.id.namas);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getImage                = user.get(SessionManager.IMAGE);
        getName                 = user.get(SessionManager.NAME);

        Nama.setText(getName);

        Picasso.with(Dashboard.this)
                .load(urlImagePegawai + getImage)
                .into(profile_image);
        profile_image.setOnClickListener(view1 -> Toast.makeText(this, "Hi..", Toast.LENGTH_SHORT).show());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Home()).commit();
        }

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                Log.d(TAG, "onCreate: ");
            } else {
                Intent intent = new Intent(Dashboard.this, NoConnection.class);
                startActivity(intent);
            }
        }
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();


        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        id = getIntent().getStringExtra(TAG_ID);
    }

    private void FunBeritaAcara() {
        BeritaAcara fragment2 = new BeritaAcara();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment2)
                .commit();
    }


    public  void LoginOut() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Dashboard.this);

        // set title dialog
        alertDialogBuilder.setTitle("Keluar dari aplikasi?");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Klik Ya untuk keluar!")
                .setIcon(R.mipmap.ic_wengky)
                .setCancelable(false)
                .setPositiveButton("Ya", (dialog, id) -> {
                    // TODO jika tombol diklik, maka akan menutup activity ini
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(Login.session_status, false);
                    editor.putString(TAG_ID, null);
                    editor.apply();

                    Intent intent = new Intent(Dashboard.this, Login.class);
//                    finish();
                    startActivity(intent);
                })
                .setNegativeButton("Tidak", (dialog, id) -> {
                    // TODO jika tombol ini diklik, akan menutup dialog
                    // TODO dan tidak terjadi apa2
                    dialog.cancel();
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new Home();
                        break;
                    case R.id.absen:
                        selectedFragment = new Absensi();
                        break;
                    case R.id.scan:
                        selectedFragment = new Scanning();
                        break;
                    case R.id.gaji:
                        selectedFragment = new Gaji();
                        break;
                    case R.id.profile:
                        selectedFragment = new Profile();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            };

    private void checkInternet() {
        registerReceiver(broadcastReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public static void hideBottomNav(){
        bottomAppBar.setVisibility(View.GONE);
        Fab.setVisibility(View.GONE);
        bottomNav.setVisibility(View.GONE);
    }

    public static void showBottomNav(){
        bottomAppBar.setVisibility(View.VISIBLE);
        Fab.setVisibility(View.VISIBLE);
        bottomNav.setVisibility(View.VISIBLE);
    }

    private void Keluar() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Keluar dari aplikasi?");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Klik Ya untuk keluar!")
                .setIcon(R.mipmap.ic_wengky)
                .setCancelable(false)
                .setPositiveButton("Ya", (dialog, id) -> {
                    // TODO jika tombol diklik, maka akan menutup activity ini
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(Login.session_status, false);
                    editor.putString(TAG_ID, null);
                    editor.apply();

                    Intent intent = new Intent(Dashboard.this, Login.class);
                    finish();
                    startActivity(intent);
                })
                .setNegativeButton("Tidak", (dialog, id) -> {
                    // TODO jika tombol ini diklik, akan menutup dialog
                    // TODO dan tidak terjadi apa2
                    dialog.cancel();
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();

    }

    private void replace (Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.home == seletedItemId){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set title dialog
            alertDialogBuilder.setTitle("Konfirmasi");

            // set pesan dari dialog
            alertDialogBuilder
                    .setMessage("Klik Ya untuk keluar!")
                    .setCancelable(false)
                    .setPositiveButton("Ya", (dialog, id) -> {
                        // jika tombol diklik, maka akan menutup activity ini
                        Dashboard.this.finish();
                        super.onBackPressed();
                    })
                    .setNegativeButton("Tidak", (dialog, id) -> {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    });

            // membuat alert dialog dari builder
            AlertDialog alertDialog = alertDialogBuilder.create();

            // menampilkan alert dialog
            alertDialog.show();
        }else {
            setHomeItem(Dashboard.this);
        }
    }

    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setDescription(CHANNEL_DESC);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "firebaseNotifChannel";
            String description = "Receve Firebase notification";
            int importance = NotificationManager.IMPORTANCE_MAX;
            @SuppressLint("WrongConstant") NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
