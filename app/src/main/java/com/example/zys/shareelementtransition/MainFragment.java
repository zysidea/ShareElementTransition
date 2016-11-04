package com.example.zys.shareelementtransition;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import static com.example.zys.shareelementtransition.Constant.sImageArray;

/**
 * Created by zys on 16/11/3.
 */

public class MainFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    public MainFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
    }


    public ImageView getNewSharedElement(int position) {
        return (ImageView) mRecyclerView.findViewWithTag("tag" + position);
    }

    public void rennter(int startPosition, int currentPosition) {
        if (startPosition != currentPosition) {
            mRecyclerView.scrollToPosition(currentPosition);
        }
        getActivity().postponeEnterTransition();
        mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                mRecyclerView.requestLayout();
                getActivity().startPostponedEnterTransition();
                return true;
            }
        });
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private LayoutInflater mLayoutInflater;


        public MyAdapter() {
            mLayoutInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public int getItemCount() {
            return Constant.sImageArray.length;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.item_recyclerview, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Drawable drawable = getActivity().getResources().getDrawable(sImageArray[position]);
            holder.imageView.setImageDrawable(drawable);
            holder.imageView.setTransitionName( Constant.transitionName+ position);
            holder.imageView.setTag("tag" + position);

        }


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView imageView;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                imageView = (ImageView) itemView.findViewById(R.id.item_imageview);
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SecondActivity.class);
                intent.putExtra(Constant.EXTRA_START_POSITION, getAdapterPosition());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), imageView, imageView.getTransitionName());
                    getActivity().startActivity(intent, options.toBundle());
                } else {
                    getActivity().startActivity(intent);
                }
            }
        }
    }
}
