package com.notepad.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.notepad.BuildConfig;
import com.notepad.R;
import com.notepad.appdata.AppManager;
import com.notepad.appdata.Codes;
import com.notepad.appdata.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    private Activity activity;
    private int      cameraPermission;
    private int      readFilePermission;
    private int      openGalleryPermission;
    private int      saveBitmapPermission;

    public ImageUtils(Activity activity) {

        this.activity = activity;

        cameraPermission = Codes.Permission.CAMERA;
        readFilePermission = Codes.Permission.READ_FILE;
        openGalleryPermission = Codes.Permission.OPEN_GALLERY;
        saveBitmapPermission = Codes.Permission.SAVE_BITMAP;
    }

    /**
     * Method to start the Camera
     */
    public void startCamera(String currentImageId) {
        /*  Check if the Permission for the Camera was Granted  */
        if (!AppManager.getInstance().askUserToGrantPermission(activity,
                                                               Manifest.permission.CAMERA, cameraPermission))
            return;


        /*  Check whether the Camera feature is available or not    */
        if (!isCameraAvailable()) {
            Utils.snackBar(activity, activity.getString(R.string.camera_feature_unavailable), Codes.SnackBarType.ERROR);
            return;
        }

        /*  Check for the SD CARD or External Storage   */
        if (!isExternalStorageAvailable()) {
            Utils.snackBar(activity, activity.getString(R.string.camera_feature_unavailable), Codes.SnackBarType.ERROR);
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {

            File fileToBeWritten = new File(Utils.getDirectory(activity, Constant.FileType.ATTACHMENT), currentImageId);

            if (!fileToBeWritten.exists()) {
                try {
                    fileToBeWritten.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity,
                                                                                               BuildConfig.APPLICATION_ID + ".provider",
                                                                                               fileToBeWritten));
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileToBeWritten));
            }
            activity.startActivityForResult(takePictureIntent, Codes.Request.OPEN_CAMERA_ADD_IMAGE);
        }
    }

    private boolean isCameraAvailable() {
        Log.e(TAG, "isCameraAvailable");
        return activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private boolean isExternalStorageAvailable() {
        Log.e(TAG, "isExternalStorageAvailable");
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public String compressImage(String fileName) {
        if (!AppManager.getInstance().askUserToGrantPermission(activity,
                                                               Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                               saveBitmapPermission)) return null;

        String filePath;
        Bitmap scaledBitmap;
        try {
            filePath = new File(Utils.getDirectory(activity, Constant.FileType.ATTACHMENT), fileName).getAbsolutePath();
            BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

            float maxHeight = 1.5f * 816.0f;
            float maxWidth = 1.5f * 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }

//      setting inSampleSize value allows to load a scaled down version of the original image

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            //          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);

            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);


            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                                               scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                                               true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filename = null;
        try {
            filename = saveBitmapImage(scaledBitmap, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    private String saveBitmapImage(Bitmap bitmap, String fileName) throws IOException {
        Log.e(TAG, "saveBitmapImage");
        String path = new File(Utils.getDirectory(activity, Constant.FileType.ATTACHMENT), fileName).getAbsolutePath();
        return convertBitmapToFile(bitmap, path);
    }

    /**
     * Method to convert a Bitmap to file
     *
     * @param bitmap
     * @param filePath
     * @throws IOException
     */
    private String convertBitmapToFile(Bitmap bitmap, String filePath) throws IOException {

        Log.e(TAG, "convertBitmapToFile");

        // Create a file to write bitmap data
        File file = new File(filePath);
        file.createNewFile();

        if (bitmap == null)
            return null;

        try {
            // Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70   /*ignored for PNG*/, bos);

            // Write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return file.getName();
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int requiredWidth, int requiredHeight) {

        Log.e(TAG, "calculateInSampleSize");

//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//
//        int inSampleSize = 1;
//
//        if (height > requiredHeight || width > requiredWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) > requiredHeight && (halfWidth / inSampleSize) > requiredWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;


        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > requiredHeight || width > requiredWidth) {
            final int heightRatio = Math.round((float) height / (float) requiredHeight);
            final int widthRatio = Math.round((float) width / (float) requiredWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = requiredWidth * requiredHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        Log.e(TAG, "calculateInSampleSize==" + inSampleSize);
        return inSampleSize;
    }

    public void copyFileFromUri(Uri uri, String fileName) throws IOException {

        Log.e(TAG, "copyFileFromUri");
        if (activity == null)
            return;

        InputStream inputStream = activity.getContentResolver().openInputStream(uri);

        FileOutputStream fileOutputStream = new FileOutputStream(
                new File(Utils.getDirectory(activity, Constant.FileType.ATTACHMENT), fileName)
        );

        byte[] buffer = new byte[1024];

        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1)
            fileOutputStream.write(buffer, 0, bytesRead);

        fileOutputStream.close();

        inputStream.close();
    }


    /**
     * Method to center crop a bitmap
     *
     * @param source
     * @return
     */
    public Bitmap centerCrop(Bitmap source) {

        Log.e(TAG, "centerCrop: Source bitmap: " + source);

        if (source == null) return null;


        if (source.getWidth() >= source.getHeight())
            return Bitmap.createBitmap(
                    source,
                    source.getWidth() / 2 - source.getHeight() / 2,
                    0,
                    source.getHeight(),
                    source.getHeight()
            );

        return Bitmap.createBitmap(
                source,
                0,
                source.getHeight() / 2 - source.getWidth() / 2,
                source.getWidth(),
                source.getWidth()
        );
    }

    public void openGallery() {

        Log.e(TAG, "openGallery");

        // Check and ask for Permissions
        if (!AppManager.getInstance().askUserToGrantPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, openGalleryPermission))
            return;

        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            activity.startActivityForResult(photoPickerIntent, Codes.Request.OPEN_GALLERY_ADD_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.snackBar(activity, activity.getString(R.string.no_gallery), Codes.SnackBarType.MESSAGE);
        }
    }
}
