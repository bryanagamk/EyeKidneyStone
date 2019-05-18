package com.example.eyekidneystone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.eyekidneystone.logic.BitmapData;
import com.github.clans.fab.FloatingActionButton;

public class ScleraActivity extends AppCompatActivity {

    private Button btnCheck;
    public ImageView ivCaptured;
    public Bitmap bmp_captured;
    FloatingActionButton fabCamera, fabGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sclera);

        fabCamera = findViewById(R.id.fabCamera);
        fabGallery = findViewById(R.id.fabGallery);
        btnCheck = findViewById(R.id.btnCheck);
        ivCaptured = findViewById(R.id.ivCaptured);

        Button btnCheck = findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResultScleraActivity.class));
                finish();
            }
        });

        btnCheck.setVisibility(View.INVISIBLE);

        fabCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CameraTwoActivity.class));
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
                startActivity(new Intent(getApplicationContext(), ResultScleraActivity.class));
                finish();
            }
        });

        setImage(ivCaptured);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ResultIrisActivity.class));
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            if (photoUri != null) {
                try {
                    bmp_captured = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
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
