package com.example.eyekidneystone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eyekidneystone.logic.BitmapData;
import com.example.eyekidneystone.logic.Preprocess;
import com.example.eyekidneystone.logic.Result;

import java.text.DecimalFormat;

public class ResultScleraActivity extends AppCompatActivity {

    private ImageView ivFoto, ivFotoSclera, ivROIGinjal, ivResultSegmentasi;
    private TextView tvHasilIris;
    private Bitmap processBmp;
    private Preprocess preprocess;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_sclera);

        ivFoto = findViewById(R.id.ivFoto);
        ivFotoSclera = findViewById(R.id.ivFotoSclera);
        ivROIGinjal = findViewById(R.id.ivROIGinjal);
        ivResultSegmentasi = findViewById(R.id.ivResultSegmentasi);
        tvHasilIris = findViewById(R.id.tvHasilIris);

        processBmp = BitmapData.bitmap;
        df = new DecimalFormat("##.###");

        new PreprocessingTask().execute();

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResultOverviewActivity.class));
                finish();
            }
        });
    }

    private class PreprocessingTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            // Runs on the UI thread before doInBackground
            // Good for toggling visibility of a progress indicator
        }

        protected Void doInBackground(Void... strings) {
            // Some long-running task like downloading an image.
            preprocess = new Preprocess(processBmp);

            final Bitmap originalBmp = preprocess.getOriginalBmp();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivFoto.setImageBitmap(originalBmp);
                }
            });

            final Bitmap medianFilterBmp = preprocess.getMedianFilter(originalBmp);

            final Bitmap binerBmp = preprocess.getBinerization(medianFilterBmp);

            final Bitmap crop1Bmp = preprocess.getCrop1(binerBmp);

            Bitmap crop2Bmp = preprocess.getCrop2(crop1Bmp);

            crop2Bmp = Bitmap.createScaledBitmap(crop2Bmp, 300, 300, true);
            final Bitmap finalCropBmp = crop2Bmp;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivFotoSclera.setImageBitmap(finalCropBmp);
                }
            });

            final Bitmap grayScaleBmp = preprocess.getGrayScale(finalCropBmp);

            final Bitmap sobelBmp = preprocess.getSobel(grayScaleBmp);

            final Bitmap segmenRoiBmp = preprocess.getSegmentation(sobelBmp);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivROIGinjal.setImageBitmap(segmenRoiBmp);
                }
            });

            final Bitmap roiBmp = preprocess.getROI(sobelBmp);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivResultSegmentasi.setImageBitmap(roiBmp);
                }
            });

            final Bitmap ekstraksiFiturBmp = preprocess.getEkstraksiFitur(roiBmp);


            final Double[] ratio = preprocess.getRatio(ekstraksiFiturBmp);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvHasilIris.setText("Rasio Hitam: "+ df.format(ratio[0]) +"; Rasio Putih: "+df.format(ratio[1]));
                }
            });
            final String hasil = preprocess.getResult(ratio);

            new Result(hasil, "iris", ratio[0], ratio[1]);

            return null;
        }

        protected void onProgressUpdate(Void... values) {
            // Executes whenever publishProgress is called from doInBackground
            // Used to update the progress indicator
        }

        protected void onPostExecute(Void result) {
            // This method is executed in the UIThread
            // with access to the result of the long running task
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ScleraActivity.class));
        finish();
    }
}
