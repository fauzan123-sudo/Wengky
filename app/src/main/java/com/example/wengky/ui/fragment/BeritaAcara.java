package com.example.wengky.ui.fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.wengky.R;
import com.example.wengky.adapter.AdapterBeritaAcara;
import com.example.wengky.helper.AppController;
import com.example.wengky.model.Model_Berita_Acara;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static com.example.wengky.helper.Constans.TAG_JSON_OBJECT;
import static com.example.wengky.helper.Constans.berita;

public class BeritaAcara extends Fragment {
    RelativeLayout relativeLayout;
    private ProgressDialog pd;
    private RecyclerView mRecyclerView;
    private ArrayList<Model_Berita_Acara> modelBeritaAcara;
    private AdapterBeritaAcara mExampleAdapter;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_berita_acara, container, false);
        pd = new ProgressDialog(requireActivity());
        pd.setMessage("loading");
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");

        relativeLayout = view.findViewById(R.id.kembaliges);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home fragment2 = new Home();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment2)
                        .commit();
            }
        });

        Pusher pusher = new Pusher("caf6f438aeb76ce29ca9", options);

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
//                modelBeritaAcara.clear();
            parseJSON();
            //NOTIFICATION
            String data = event.getData();

            String gambar;
            String judul;
            String keterangan;
//
            try {
                JSONObject jsonObject = new JSONObject(data);
                judul= jsonObject.getString("title");
                gambar= jsonObject.getString("image");
                keterangan= jsonObject.getString("isi");

                Log.i("info", "onEvent: " + gambar+judul+keterangan);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//                mBuilder = new NotificationCompat.Builder(requireActivity());
//                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
//                mBuilder.setContentTitle(judul)
//                        .setContentText(keterangan)
////                        .setSound(Default)
//                        .setAutoCancel(false)
//                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
//
//                mNotificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
//                {
//                    int importance = NotificationManager.IMPORTANCE_HIGH;
//                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
//                    notificationChannel.enableLights(true);
//                    notificationChannel.setLightColor(Color.RED);
//                    notificationChannel.enableVibration(true);
//                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                    assert mNotificationManager != null;
//                    mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
//                    mNotificationManager.createNotificationChannel(notificationChannel);
//                }
//                assert mNotificationManager != null;
//                mNotificationManager.notify(0 /* Request Code */, mBuilder.build());

        });

        ProgressDialog pd = new ProgressDialog(requireActivity());
        pd.setMessage("loading");
        mRecyclerView = view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        modelBeritaAcara    = new ArrayList<>();
        parseJSON();
        return view;
    }

    private void parseJSON() {
        modelBeritaAcara.clear();
        JsonArrayRequest jArr = new JsonArrayRequest(berita,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject hit     = response.getJSONObject(i);
                            String Gambar      = hit.getString("image");
                            String Judul       = hit.getString("title");
                            String Keterangan  = hit.getString("isi");
                            modelBeritaAcara.add(new Model_Berita_Acara(Gambar, Judul, Keterangan));

                        } catch (JSONException e) {
//                            e.printStackTrace();
                        }
                    }
                    mExampleAdapter = new AdapterBeritaAcara(getActivity(), modelBeritaAcara);
                    mRecyclerView.setAdapter(mExampleAdapter);
                    mExampleAdapter.notifyDataSetChanged();

                }, Throwable::printStackTrace);
        AppController.getInstance().addToRequestQueue(jArr, TAG_JSON_OBJECT);
    }

    @Override
    public void onResume() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        super.onResume();
    }

    @Override
    public void onStop() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        super.onStop();
    }

//    AdapterBeritaAcara.FragmentCommunication communication= (position, name, job) -> {
//        DetailBerita fragmentB=new DetailBerita();
//        Bundle bundle=new Bundle();
//        bundle.putString("NAME",name);
//        bundle.putString("JOB",job);
//        fragmentB.setArguments(bundle);
//        FragmentManager manager=getFragmentManager();
//        FragmentTransaction transaction=manager.beginTransaction();
//        transaction.replace(R.id.fragment_container,fragmentB).commit();
//    };
}