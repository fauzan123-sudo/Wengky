package com.example.wengky.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wengky.Dashboard;
import com.example.wengky.Login;
import com.example.wengky.R;
import com.example.wengky.adapter.AdapterAbsen;
import com.example.wengky.adapter.AdapterSpinner;
import com.example.wengky.helper.AppController;
import com.example.wengky.helper.LoadingCustom;
import com.example.wengky.helper.LoadingCustomFragment;
import com.example.wengky.helper.SessionManager;
import com.example.wengky.model.ModelAbsen;
import com.example.wengky.model.SpinnerModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.wengky.helper.Constans.TAG_JSON_OBJECT;
import static com.example.wengky.helper.Constans.TAG_SUCCESS;
import static com.example.wengky.helper.Constans.Total_Absensi;
import static com.example.wengky.helper.Constans.URL_Spinner;
import static com.example.wengky.helper.Constans.izin;
import static com.example.wengky.helper.Constans.read_absensi;

public class Absensi extends Fragment implements AdapterView.OnItemSelectedListener{
    RadioGroup radioGroup;
    int success,angka,JenisIjinId, position;
    TextView Tr, InHide;
    RadioButton rb1,rb2;
    TextView AksiBottomSheet, Dari, Sampai, Nama, Jabatan, Hadir, Izin, Alpa;
    public String getId, getNama, getImage, getJabatan, Status, JenisIjin, ID;
    private BottomSheetBehavior bottomSheetBehavior;
    SessionManager sessionManager;
    EditText Alasan;
    Button KirimDataIzin;
    ImageView arrow_right;
    Spinner spinnerCountries;
    private TimePickerDialog timePickerDialog;
    private int mYear, mMonth, mDay;
    CircleImageView profile_image;
    AdapterSpinner adapter;
    List<SpinnerModel> spinnerList = new ArrayList<>();
    HashMap<String, String> user;
    String tipe;
//    private ProgressDialog pd;
    //    Absen
    RecyclerView mRecyclerView;
    private AdapterAbsen mExampleAdapter;
    ArrayList<ModelAbsen> modelBeritaAcara = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        parseJson(getId);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_absensi, container, false);

        //Find ID

        tipe = String.valueOf(angka);
//        pd = new ProgressDialog(requireActivity());
//        pd.setMessage("loading");
        Hadir               = view.findViewById(R.id.hadir);
        InHide              = view.findViewById(R.id.inHide);
        Izin                = view.findViewById(R.id.izin);
        Alpa                = view.findViewById(R.id.alpa);
        Nama                = view.findViewById(R.id.nama);
        Jabatan             = view.findViewById(R.id.jabatan);
        spinnerCountries    = view.findViewById(R.id.jenis_izin);
        AksiBottomSheet     = view.findViewById(R.id.ajukanIzin);
        arrow_right         = view.findViewById(R.id.arrow_right);
        View bottomSheet    = view.findViewById(R.id.bottom_sheet);
        radioGroup          = view.findViewById(R.id.Radio_Group);
        rb1                 = view.findViewById(R.id.rbJam);
        rb2                 = view.findViewById(R.id.rbHari);
        Dari                = view.findViewById(R.id.dari);
        Sampai              = view.findViewById(R.id.sampai);
        KirimDataIzin       = view.findViewById(R.id.kirimDataIzin);
        Alasan              = view.findViewById(R.id.alasan);
        profile_image       = view.findViewById(R.id.profile_image);
        Tr                  = view.findViewById(R.id.tr);
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
//                modelBeritaAcara.clear();
//            requireActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    parseJson(getId);
//                }
//            });
//            parseJson(getId);
//            Absensi fragment2 = new Absensi();
//            requireActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, fragment2)
//                    .commit();

            //NOTIFICATION
            String data = event.getData();

            String gambar;
            String judul;
            String keterangan;
//
            try {
                JSONObject jsonObject = new JSONObject(data);
                judul= jsonObject.getString("tanggal");
                gambar= jsonObject.getString("jam");
                keterangan= jsonObject.getString("status");
//                modelBeritaAcara.add(new ModelAbsen(judul, keterangan, Status));
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
        Status              = "masuk";
        callData();

        adapter = new AdapterSpinner(requireActivity(), spinnerList);

        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                JenisIjin = spinnerList.get(i).getNama();
                JenisIjinId = spinnerList.get(i).getIdJenisIzin();
                Log.d("disinio", "onItemSelected: "+ JenisIjinId);
//                Toast.makeText(requireActivity(), spinnerList.get(i).getIdJenisIzin() , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerCountries.setAdapter(adapter);

        // Declare
//        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        sessionManager          = new SessionManager(requireActivity());
        sessionManager.checkLogin();
        user = sessionManager.getUserDetail();
        getId                   = user.get(SessionManager.ID);
        tampilDataJumlah(getId);

        mRecyclerView = view.findViewById(R.id.rec);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mExampleAdapter = new AdapterAbsen(getActivity(), modelBeritaAcara);
        mRecyclerView.setAdapter(mExampleAdapter);

        parseJson(getId);
//        requireActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                parseJson(getId);
//            }
//        });

        //Bottom Sheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        //Sharepreference

        getNama                      = user.get(SessionManager.NAME);
        getImage                     = user.get(SessionManager.IMAGE);
        getJabatan                   = user.get(SessionManager.JABATAN);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        KirimDataIzin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: " + position);
                if (position==0){
                    Toast.makeText(requireActivity(), "harap pilih waktu dulu!!", Toast.LENGTH_SHORT).show();
                }else{
                    kirimDataIzin();
                }
//                Toast.makeText(requireActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {

            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            View radioButton = radioGroup.findViewById(radioButtonID);
            position = radioGroup.indexOfChild(radioButton);
            switch (i) {
                case R.id.rbJamw://hari
                    angka = position;
                    Log.d("angka", "onCreateView: "+angka);
//                    tipe = "1";
                    break;
                case R.id.rbHari://hari
                    showCalendar();
                    angka = position;
                    Log.d("angka", "onCreateView: "+angka);
//                    tipe = "1";
                    break;
                case R.id.rbJam://jam
                    showClock();
                    angka = position+2;
                    Log.d("angka", "onCreateView: "+angka);
//                    tipe = "2";
                    break;
                default:
                    radioGroup.clearCheck();
                    break;
            }
        });
        arrow_right.setOnClickListener(view13 -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
        AksiBottomSheet.setOnClickListener(view14 -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        return view;
    }

    private void parseJson(String GETID) {
        modelBeritaAcara.clear();
//        pd.show();
//        loadingDialog.StartLoadingDialogFragment();
        Log.d("parseJSON"+ GETID, "parseJson: ");
        StringRequest jArr = new StringRequest(Request.Method.POST,read_absensi,
                response -> {
                    JSONArray jsonArray;
                    try {
//                        pd.hide();
//                        loadingDialog.dissmisDialogFragment();
                        jsonArray = new JSONArray(response);

                        if (jsonArray.length()!=0){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonobject = jsonArray.getJSONObject(i);
                                String Judul          = jsonobject.getString("tanggal");
                                String Keterangan     = jsonobject.getString("jam");
                                String Status         = jsonobject.getString("status");
                                Log.d("cekbang", "parseJson: " + Status);
                                modelBeritaAcara.add(new ModelAbsen(Judul, Keterangan, Status));
                            }
                        }else{
                            InHide.setVisibility(View.VISIBLE);
                            Log.d("cekbang", "data kosong bang ");

                        }

                        mExampleAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
//                        loadingDialog.dissmisDialogFragment();
//                        pd.hide();
                        e.printStackTrace();
                        Log.d("parseJson catch"+ e, "parseJson: ");
                    }
                }, error -> {
            Activity activity = getActivity();
            if(activity != null && isAdded())
//                pd.hide();
            if (error instanceof NoConnectionError) {
                String errormsg = "error";
                Toast.makeText(activity, errormsg, Toast.LENGTH_LONG).show();
            }

//            pd.hide();
//            loadingDialog.dissmisDialogFragment();
            Log.d("volley error"+error, "onErrorResponse: ");
            Log.d("TAG", "onErrorResponse: ");
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_pegawai", GETID);
                return params;
            }};
        AppController.getInstance().addToRequestQueue(jArr, "tag_json_obj");
    }

    private void tampilDataJumlah(String GETID) {
//        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Total_Absensi, response -> {
            try {
                JSONObject jObj = new JSONObject(response);
                Log.d("cek", "tampilDataJumlah: " + jObj);
                success         = jObj.getInt(TAG_SUCCESS);
                if (success == 1) {
//                    pd.hide();
                    String jumlah_hadir = jObj.getString("hadir");
                    String jumlah_izin  = jObj.getString("izin");
                    String jumlah_alpa  = jObj.getString("alfa");
                    Hadir.setText(jumlah_hadir);
                    Izin.setText(jumlah_izin);
                    Alpa.setText(jumlah_alpa);
                } else {
//                    pd.hide();
//                    Toast.makeText(requireActivity(), "Sukses 0" , Toast.LENGTH_SHORT).show();
                    Log.d("suksesnya 0", "tampilDataJumlah: ");
                }

            } catch (JSONException e) {
//                pd.hide();
                e.printStackTrace();
                Log.d("TAG"+e, "tampilDataJumlah: ");
            }
        },
                error -> {
//                    pd.hide();
//            Toast.makeText(requireActivity(), "Error " + error.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("volley error jumlah"+ error, "tampilDataJumlah: ");
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_pegawai", GETID);
                Log.d("id_pegawai", "getParams: " + GETID);
                Log.i("id_pegawai", "getParams: " + GETID);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, TAG_JSON_OBJECT);
    }

    //    Spinner
    private void callData() {
        spinnerList.clear();
        JsonArrayRequest jArr = new JsonArrayRequest(URL_Spinner,
                response -> {
                    Log.e("response", response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            SpinnerModel item = new SpinnerModel();
                            item.setNama(obj.getString("nama"));
                            item.setIdJenisIzin(obj.getInt("id_jenis_izin"));
                            spinnerList.add(item);
                        } catch (JSONException e) {
                            Log.e("catch", "callData: ", e );
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();

                }, error -> {
//            VolleyLog.e("terjadi error spinner ", "Error: " + error.getMessage());
            Log.d("volley error"+error, "callData: ");

        });
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        requestQueue.add(jArr);

    }

    private void kirimDataIzin() {
        String dari     = Dari.getText().toString().trim();
        String sampai   = Sampai.getText().toString().trim();
        String Alasnya  = Alasan.getText().toString().trim();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, izin,
                response -> {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        success         = jObj.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Absensi()).commit();
                            Log.i("success", "kirimDataIzin: ");
                            ((BottomNavigationView)requireActivity().findViewById(R.id.bottomNavigationView)).setSelectedItemId(R.id.absen);
//                        ((BottomNavigationView) requireActivity().findViewById(R.id.bottomNavigation)).setSelectedItemId(R.id.absen);
                        }else{
//                            Toast.makeText(requireActivity(), "Maaf sepertianya ada masalah!!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
//                        e.printStackTrace();
                        Log.d("error catch izin", "kirimDataIzin: ");
                    }
                }, error -> {
            Log.d("error volley izin", "onErrorResponse: ");
            Log.e("error volley izin", "onErrorResponse: ");
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_pegawai", getId);
                params.put("start_izin", dari);
                params.put("end_izin", sampai);
                params.put("jenis_izin", String.valueOf(JenisIjinId));
                params.put("keterangan", Alasnya);
                params.put("tipe", String.valueOf(angka)); // disini tambahkan tipe
                Log.d("aa", "getParams: "+ tipe);
                return params;
            }};
        AppController.getInstance().addToRequestQueue(stringRequest, TAG_JSON_OBJECT);
    }

    private void showCalendar() {
        Dari.setEnabled(true);
        Sampai.setEnabled(true);
        Dari.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            mYear   = calendar.get(Calendar.YEAR);
            mMonth  = calendar.get(Calendar.MONTH);
            mDay    = calendar.get(Calendar.DAY_OF_MONTH);
            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year, monthOfYear, dayOfMonth) ->
                    Dari.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        Sampai.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            mYear   = calendar.get(Calendar.YEAR);
            mMonth  = calendar.get(Calendar.MONTH);
            mDay    = calendar.get(Calendar.DAY_OF_MONTH);
            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year, monthOfYear, dayOfMonth) ->
                    Sampai.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        });
    }

    @SuppressLint("SetTextI18n")
    private void showClock() {
        Dari.setEnabled(true);
        Sampai.setEnabled(true);
        Dari.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(getActivity(), (view1, hourOfDay, minute) ->
                    Dari.setText(hourOfDay + ":" + minute),
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),

                    DateFormat.is24HourFormat(getActivity()));

            timePickerDialog.show();


        });
        Sampai.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(getActivity(), (view12, hourOfDay, minute) ->
                    Sampai.setText(hourOfDay + ":" + minute),
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),

                    DateFormat.is24HourFormat(getActivity()));

            timePickerDialog.show();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        String text = parent.getItemAtPosition(i).toString();
        Log.d("select" + text, "onItemSelected: ");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onStop() {
        super.onStop();
        AppController.getInstance().cancelPendingRequests("tag_json_obj");
    }
}