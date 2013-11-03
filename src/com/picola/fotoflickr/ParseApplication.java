package com.picola.fotoflickr;

import android.app.Application;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, "G1LnFzyc3IopA4RbTZ2viOsGU7oVnVjwfoEHKjSi",
                "l55mQ3AonoYqDdRqrnHST3CmDLPXDBUjJwhD9s2n");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

        ImageLoaderConfiguration config =
            new ImageLoaderConfiguration.Builder(this)
            .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
            .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
            .memoryCacheSize(2 * 1024 * 1024)
            .build();
        ImageLoader.getInstance().init(config);
    }

}
