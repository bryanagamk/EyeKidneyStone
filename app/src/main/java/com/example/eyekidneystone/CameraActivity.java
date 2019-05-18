package com.example.eyekidneystone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.eyekidneystone.logic.BitmapData;
import com.example.eyekidneystone.logic.PreviewCamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class CameraActivity extends Activity {

    ImageView captureButton;
    Activity context;
    Camera camera;
    PreviewCamera previewCamera;
    FrameLayout frame;
    String path = "/sdcard/EyeKidneyStone/cache/images/";

    Camera.Parameters params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        context = this;

        captureButton = findViewById(R.id.captureButton);

        previewCamera = new PreviewCamera(this, (SurfaceView) findViewById(R.id.surfaceView));
        frame = findViewById(R.id.frameLayout);
        frame.addView(previewCamera);
        previewCamera.setKeepScreenOn(true);

        camera = Camera.open();
        params = camera.getParameters();

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    takeFocusedPicture();
                } catch (Exception e) {

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        releaseCameraAndPreview();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null) {
            camera = Camera.open();
            camera.startPreview();
            camera.setErrorCallback(new Camera.ErrorCallback() {
                public void onError(int error, Camera mcamera) {

                    camera.release();
                    camera = Camera.open();
                    Log.d("Camera died", "error camera");

                }
            });

        }
        if (camera != null) {
            if (Build.VERSION.SDK_INT >= 14)
                setCameraDisplayOrientation(context, Camera.CameraInfo.CAMERA_FACING_BACK, camera);
            previewCamera.setCamera(camera);
        }
    }

    private void releaseCameraAndPreview() {
        previewCamera.setCamera(null);
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {

            try {
                camera.takePicture(mShutterCallback, null, jpegCallback);
            } catch (Exception e) {

            }

        }
    };

    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub

        }
    };

    public void takeFocusedPicture() {
        camera.autoFocus(mAutoFocusCallback);

    }

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // Log.d(TAG, "onPictureTaken - raw");
        }
    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @SuppressWarnings("deprecation")
        public void onPictureTaken(byte[] data, Camera camera) {

            FileOutputStream outStream = null;
            Calendar c = Calendar.getInstance();
            File videoDirectory = new File(path);

            if (!videoDirectory.exists()) {
                videoDirectory.mkdirs();
            }

            try {
                // Write to SD Card
                outStream = new FileOutputStream(path + c.getTime().getSeconds() + ".jpg");
                outStream.write(data);
                outStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 5;

            options.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared

            options.inInputShareable = true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future


            Bitmap realImage = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(path + c.getTime().getSeconds() + ".jpg");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                Log.d("EXIF value",
                        exif.getAttribute(ExifInterface.TAG_ORIENTATION));
                if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                        .equalsIgnoreCase("1")) {
                    realImage = rotate(realImage, 90);
                } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                        .equalsIgnoreCase("8")) {
                    realImage = rotate(realImage, 90);
                } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                        .equalsIgnoreCase("3")) {
                    realImage = rotate(realImage, 90);
                } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                        .equalsIgnoreCase("0")) {
                    realImage = rotate(realImage, 90);
                }
            } catch (Exception e) {

            }

            Bitmap resized = Bitmap.createScaledBitmap(realImage, 2400, 3200, true);
            BitmapData.bitmap = resized;
            releaseCameraAndPreview();
            finish();

            Intent intent = new Intent(CameraActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };

    public static Bitmap rotate(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }
}

