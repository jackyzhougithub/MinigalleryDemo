package minigrallery.fun.com.minigallerydemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fun.minigallery.ui.galleryrecyclellview.MiniGalleryView;
import com.fun.minigallery.model.GalleryEntity;
import com.fun.minigallery.repository.MiniGalleryCache;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MiniGalleryCache.CacheChangedCallback{

    private static final int REQUEST_PERMISION = 1;
    private static final String TEST_URL = "pictures";
    private MiniGalleryCache miniGalleryCache;
    private MiniGalleryView miniGalleryView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestSDpermission();
    }

    private void requestSDpermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISION);
        } else {
            initView();
        }
    }

    private void initView(){
        miniGalleryCache = new MiniGalleryCache(this);
        miniGalleryCache.init(TEST_URL);
        miniGalleryView = (MiniGalleryView) findViewById(R.id.gallery);
        miniGalleryView.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        miniGalleryCache.registerCallback(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initView();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        miniGalleryCache.unRegisterCallback(this);
    }

    @Override
    public void onSync(List<GalleryEntity> data) {

    }

}
