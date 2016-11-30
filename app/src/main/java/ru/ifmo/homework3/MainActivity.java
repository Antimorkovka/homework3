package ru.ifmo.homework3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private String path;
    private String imagePath;
    private File imageFile;
    private Button startButton;
    private ImageView imageView;
    private TextView textView;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        path = this.getFilesDir().toString();
        imagePath = path + '/' +getString( R.string.image_name);

        startButton = (Button) findViewById(R.id.start_button);
        imageView = (ImageView) findViewById(R.id.image_view);
        textView = (TextView) findViewById(R.id.text_view);


        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        textView.setText(imagePath);
    }

    public void onStartClick(View view) {
        textView.setText(this.getFilesDir().toString());
        serviceIntent = new Intent(getApplicationContext(), ImageService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        textView.setText(imagePath);
    }
}
