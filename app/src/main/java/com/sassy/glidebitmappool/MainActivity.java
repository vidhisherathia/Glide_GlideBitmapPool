package com.sassy.glidebitmappool;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.glidebitmappool.GlideBitmapFactory;
import com.glidebitmappool.GlideBitmapPool;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlideBitmapPool.initialize(10*1024*1024);

        iv = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.prgsBar);
        progressBar.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load("https://moodle.htwchur.ch/pluginfile.php/124614/mod_page/content/4/example.jpg")
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                })
                .into(iv);

    }

    public void normalResource(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap b1 = BitmapFactory.decodeResource(getResources(),R.drawable.test1);
                b1.recycle();
                Bitmap b2 = BitmapFactory.decodeResource(getResources(),R.drawable.test2);
                b2.recycle();
                Bitmap b3 = BitmapFactory.decodeResource(getResources(),R.drawable.test3);
                b3.recycle();
                Bitmap b4 = BitmapFactory.decodeResource(getResources(),R.drawable.test4);
                b4.recycle();
                Bitmap b5 = BitmapFactory.decodeResource(getResources(),R.drawable.test5);
                b5.recycle();
            }
        }).start();
    }

    public void normalFile(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+
                        File.separator + "Download" + File.separator + "test";
                for (int i =1; i<=5; i++){
                    Bitmap b = BitmapFactory.decodeFile(path + i + ".png");
                    GlideBitmapPool.putBitmap(b);
                }
            }
        }).start();
    }

    public void resourceOptimized(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap b1 = BitmapFactory.decodeResource(getResources(),R.drawable.test1);
                GlideBitmapPool.putBitmap(b1);
                Bitmap b2 = BitmapFactory.decodeResource(getResources(),R.drawable.test2);
                GlideBitmapPool.putBitmap(b2);
                Bitmap b3 = BitmapFactory.decodeResource(getResources(),R.drawable.test3);
                GlideBitmapPool.putBitmap(b3);
                Bitmap b4 = BitmapFactory.decodeResource(getResources(),R.drawable.test4);
                GlideBitmapPool.putBitmap(b4);
                Bitmap b5 = BitmapFactory.decodeResource(getResources(),R.drawable.test5);
                GlideBitmapPool.putBitmap(b5);
            }
        }).start();
    }

    public void fileOptimized(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            File.separator + "Download" + File.separator + "test";
                    for (int i = 1; i <= 5; i++) {
                        Bitmap bitmap = GlideBitmapFactory.decodeFile(path + i + ".png");
                        GlideBitmapPool.putBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void downSample(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            File.separator + "Download" + File.separator + "test";
                    for (int i = 1; i <= 5; i++) {
                        Bitmap bitmap = GlideBitmapFactory.decodeFile(path + i + ".png",
                                100, 100);
                        GlideBitmapPool.putBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void clearMemory(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GlideBitmapPool.clearMemory();
            }
        }).start();
    }
}
