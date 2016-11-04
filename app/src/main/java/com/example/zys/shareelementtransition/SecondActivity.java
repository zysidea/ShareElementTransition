package com.example.zys.shareelementtransition;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import static com.example.zys.shareelementtransition.Constant.IMAGE_ARRAY;

public class SecondActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Adapter mAdapter;
    private View mCurrentView;
    private int mCurrentPosition;
    private int mStartPosition;
    private boolean mIsReturning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        postponeEnterTransition();
        setEnterSharedElementCallback(mCallback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mStartPosition = getIntent().getIntExtra(Constant.EXTRA_START_POSITION, 0);
        if (savedInstanceState == null) {
            mCurrentPosition = mStartPosition;
        } else {
            mCurrentPosition = savedInstanceState.getInt(Constant.EXTRA_CURRENT_POSITION);
        }
        mAdapter = new Adapter();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mStartPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constant.EXTRA_CURRENT_POSITION, mCurrentPosition);
    }

    SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mIsReturning) {
                ImageView imageView = (ImageView) mCurrentView.findViewById(R.id.item_imageview);
                Rect rect = new Rect();
                getWindow().getDecorView().getHitRect(rect);
                if (imageView.getLocalVisibleRect(rect)) {
                    if (imageView == null) {
                        names.clear();
                        sharedElements.clear();
                    } else if (mStartPosition != mCurrentPosition) {
                        names.clear();
                        names.add(imageView.getTransitionName());
                        sharedElements.clear();
                        sharedElements.put(imageView.getTransitionName(), imageView);
                    }
                }
            }
        }
    };


    private class Adapter extends PagerAdapter {

        LayoutInflater mLayoutInflater;
        ImageView mImageView;

        public Adapter() {
            mLayoutInflater = LayoutInflater.from(SecondActivity.this);
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mLayoutInflater.inflate(R.layout.item_viewpager, container, false);
            mImageView = (ImageView) view.findViewById(R.id.item_imageview);
            mImageView.setTransitionName(Constant.TRANSITION_NAME + position);
            Drawable drawable = getResources().getDrawable(IMAGE_ARRAY[position]);
            mImageView.setImageDrawable(drawable);
            if (position == mStartPosition) {
                mImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
            }
            container.addView(view);
            return view;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentView = (View) object;
        }

        @Override
        public int getCount() {
            return Constant.IMAGE_ARRAY.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }

    @Override
    public void finishAfterTransition() {

        mIsReturning = true;
        Intent intent = new Intent();
        intent.putExtra(Constant.EXTRA_START_POSITION, mStartPosition);
        intent.putExtra(Constant.EXTRA_CURRENT_POSITION, mCurrentPosition);
        setResult(RESULT_OK, intent);
        super.finishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
