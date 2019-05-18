package com.example.eyekidneystone.logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Arrays;

public class Preprocess {
    private Bitmap originalBmp;

    public Preprocess(Bitmap originalBmp) {
        this.originalBmp = originalBmp;
    }

    public Bitmap getOriginalBmp() {
        return this.originalBmp;
    }

    public Bitmap getMedianFilter(Bitmap bitmap) {
        Bitmap mfBmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cMedianFilter = new Canvas(mfBmp);
        cMedianFilter.drawBitmap(mfBmp, 0, 0, new Paint());

        int[] w = new int[9];
        int[] wr = new int[9];
        int[] wg = new int[9];
        int[] wb = new int[9];

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                if (x - 1 < 0 || y - 1 < 0)
                    w[0] = Color.rgb(0, 0, 0);
                else
                    w[0] = bitmap.getPixel(x - 1, y - 1);

                if (y - 1 < 0)
                    w[1] = Color.rgb(0, 0, 0);
                else
                    w[1] = bitmap.getPixel(x, y - 1);

                if (x + 1 > bitmap.getWidth() - 1 || y - 1 < 0)
                    w[2] = Color.rgb(0, 0, 0);
                else
                    w[2] = bitmap.getPixel(x + 1, y - 1);

                if (x - 1 < 0)
                    w[3] = Color.rgb(0, 0, 0);
                else
                    w[3] = bitmap.getPixel(x - 1, y);

                w[4] = bitmap.getPixel(x, y);

                if (x + 1 > bitmap.getWidth() - 1)
                    w[5] = Color.rgb(0, 0, 0);
                else
                    w[5] = bitmap.getPixel(x + 1, y);

                if (x - 1 < 0 || y + 1 > bitmap.getHeight() - 1)
                    w[6] = Color.rgb(0, 0, 0);
                else
                    w[6] = bitmap.getPixel(x - 1, y + 1);

                if (y + 1 > bitmap.getHeight() - 1)
                    w[7] = Color.rgb(0, 0, 0);
                else
                    w[7] = bitmap.getPixel(x, y + 1);

                if (x + 1 > bitmap.getWidth() - 1 || y + 1 > bitmap.getHeight() - 1)
                    w[8] = Color.rgb(0, 0, 0);
                else
                    w[8] = bitmap.getPixel(x + 1, y + 1);


                for (int j = 0; j < w.length; j++) {
                    wr[j] = Color.red(w[j]);
                    wg[j] = Color.green(w[j]);
                    wb[j] = Color.blue(w[j]);
                }

                Arrays.sort(wr);
                Arrays.sort(wg);
                Arrays.sort(wb);

                int new_w = Color.rgb(wr[4], wg[4], wb[4]);
                mfBmp.setPixel(x, y, new_w);
            }
        }
        return mfBmp;
    }

    public Bitmap getSharpen(Bitmap bitmap) {
        Bitmap sharpBmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cSharp = new Canvas(sharpBmp);
        cSharp.drawBitmap(sharpBmp, 0, 0, new Paint());

        int[] filter_sharpen = {0, -1, 0, -1, 5, -1, 0, -1, 0};

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int[] w = new int[9];
                int[] wr = new int[9];
                int[] wg = new int[9];
                int[] wb = new int[9];
                int xr = 0, xg = 0, xb = 0;
                if (x - 1 < 0 || y - 1 < 0)
                    w[0] = Color.rgb(0, 0, 0);
                else
                    w[0] = bitmap.getPixel(x - 1, y - 1);

                if (y - 1 < 0)
                    w[1] = Color.rgb(0, 0, 0);
                else
                    w[1] = bitmap.getPixel(x, y - 1);

                if (x + 1 > bitmap.getWidth() - 1 || y - 1 < 0)
                    w[2] = Color.rgb(0, 0, 0);
                else
                    w[2] = bitmap.getPixel(x + 1, y - 1);

                if (x - 1 < 0)
                    w[3] = Color.rgb(0, 0, 0);
                else
                    w[3] = bitmap.getPixel(x - 1, y);

                w[4] = bitmap.getPixel(x, y);

                if (x + 1 > bitmap.getWidth() - 1)
                    w[5] = Color.rgb(0, 0, 0);
                else
                    w[5] = bitmap.getPixel(x + 1, y);

                if (x - 1 < 0 || y + 1 > bitmap.getHeight() - 1)
                    w[6] = Color.rgb(0, 0, 0);
                else
                    w[6] = bitmap.getPixel(x - 1, y + 1);

                if (y + 1 > bitmap.getHeight() - 1)
                    w[7] = Color.rgb(0, 0, 0);
                else
                    w[7] = bitmap.getPixel(x, y + 1);

                if (x + 1 > bitmap.getWidth() - 1 || y + 1 > bitmap.getHeight() - 1)
                    w[8] = Color.rgb(0, 0, 0);
                else
                    w[8] = bitmap.getPixel(x + 1, y + 1);


                for (int i = 0; i < w.length; i++) {
                    xr = xr + (Color.red(w[i]) * filter_sharpen[i]);
                    xg = xg + (Color.green(w[i]) * filter_sharpen[i]);
                    xb = xb + (Color.blue(w[i]) * filter_sharpen[i]);
                }
                if (xr < 0) xr = -xr;
                if (xr > 255) xr = 255;

                if (xg < 0) xg = -xg;
                if (xg > 255) xg = 255;

                if (xb < 0) xb = -xb;
                if (xb > 255) xb = 255;

                int new_w = Color.rgb(xr, xg, xb);
                sharpBmp.setPixel(x, y, new_w);
            }
        }
        bitmap = sharpBmp;
        return bitmap;
    }

    public Bitmap getBinerization(Bitmap bitmap) {
        Bitmap binerBmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cBiner = new Canvas(binerBmp);
        cBiner.drawBitmap(binerBmp, 0, 0, new Paint());
        int white = 255;
        for (int x = 0; x < binerBmp.getWidth(); x++) {
            for (int y = 0; y < binerBmp.getHeight(); y++) {
                int w = bitmap.getPixel(x, y);
                int r = Color.red(w);
                int g = Color.green(w);
                int b = Color.blue(w);

                double xr = Math.sqrt((Math.pow(r - white, 2)) + (Math.pow(g - white, 2)) + (Math.pow(b - white, 2)));

                int new_xr = 0;
                if (xr < 175)
                    new_xr = 255;

                int new_w = Color.rgb(new_xr, new_xr, new_xr);
                binerBmp.setPixel(x, y, new_w);
            }
        }
        return binerBmp;
    }

    public Bitmap getCrop1(Bitmap bitmap) {
        int binerWhite = 255;

        int[] horizontal = new int[bitmap.getWidth()];
        int[] vertical = new int[bitmap.getHeight()];

        //inisialisasi nilai horizontal dan vertical
        for (int i = 0; i < bitmap.getWidth(); i++)
            horizontal[i] = 0;
        for (int i = 0; i < bitmap.getHeight(); i++)
            vertical[i] = 0;

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int w = bitmap.getPixel(x, y);
                if (Color.red(w) == binerWhite && Color.green(w) == binerWhite && Color.blue(w) == binerWhite) {
                    horizontal[x]++;
                }
            }
        }

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int w = bitmap.getPixel(x, y);
                if (Color.red(w) == binerWhite && Color.green(w) == binerWhite && Color.blue(w) == binerWhite) {
                    vertical[y]++;
                }
            }
        }

        ///////////////////////////find point

        Bitmap bmp_point1 = getOriginalBmp();
        int leftPoint = 0, rightPoint = 0, topPoint = 0, bottomPoint = 0;
        int leftThreshold = 0, rightThreshold = 0, topThreshold = 0, bottomThreshold = 0;

        int leftIndexMinimum = bmp_point1.getWidth() / 100 * 22;
        int leftIndexMaximum = bmp_point1.getWidth() / 100 * 39;

        int rightIndexMinimum = bmp_point1.getWidth() / 100 * 88;
        int rightIndexMaximum = bmp_point1.getWidth() / 100 * 70;

        int topIndexMinimum = bmp_point1.getHeight() / 100 * 5;
        int topIndexMaximum = bmp_point1.getHeight() / 100 * 26;

        int bottomIndexMinimum = bmp_point1.getHeight() / 100 * 97; //78
        int bottomIndexMaximum = bmp_point1.getHeight() / 100 * 76; //68

        int mid_left_right = bmp_point1.getWidth() / 2;
        int mid_top_bottom = bmp_point1.getHeight() / 2;
        int left = 0;
        int top = 0;
        int rght = bmp_point1.getWidth() - 1;
        int btm = bmp_point1.getHeight() - 1;

        //////////////////CARI POINT START/////////////////////////////////
        //left
        for (int i = left; i <= leftIndexMaximum; i++) {
            if (horizontal[i] > leftThreshold) {
                leftThreshold = horizontal[i];
                leftPoint = i;
                //break;
            }
        }
        //right
        for (int i = rght; i >= rightIndexMaximum; i--) {
            if (horizontal[i] > rightThreshold) {
                rightThreshold = horizontal[i];
                rightPoint = i;
                //break;
            }
        }
        //top
        for (int i = top; i <= topIndexMaximum; i++) {
            if (vertical[i] > topThreshold) {
                topThreshold = vertical[i];
                topPoint = i;
                //break;
            }
        }
        //bottom
        for (int i = btm; i >= bottomIndexMaximum; i--) {
            if (vertical[i] > bottomThreshold) {
                bottomThreshold = vertical[i];
                bottomPoint = i;
                //break;
            }
        }

        if (leftThreshold <= 0)
            leftPoint = leftIndexMaximum;
        if (rightThreshold <= 0)
            rightPoint = rightIndexMaximum;
        if (topThreshold <= 0)
            topPoint = topIndexMaximum;
        if (bottomThreshold <= 0)
            bottomPoint = bottomIndexMaximum;

        //////////////////CARI POINT END/////////////////////////////////

        //////////////////CROP 1 START////////////////////////////////////
        int crop_width = rightPoint - leftPoint;
        int crop_height = bottomPoint - topPoint;

        Bitmap crop1Bmp = Bitmap.createBitmap(crop_width, crop_height, Bitmap.Config.ARGB_8888);
        //crop1BinerBmp = Bitmap.createBitmap(crop_width, crop_height, Bitmap.Config.ARGB_8888);

        Canvas crop = new Canvas(crop1Bmp);
        crop.drawBitmap(crop1Bmp, 0, 0, new Paint());

        int xc = 0, yc = 0;
        for (int x = leftPoint + 1; x <= rightPoint; x++) {
            yc = 0;
            for (int y = topPoint + 1; y <= bottomPoint; y++) {
                int color_ori = originalBmp.getPixel(x, y);
//                int color_biner = bitmap.getPixel(x, y);

                crop1Bmp.setPixel(xc, yc, color_ori);
                //  crop1BinerBmp.setPixel(xc, yc, color_biner);
                yc++;
            }
            xc++;
        }
        //////////////////CROP 1 END///////////////////////////////////
        return crop1Bmp;
    }

    public Bitmap getCrop2(Bitmap bitmap) {

        Bitmap crop1BinerBmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cropBiner = new Canvas(crop1BinerBmp);
        cropBiner.drawBitmap(crop1BinerBmp, 0, 0, new Paint());

        int black = 0;
        int white = 255;
        for (int x = 0; x < crop1BinerBmp.getWidth(); x++) {
            for (int y = 0; y < crop1BinerBmp.getHeight(); y++) {
                int w = bitmap.getPixel(x, y);
                int r = Color.red(w);
                int g = Color.green(w);
                int b = Color.blue(w);

                double xr = Math.sqrt((Math.pow(r - white, 2)) + (Math.pow(g - white, 2)) + (Math.pow(b - white, 2)));

                int new_xr = 0;
                if (xr < 175)
                    new_xr = 255;

                int new_w = Color.rgb(new_xr, new_xr, new_xr);
                crop1BinerBmp.setPixel(x, y, new_w);
            }
        }

        int leftPoint = 0, rightPoint = 0, topPoint = 0, bottomPoint = 0;
        int mid_width = crop1BinerBmp.getWidth() / 2;
        int mid_height = crop1BinerBmp.getHeight() / 2;


        int leftMax = crop1BinerBmp.getWidth() / 3;
        int rightMax = crop1BinerBmp.getWidth() / 3 * 2;

        int topMax = crop1BinerBmp.getHeight() / 3;
        int bottomMax = crop1BinerBmp.getHeight() / 3 * 2;

        //atas
        for (int y = 0; y <= topMax; y++) {
            int x = mid_width;
            int w = crop1BinerBmp.getPixel(x, y);
            if (Color.red(w) == black || y == topMax) {
                topPoint = y;
                break;
            }
        }

        //bawah
        for (int y = crop1BinerBmp.getHeight() - 1; y >= bottomMax; y--) {
            int x = mid_width;
            int w = crop1BinerBmp.getPixel(x, y);
            if (Color.red(w) == black || y == bottomMax) {
                bottomPoint = y;
                break;
            }
        }

        //kiri
        for (int x = 0; x <= leftMax; x++) {
            int y = mid_height;
            int w = crop1BinerBmp.getPixel(x, y);
            if (Color.red(w) == black || x == leftMax) {
                leftPoint = x;
                break;
            }
        }

        //kanan
        for (int x = crop1BinerBmp.getWidth() - 1; x >= rightMax; x--) {
            int y = mid_height;
            int w = crop1BinerBmp.getPixel(x, y);
            if (Color.red(w) == black || x == rightMax) {
                rightPoint = x;
                break;
            }

        }

        if (bottomPoint <= topPoint)
            bottomPoint = crop1BinerBmp.getHeight();
        if (rightPoint <= leftPoint)
            rightPoint = crop1BinerBmp.getWidth();

        int crop_width = rightPoint - leftPoint;
        int crop_height = bottomPoint - topPoint;
        Bitmap crop2BinerBmp = Bitmap.createBitmap(crop_width, crop_height, Bitmap.Config.ARGB_8888);
        Canvas crop2Biner = new Canvas(crop2BinerBmp);
        crop2Biner.drawBitmap(crop2BinerBmp, 0, 0, new Paint());

        int xc = 0, yc = 0;
        for (int x = leftPoint + 1; x <= rightPoint; x++) {
            yc = 0;
            for (int y = topPoint + 1; y <= bottomPoint; y++) {
                int w = bitmap.getPixel(x, y);
                crop2BinerBmp.setPixel(xc, yc, w);
                yc++;
            }
            xc++;
        }

        return crop2BinerBmp;
    }

    public Bitmap getGrayScale(Bitmap bitmap) {
        Bitmap grayBmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cGray = new Canvas(grayBmp);
        cGray.drawBitmap(grayBmp, 0, 0, new Paint());

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int w = bitmap.getPixel(x, y);
                int gray = (int) (Color.red(w) + Color.green(w) + Color.blue(w)) / 3;
                grayBmp.setPixel(x, y, Color.rgb(gray, gray, gray));
            }
        }
        return grayBmp;
    }

    public Bitmap getSobel(Bitmap bitmap) {
        Bitmap sobelBmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cSobel = new Canvas(sobelBmp);
        cSobel.drawBitmap(sobelBmp, 0, 0, new Paint());

        int sobelX[][] = {
                {-3, 0, 3},
                {-10, 0, 10},
                {-3, 0, 3}
        };

        int sobelY[][] = {
                {-3, -10, -3},
                {0, 0, 0},
                {3, 10, 3}
        };


//        Bitmap bmp_sobel_x;
        Bitmap bmp_sobel_x = bitmap.copy(bitmap.getConfig(), true);
        //konvolusi filter_x
//        bmp_sobel_x = sobelBmp;
        for (int y = 1; y < bitmap.getHeight() - 1; y++) {
            for (int x = 1; x < bitmap.getWidth() - 1; x++) {
                int hasil = getValueKonvolusi(sobelX, bitmap, x, y);
                bmp_sobel_x.setPixel(x, y, Color.rgb(hasil, hasil, hasil));
            }
        }

//        Bitmap bmp_sobel_y;
        Bitmap bmp_sobel_y = bitmap.copy(bitmap.getConfig(), true);
        //konvolusi filter_y
//        bmp_sobel_y = bitmap;
        for (int y = 1; y < bitmap.getHeight() - 1; y++) {
            for (int x = 1; x < bitmap.getWidth() - 1; x++) {
                int hasil = getValueKonvolusi(sobelY, bitmap, x, y);
                bmp_sobel_y.setPixel(x, y, Color.rgb(hasil, hasil, hasil));
            }
        }

        //sobel
        Bitmap bmp_sobel2a = bmp_sobel_x, bmp_sobel2b = bmp_sobel_y;
        //  Bitmap bmp_sobel_fix = bitmap;
        //  Bitmap bmp_sobel_fix = bitmap.copy(bitmap.getConfig(), true);

        for (int y = 1; y < bitmap.getHeight() - 1; y++) {
            for (int x = 1; x < bitmap.getWidth() - 1; x++) {
                int hasil = getMagnitude(bmp_sobel2a, bmp_sobel2b, x, y);

                sobelBmp.setPixel(x, y, Color.rgb(hasil, hasil, hasil));
            }
        }

//        bitmap = bmp_sobel_fix;
        return sobelBmp;
    }

    private int getValueKonvolusi(int[][] filter, Bitmap bmp1, int x, int y) {
        int value = 0;
        value += (filter[0][0] * Color.red(bmp1.getPixel(x - 1, y - 1)));
        value += (filter[0][1] * Color.red(bmp1.getPixel(x, y - 1)));
        value += (filter[0][2] * Color.red(bmp1.getPixel(x + 1, y - 1)));
        value += (filter[1][0] * Color.red(bmp1.getPixel(x - 1, y)));
        value += (filter[1][1] * Color.red(bmp1.getPixel(x, y)));
        value += (filter[1][2] * Color.red(bmp1.getPixel(x + 1, y)));
        value += (filter[2][0] * Color.red(bmp1.getPixel(x - 1, y + 1)));
        value += (filter[2][1] * Color.red(bmp1.getPixel(x, y + 1)));
        value += (filter[2][2] * Color.red(bmp1.getPixel(x + 1, y + 1)));

        if (value < 0) value = 0;
        if (value > 255) value = 255;

        return value;
    }

    private int getMagnitude(Bitmap bmp2a, Bitmap bmp2b, int x, int y) {
        int value1 = Color.red(bmp2a.getPixel(x, y));
        int value2 = Color.red(bmp2a.getPixel(x, y));
        int hasil = (int) Math.sqrt((Math.pow(value1, 2)) + Math.pow(value2, 2));
        if (hasil > 255)
            hasil = 255;
        return hasil;
    }

    public Bitmap getSegmentation(Bitmap bitmap) {
        Bitmap segmentationBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cSegmentation = new Canvas(segmentationBitmap);
        cSegmentation.drawBitmap(segmentationBitmap, 0, 0, new Paint());

//        int left = bitmap.getWidth() * 11 / 32;
        int left = bitmap.getWidth() * 16 / 32;
//        int right = bitmap.getWidth() * 13 / 32;
        int right = bitmap.getWidth() * 19 / 32;
//        int top = (int) (bitmap.getHeight() * 21 / 32);
        int top = (int) (bitmap.getHeight() * 24 / 32);
//        int bottom = (int) (bitmap.getHeight() * 23.5 / 32);
        int bottom = (int) (bitmap.getHeight() * 31 / 32);

        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                int color = bitmap.getPixel(i, j);

                if ((i == left || i == right) && j >= top && j <= bottom) {
                    segmentationBitmap.setPixel(i, j, Color.RED);
                } else if (i >= left && i <= right && (j == top || j == bottom)) {
                    segmentationBitmap.setPixel(i, j, Color.RED);
                } else {
                    segmentationBitmap.setPixel(i, j, Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
                }
            }
        }

        return segmentationBitmap;
    }

    public Bitmap getROI(Bitmap bitmap) {
        /*int left = bitmap.getWidth() * 11 / 32;
        int right = bitmap.getWidth() * 13 / 32;
        int top = (int) (bitmap.getHeight() * 21 / 32);
        int bottom = (int) (bitmap.getHeight() * 23.5 / 32);*/
        int left = bitmap.getWidth() * 16 / 32;
        int right = bitmap.getWidth() * 19 / 32;
        int top = (int) (bitmap.getHeight() * 24 / 32);
        int bottom = (int) (bitmap.getHeight() * 31 / 32);

        Bitmap roiBitmap = Bitmap.createBitmap(right - left, bottom - top, Bitmap.Config.ARGB_8888);
        Canvas cROI = new Canvas(roiBitmap);
        cROI.drawBitmap(roiBitmap, 0, 0, new Paint());

        int xb = 0, yb = 0;

        for (int x = left + 1; x <= right; x++) {
            for (int y = top + 1; y <= bottom; y++) {
                int color = bitmap.getPixel(x, y);
                roiBitmap.setPixel(xb, yb, Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
                yb++;
            }
            xb++;
            yb = 0;
        }

        return roiBitmap;
    }

    public Bitmap getEkstraksiFitur(Bitmap bitmap) {
        Bitmap ekstraksiFiturBmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cEkstraksiFitur = new Canvas(ekstraksiFiturBmp);
        cEkstraksiFitur.drawBitmap(ekstraksiFiturBmp, 0, 0, new Paint());

        int black = 0, white = 255;

        int new_xr = 0;
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int w = bitmap.getPixel(x, y);
                int r = Color.red(w);
                int g = Color.green(w);
                int b = Color.blue(w);

                double xr = Math.sqrt((Math.pow(r - white, 2)) + (Math.pow(g - white, 2)) + (Math.pow(b - white, 2)));

                if (xr < 350) {
                    new_xr = 255;
                } else {
                    new_xr = 0;
                }
                int new_w = Color.rgb(new_xr, new_xr, new_xr);
                ekstraksiFiturBmp.setPixel(x, y, new_w);
            }
        }
        return ekstraksiFiturBmp;
    }

    public Double[] getRatio(Bitmap bitmap) {
        int black = 0, white = 255;
        double black_count = 0, white_count = 0;
        double count = 0;
        int lain = 0;

        int new_xr = 0;
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int w = bitmap.getPixel(x, y);

                if (Color.red(w) == white) {
                    white_count++;
                } else {
                    black_count++;
                }
                count++;
            }
        }

        double r_black = 0.0, r_white = 0.0;
        r_black = black_count / count;
        r_white = white_count / count;

        Double[] ratio = {r_black, r_white};
        return ratio;
    }

    public String getResult(Double[] ratio) {
        String result = "";
        double th_white = 0.212;
        //double th_white = 0.25;
        if (ratio[1] > th_white) {
            result = "Abnormal";
            BitmapData.result = "ABNORMAL";
        } else {
            result = "Normal";
            BitmapData.result = "NORMAL";
        }
        return result;
    }
}