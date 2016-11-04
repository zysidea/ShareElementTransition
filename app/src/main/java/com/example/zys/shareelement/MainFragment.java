package com.example.zys.shareelement;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.R.attr.text;
import static android.R.attr.y;
import static com.example.zys.shareelement.R.drawable.image2;
import static com.example.zys.shareelement.R.drawable.image3;
import static com.example.zys.shareelement.R.drawable.image4;

/**
 * Created by zys on 16/11/3.
 */

public class MainFragment extends Fragment{

    public static final int sImageArray[]={
        R.drawable.image1, image2,
        image3, image4
    };
    public static final String sTextArray[]={
        "image1","image2",
        "image3","image4"
    };

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
        View view = inflater.inflate(R.layout.fragment_content_main,container,false);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.recyclerview);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        private LayoutInflater mLayoutInflater;


        public MyAdapter() {
            mLayoutInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public int getItemCount() {
            return sImageArray.length;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {
            View view = mLayoutInflater.inflate(R.layout.item_recyclerview,parent,false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Drawable drawable = getActivity().getResources().getDrawable(sImageArray[position]);
            holder.imageView.setImageDrawable(drawable);
            holder.textView.setText(sTextArray[position]);
            holder.imageView.setTransitionName("image"+position);
            holder.textView.setTransitionName("text"+position);
        }


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public ImageView imageView;
            public TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                textView=(TextView)itemView.findViewById(R.id.item_textview);
                imageView=(ImageView)itemView.findViewById(R.id.item_imageview);
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SecondActivity.class);
                intent.putExtra("position",getAdapterPosition());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    Pair<View, String> pair1 = Pair.create((View) imageView, imageView.getTransitionName());
                    Pair<View, String> pair2 = Pair.create((View) textView, textView.getTransitionName());

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), pair1, pair2);
                    getActivity().startActivity(intent, options.toBundle());
                } else {
                    getActivity().startActivity(intent);
                }
            }
        }
    }
}
