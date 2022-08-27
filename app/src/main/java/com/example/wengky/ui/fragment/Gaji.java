package com.example.wengky.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.wengky.R;
import com.example.wengky.helper.AppController;
import com.example.wengky.helper.SessionManager;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.wengky.helper.Constans.TAG_JSON_OBJECT;
import static com.example.wengky.helper.Constans.gaji;

public class Gaji extends Fragment {
    ProgressDialog pd;
    int success;
    private static final String TAG = Gaji.class.getSimpleName();
    TextView Gaji_Bersih, Asuransi, Pph21, gaji_pokok, totalPotongan;
    SessionManager sessionManager;
    String getId, getNama, getImage, getJabatan;
    //    CircleImageView profile_image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gaji, container, false);
        pd = new ProgressDialog(requireActivity());
        pd.setMessage("loading");
        sessionManager = new SessionManager(requireActivity());
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        parseJSON();

        gaji_pokok        = rootView.findViewById(R.id.gajiPokok);
        Asuransi          = rootView.findViewById(R.id.asuransi);
        Pph21             = rootView.findViewById(R.id.pph21);
        Gaji_Bersih       = rootView.findViewById(R.id.gaji_bersih);
        totalPotongan     = rootView.findViewById(R.id.totalPotongan);

        getNama     = user.get(SessionManager.NAME);
        getImage    = user.get(SessionManager.IMAGE);
        getJabatan  = user.get(SessionManager.JABATAN);
        getId = user.get(SessionManager.ID);

        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");
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
//            requireActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    parseJSON();
//                }
//            });


            //NOTIFICATION
            String data = event.getData();

            String gambar;
            String judul;
            String keterangan;


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

        return  rootView;
    }


    private void parseJSON() {
            pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, gaji,
                response -> {
            pd.hide();
                    Log.i(TAG, response);
                    try {
                        JSONObject jObj = new JSONObject(response);
                        int status = jObj.getInt("status");
                        if (status == 1){
                            String gaji_pokoks       = jObj.getString("gaji_pokok");
                            String asuransi         = jObj.getString("Asuransi");
                            String pph21            = jObj.getString("PPH21");
                            String total_potongans   = jObj.getString("total_potongan");
                            String gaji_bersihs      = jObj.getString("gaji_bersih");


                            gaji_pokok.setText("Rp. " + formatRupiah(Double.parseDouble(gaji_pokoks)));
//                            gaji_pokok.setText("Rp. " + gaji_pokoks);
//                            Asuransi.setText("Rp. "+ asuransi );
                            Asuransi.setText("Rp. " + formatRupiah(Double.parseDouble(asuransi)));
//                            Pph21.setText("Rp." + pph21);
                            Pph21.setText("Rp. " + formatRupiah(Double.parseDouble(pph21)));
//                            totalPotongan.setText("Rp" + total_potongans );
                            totalPotongan.setText("Rp. " + formatRupiah(Double.parseDouble(total_potongans)));
//                            Gaji_Bersih.setText("Rp." + gaji_bersihs);
                            Gaji_Bersih.setText("Rp. "+ formatRupiah(Double.parseDouble(gaji_bersihs)));

//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject object = jsonArray.getJSONObject(i);
//                                String key = object.getString("key").trim();
//                                String name = object.getString("name").trim();
//                                String value = object.getString("value").trim();
//

//                            }
                        }

                    } catch (JSONException e) {
                        pd.hide();
                        e.printStackTrace();
                    }

                }, Throwable::printStackTrace){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<>();
                params.put("id_pegawai", getId);
                return params;
            }};
        AppController.getInstance().addToRequestQueue(stringRequest, TAG_JSON_OBJECT);
    }

    class MyStringRequest extends StringRequest{

        private Map params = new HashMap();
    public MyStringRequest (Map params, int mehotd, String url, Response.Listener listenr, Response.ErrorListener errorListenr){
            super(mehotd,url,listenr,errorListenr);

            this.params = params;

        }
        @Override
        protected Map<String, String> getParams(){

            return params;

        }

    }

//    RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
//
//    String url = "https://theUrlToRequest";
//    Map<String, String>  params = new HashMap<>();
//    params.put("id_pegawai", "6")
//
//    MyStringRequest postRequest = new MyStringRequest (params ,Request.Method.POST, gaji,
//            new Response.Listener<String>(){
//                @Override
//                public void onResponse(String response) {
//
//                }
//            },
//            new Response.ErrorListener(){
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            }
//    );
//        queue.add(postRequest);

    private String formatRupiah(Double number){
        Locale locale = new Locale("id","ID");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        return numberFormat.format(number);
    }
}