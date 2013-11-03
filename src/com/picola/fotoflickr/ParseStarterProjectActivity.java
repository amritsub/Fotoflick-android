package com.picola.fotoflickr;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class ParseStarterProjectActivity extends Activity {

    private GridView mGridView;
    private ProgressBar mProgressBar;
    private ParseQueryAdapter<ParseObject> mAdapter;

    private Date mLastHour;
    private ImageSize mTargetSize = new ImageSize(120, 80);

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAdapter.loadNextPage();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseAnalytics.trackAppOpened(getIntent());
        setContentView(R.layout.main);

        mLastHour = new Date();
        mLastHour.setHours(mLastHour.getHours() - 2);

        // Instantiate a QueryFactory to define the ParseQuery to be used for fetching items in this
        // Adapter.
        ParseQueryAdapter.QueryFactory<ParseObject> factory =
            new ParseQueryAdapter.QueryFactory<ParseObject>() {
              public ParseQuery<ParseObject> create() {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
                query.whereGreaterThan("createdAt", mLastHour);
                query.orderByDescending("createdAt");
                return query;
              }
            };
        mAdapter = new ParseQueryAdapter<ParseObject>(this, factory) {
            @Override
            public View getItemView(ParseObject object, View v, ViewGroup parent) {
                if (v == null) {
                    v = View.inflate(ParseStarterProjectActivity.this, R.layout.photo, null);
                }

                super.getItemView(object, v, parent);

                final ImageView imgView = (ImageView)v;
                ImageLoader.getInstance()
                    .loadImage(object.getString("imageURLString")
                            , mTargetSize
                            , new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    imgView.setImageBitmap(loadedImage);
                                }
                            });
                imgView.setOnClickListener(mListener);
                return v;
            }
        };

        // Perhaps set a callback to be fired upon successful loading of a new set of ParseObjects.
        mAdapter.addOnQueryLoadListener(new OnQueryLoadListener<ParseObject>() {
            @Override
            public void onLoading() {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoaded(List<ParseObject> objects, Exception e) {
                mProgressBar.setVisibility(View.GONE);
                android.util.Log.d("Foto", "onLoaded");
                if (objects != null) {
                    android.util.Log.d("Foto", "parsing objects: " + objects.size());
                }
            }
        });

        mGridView = (GridView) findViewById(R.id.photo_grid);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
