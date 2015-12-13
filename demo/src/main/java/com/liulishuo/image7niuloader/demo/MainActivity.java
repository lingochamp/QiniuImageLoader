package com.liulishuo.image7niuloader.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.liulishuo.image7niuloader.picassoloader.PicassoLoader;

/**
 * Created by Jacksgong on 12/13/15.
 */
public class MainActivity extends AppCompatActivity {
    private static String MOCK_DATA_URL = "http://7xjww9.com1.z0.glb.clouddn.com/20130221114001385.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();

        PicassoLoader.display7Niu(image1, MOCK_DATA_URL)
                .attach();

        PicassoLoader.display7Niu(image2, MOCK_DATA_URL)
                .centerCrop()
                .wR(R.dimen.image_2_width)
                .attach();

        PicassoLoader.display7Niu(image3, MOCK_DATA_URL)
                .size(dp2px(250))
                .addOpBlur(40, 20)
                .attach();

        PicassoLoader.display7Niu(image4, MOCK_DATA_URL)
                .w(dp2px(270))
                .addOpRotate(30)
                .attach();

        PicassoLoader.display7Niu(image5, MOCK_DATA_URL)
                .maxHalfW()
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

    private void assignViews() {
        image1 = (ImageView) findViewById(R.id.image_1);
        image2 = (ImageView) findViewById(R.id.image_2);
        image3 = (ImageView) findViewById(R.id.image_3);
        image4 = (ImageView) findViewById(R.id.image_4);
        image5 = (ImageView) findViewById(R.id.image_5);
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
        //TODO add github url
//        Uri uri = Uri.parse(getString(R.string.app_github_url));
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
    }
}
