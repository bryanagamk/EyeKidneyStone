package com.example.eyekidneystone.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.eyekidneystone.CameraActivity;
import com.example.eyekidneystone.R;
import com.example.eyekidneystone.ResultIrisActivity;
import com.example.eyekidneystone.logic.BitmapData;
import com.github.clans.fab.FloatingActionButton;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private Button btnCheck;
    public ImageView ivCaptured;
    public Bitmap bmp_captured;
    FloatingActionButton fabCamera, fabGallery;
    private static final int PERMISSION_ALL = 101;

    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!hasPermissions(getContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        fabCamera = view.findViewById(R.id.fabCamera);
        fabGallery = view.findViewById(R.id.fabGallery);
        btnCheck = view.findViewById(R.id.btnCheck);
        ivCaptured = view.findViewById(R.id.ivCaptured);

        btnCheck.setVisibility(View.INVISIBLE);

        fabCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CameraActivity.class));
            }
        });

        fabGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent loadIntent = new Intent(Intent.ACTION_PICK);
                loadIntent.setType("image/*");
                startActivityForResult(loadIntent, 1);
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ResultIrisActivity.class));
                getActivity().finish();
            }
        });

        setImage(ivCaptured);

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            if (photoUri != null) {
                try {
                    bmp_captured = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                    ivCaptured.setImageBitmap(bmp_captured);
                    btnCheck.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onStop() {
        super.onStop();
        bmp_captured = null;
        BitmapData.bitmap = null;
    }

    public void setImage(ImageView imageView) {
        if (BitmapData.bitmap != null) {
            bmp_captured = BitmapData.bitmap;
            if (!BitmapData.processed) {
                bmp_captured = autoCrop(bmp_captured);
            }
            imageView.setImageBitmap(bmp_captured);
            btnCheck.setVisibility(View.VISIBLE);
            btnCheck.setEnabled(true);
        } else {
            btnCheck.setVisibility(View.INVISIBLE);
        }
    }

    private Bitmap autoCrop(Bitmap bitmap) {
        int left = bitmap.getWidth() / 6 * 2;
        int right = bitmap.getWidth() / 6 * 4;
        int top = bitmap.getHeight() / 8 * 3;
        int bottom = bitmap.getHeight() / 8 * 5;

        int widthBitmapCropped = right - left;
        int heightBitmapCropped = bottom - top;

        Bitmap croppedBitmap = Bitmap.createBitmap(widthBitmapCropped, heightBitmapCropped, Bitmap.Config.ARGB_8888);
        Canvas cAutoCrop = new Canvas(croppedBitmap);
        cAutoCrop.drawBitmap(croppedBitmap, 0, 0, new Paint());

        int xb = 0, yb = 0;

        for (int x = left + 1; x <= right; x++) {
            yb = 0;
            for (int y = top + 1; y <= bottom; y++) {
                int color = bitmap.getPixel(x, y);
                croppedBitmap.setPixel(xb, yb, Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
                yb++;
            }
            xb++;
        }

        BitmapData.bitmap = croppedBitmap;
        return croppedBitmap;
    }
}
