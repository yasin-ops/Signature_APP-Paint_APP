package com.muhammadyaseenfatimamazharsarfarz.signatureapp;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {
    int default_Color;
    SignatureView signature_view;
    ImageView btn_eraser, btn_color, btn_save;
    SeekBar penSize;
    TextView txtPenSize;
    private static String fileName;
    private AdView adView;
    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mySignature");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signature_view = findViewById(R.id.signature_view);
        penSize = findViewById(R.id.penSize);
        btn_eraser = findViewById(R.id.btn_eraser);
        btn_color = findViewById(R.id.btn_color);
        btn_save = findViewById(R.id.btn_save);
        txtPenSize = findViewById(R.id.txtPenSize);
        adView=findViewById(R.id.adView);
        askRunTimePermission();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String date = format.format(new Date());
        fileName = path + "/" + date + ".png";
        if (!path.exists()) {
            path.mkdir();
        }
        btn_eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signature_view.clearCanvas();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!signature_view.isBitmapEmpty()) {
                    saveImage();
                }
            }
        });
        default_Color = ContextCompat.getColor(MainActivity.this, R.color.black);
        penSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int size, boolean b) {
                txtPenSize.setText(size + "dp");
                signature_view.setPenSize(size);
                penSize.setMax(50);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();

            }
        });


    }

    private void saveImage() {
        File file = new File(fileName);
        Bitmap bitmap = signature_view.getSignatureBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitMapData = bos.toByteArray();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitMapData);
            fos.flush();
            fos.close();

            Toasty.normal(MainActivity.this, "This is General Info", Toast.LENGTH_LONG, ContextCompat.getDrawable(MainActivity.this, R.drawable.signature_logo)).show();


        } catch (IOException e) {
            e.printStackTrace();
            Toasty.error(this, "Image Could not Saved", Toast.LENGTH_LONG).show();
        }

    }

    private void openColorPicker() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(MainActivity.this, default_Color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {


            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                default_Color = color;
                signature_view.setPenColor(color);


            }
        });
        ambilWarnaDialog.show();

    }

    private void askRunTimePermission() {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        Toast.makeText(MainActivity.this, "Permission is allowed", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();


    }
}