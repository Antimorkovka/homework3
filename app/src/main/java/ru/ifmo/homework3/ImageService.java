package ru.ifmo.homework3;

import android.app.Service;
import android.content.AsyncTaskLoader;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static ru.ifmo.homework3.R.id.image;

public class ImageService extends Service {

    private String TAG = ImageService.class.getSimpleName();
    private ClipboardManager clipboard;
    private String clip = null;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreateService");
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                if (clipboard.hasPrimaryClip()) {
                    clip = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
                    ImageDownloader getImage = new ImageDownloader();
                    Log.d(TAG, "onPrimaryClipChanged");
                    getImage.startLoading();
                }
            }
        });
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ImageDownloader extends AsyncTaskLoader<Void> {

        ImageDownloader() {
            super(ImageService.this);

        }

        FileOutputStream out;

        @Override
        public Void loadInBackground() {
            try {
                Log.d(TAG, "loadInBackground");
                java.net.URL url = new java.net.URL(clip);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bm = BitmapFactory.decodeStream(input);
                connection.disconnect();

                File image = new File(this.getContext().getFilesDir(), getString(R.string.image_name));
                if (image.exists()) {
                    Log.d(TAG, "Found image");
                } else {
                    Log.d(TAG, "Not found");
                }


                out = new FileOutputStream(image);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                if (image.exists()) {
                    Log.d(TAG, "Found image");
                } else {
                    Log.d(TAG, "Not found");
                }
                out.close();


            } catch (IOException e) {
                Log.d(TAG, "ImageDownloader catch block" + e.toString());

            }
            return null;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }
    }


}
