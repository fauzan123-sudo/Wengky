package com.example.wengky.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.wengky.Dashboard;
import com.example.wengky.R;
import com.example.wengky.helper.AppController;
import com.example.wengky.helper.SessionManager;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static com.example.wengky.helper.Constans.CekScanning;
import static com.example.wengky.helper.Constans.TAG_JSON_OBJECT;
import static com.example.wengky.helper.Constans.TAG_SUCCESS;


public class Scanning extends Fragment {
    private CodeScanner mCodeScanner;
    ProgressDialog pDialog;
    String HasilScan;
    SessionManager sessionManager;
    String getId, data, Lokasi;
    int success;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_scanning, container, false);
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Log.e("SCAN QR CODE", "Permission already granted!");
            } else {
                requestPermission();
            }
        }


        Dashboard.hideBottomNav();
        sessionManager      = new SessionManager(requireActivity());
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(requireActivity(), scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HasilScan = result.toString();
                        Bundle bundle = new Bundle();
                        bundle.putString("key", String.valueOf(HasilScan));

                        CekAbsensi();

                        Toast.makeText(requireActivity(), result.getText(), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return view;
    }

    private void CekAbsensi() {
        pDialog = new ProgressDialog(requireActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, CekScanning, response -> {
            Log.e("TAG", "Responya: " + response);
            hideDialog();

            try {
                JSONObject jObj = new JSONObject(response);
                success         = jObj.getInt(TAG_SUCCESS);
                if (success == 1) {
                    hideDialog();
                    Toast.makeText(requireActivity(), "ya sukses", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Absensi()).commit();
//                    ((MeowBottomNavigation) requireActivity().findViewById(R.id.bottomNavigation)).setSelected(R.id.absen);
                } else {
                    hideDialog();
                    new AlertDialog.Builder(requireActivity())
                            .setTitle("Hasil Scan")
                            .setMessage("Maaf QR anda salah")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Scanning fragment2 = new Scanning();
                                    requireActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_container, fragment2)
                                            .commit();
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(requireActivity(), "eroor" + e, Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Log.e("Scan", "Scan Error: " + error.getMessage());
//            Toast.makeText(requireActivity(),
//                    error.getMessage(), Toast.LENGTH_LONG).show();
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(requireActivity())
                            .setTitle("Hasil Scan")
                            .setMessage("Maaf QR anda salah")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Scanning fragment2 = new Scanning();
                                    requireActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_container, fragment2)
                                            .commit();
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    hideDialog();
                }
            });


        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_pegawai", getId);
                Log.d("idcuy ", "getParams: " + getId);
                params.put("string_qr_code",HasilScan );
                params.put("status_scan","1" );

                Log.d("hasil", "getParams: " + HasilScan);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, TAG_JSON_OBJECT);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        Dashboard.hideBottomNav();
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                mCodeScanner.startPreview();
            } else {
                requestPermission();
            }
        }else{
            mCodeScanner.startPreview();
        }
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{CAMERA}, 200);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(requireActivity(),
                CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)requireActivity()).getSupportActionBar().show();
        Dashboard.showBottomNav();
    }


}