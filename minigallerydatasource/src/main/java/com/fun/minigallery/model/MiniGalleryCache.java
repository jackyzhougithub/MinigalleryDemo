package com.fun.minigallery.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author jacky_zhou
 * @version 2018/9/12.
 */
public class MiniGalleryCache {
    private static final String mock = "[\n" +
            "  {\n" +
            "    \"id\": 1,\n" +
            "    \"imageUrl\": \"https://wpclipart.com/education/animal_numbers/animal_number_1.jpg\",\n" +
            "    \"videoUrl\": \"https://media.giphy.com/media/l0ExncehJzexFpRHq/giphy.mp4\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 2,\n" +
            "    \"imageUrl\": \"https://wpclipart.com/education/animal_numbers/animal_number_2.jpg\",\n" +
            "    \"videoUrl\": \"https://media.giphy.com/media/26gsqQxPQXHBiBEUU/giphy.mp4\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 3,\n" +
            "    \"imageUrl\": \"https://wpclipart.com/education/animal_numbers/animal_number_3.jpg\",\n" +
            "    \"videoUrl\": \"https://media.giphy.com/media/oqLgjAahmDPvG/giphy.mp4\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 4,\n" +
            "    \"imageUrl\": \"https://wpclipart.com/education/animal_numbers/animal_number_4.jpg\",\n" +
            "    \"videoUrl\": \"https://media.giphy.com/media/d1E1szXDsHUs3WvK/giphy.mp4\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 5,\n" +
            "    \"imageUrl\": \"https://wpclipart.com/education/animal_numbers/animal_number_5.jpg\",\n" +
            "    \"videoUrl\": \"https://media.giphy.com/media/OiJjUsdAb11aE/giphy.mp4\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 6,\n" +
            "    \"imageUrl\": \"https://wpclipart.com/education/animal_numbers/animal_number_6.jpg\",\n" +
            "    \"videoUrl\": \"https://media.giphy.com/media/4My4Bdf4cakLu/giphy.mp4\"\n" +
            "  }\n" +
            "]";
    private List<GalleryInfo> galleryInfoList;

    private static final class Holder {
        private static MiniGalleryCache sInstance = new MiniGalleryCache();
    }

    private static Gson sGson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    public static MiniGalleryCache getInstance() {
        return Holder.sInstance;
    }

    public List<GalleryInfo> getGalleryList() {
        return galleryInfoList;
    }

    public void init() {
        // test
        loadFromRemote(mock);
    }

    private void loadFromRemote(String json) {
        Type listType = new TypeToken<List<GalleryInfo>>() {
        }.getType();
        galleryInfoList = sGson.fromJson(json, listType);
    }

    private MiniGalleryCache() {

    }

    public interface CacheChangedCallback {
        /**
         * 数据同步
         */
        void onSync();
    }
}
