package minigrallery.fun.com.minigallerydemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fun.minigallery.galleryrecyclellview.MiniGalleryView;
import com.fun.minigallery.model.MiniGalleryCache;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MiniGalleryCache.getInstance().init();
        final MiniGalleryView miniGalleryView = (MiniGalleryView) findViewById(R.id.gallery);
        miniGalleryView.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        miniGalleryView.updateData(MiniGalleryCache.getInstance().getGalleryList());


    }
}
