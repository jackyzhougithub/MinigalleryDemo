package minigrallery.fun.com.minigallerydemo;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fun.minigallery.ui.galleryrecyclellview.MiniGalleryView;
import com.fun.minigallery.model.GalleryEntity;
import com.fun.minigallery.viewmodel.MiniGalleryViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISION = 1;

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
        miniGalleryView = (MiniGalleryView) findViewById(R.id.gallery);
        miniGalleryView.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final MiniGalleryViewModel viewModel = ViewModelProviders.of(this).get(MiniGalleryViewModel.class);
        subscribeUi(viewModel);
    }

    private void subscribeUi(final MiniGalleryViewModel viewModel){
        viewModel.getGalleryList().observe(this, new Observer<List<GalleryEntity>>() {
            @Override
            public void onChanged(@Nullable List<GalleryEntity> galleryEntities) {
                if (galleryEntities == null){
                    return;
                }
                if (miniGalleryView != null){
                    miniGalleryView.updateData(galleryEntities);
                }
            }
        });
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
    }


}
