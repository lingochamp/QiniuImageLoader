package com.liulishuo.qiniuimageloader.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.liulishuo.qiniuimageloader.glide.GlideLoader;
import com.liulishuo.qiniuimageloader.utils.PicassoLoader;

/**
 * Created by Jacksgong on 12/13/15.
 */
public class MainActivity extends AppCompatActivity {
    private static String MOCK_DATA_URL = "http://image.youcute.cn/17-7-7/50670860.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();

        PicassoLoader.createLoader(image1, MOCK_DATA_URL)
                .attach();

        PicassoLoader.createLoader(image2, MOCK_DATA_URL)
                .centerCrop()
                .wR(R.dimen.image_2_width)
                .attach();

        PicassoLoader.createLoader(image3, MOCK_DATA_URL)
                .size(dp2px(250))
                .addOpBlur(40, 20)
                .attach();

        PicassoLoader.createLoader(image4, MOCK_DATA_URL)
                .w(dp2px(270))
                .addOpRotate(30)
                .attach();

        PicassoLoader.createLoader(image5, MOCK_DATA_URL)
                .halfScreenW()
                .attach();

        GlideLoader.createLoader(image6, MOCK_DATA_URL)
                .attach();

        GlideLoader.createLoader(image7, MOCK_DATA_URL)
                .centerCrop()
                .wR(R.dimen.image_2_width)
                .attach();

        GlideLoader.createLoader(image8, MOCK_DATA_URL)
                .size(dp2px(250))
                .addOpBlur(40, 20)
                .attach();

        GlideLoader.createLoader(image9, MOCK_DATA_URL)
                .w(dp2px(270))
                .addOpRotate(30)
                .attach();

        GlideLoader.createLoader(image10, MOCK_DATA_URL)
                .halfScreenW()
                .attach();
    }

    public int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private ImageView image5;

    private ImageView image6;
    private ImageView image7;
    private ImageView image8;
    private ImageView image9;
    private ImageView image10;

    private void assignViews() {
        image1 = findViewById(R.id.image_1);
        image2 = findViewById(R.id.image_2);
        image3 = findViewById(R.id.image_3);
        image4 = findViewById(R.id.image_4);
        image5 = findViewById(R.id.image_5);
        image6 = findViewById(R.id.image_6);
        image7 = findViewById(R.id.image_7);
        image8 = findViewById(R.id.image_8);
        image9 = findViewById(R.id.image_9);
        image10 = findViewById(R.id.image_10);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_github:
                openGitHub();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openGitHub() {
        Uri uri = Uri.parse(getString(R.string.app_github_url));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
