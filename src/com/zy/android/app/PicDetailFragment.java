package com.zy.android.app;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zy.android.R;
import com.zy.android.dos.Item;
import com.zy.android.utils.NetworkHelper;

public class PicDetailFragment extends Fragment {

	private String title;
//	private String description;
	private String image_url;
	
	private View view;
	
    static PicDetailFragment newInstance(Item item) {
    	PicDetailFragment f = new PicDetailFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("title", item.getTitle());
        args.putString("description", item.getDescription());
        args.putString("image_url", item.getImageUrl());
        // and more ...
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.title = getArguments() != null ? getArguments().getString("title") : "title";
//        this.description = getArguments() != null ? getArguments().getString("description") : "description";
        this.image_url = getArguments() != null ? getArguments().getString("image_url") : "image url";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pic_detail, container, false);
        ((TextView)(v.findViewById(R.id.pic_detail_title))).setText(this.title);
//        ((TextView)(v.findViewById(R.id.pic_detail_description))).setText(this.description);
        
        new DownloadImageTask().execute();
        this.view = v;
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    
    private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
	     protected Bitmap doInBackground(Void... urls) {
	    	 return NetworkHelper.fetchImage(PicDetailFragment.this.image_url);
	     }

	     protected void onPostExecute(Bitmap profileImg) {
	    	 if(profileImg != null) {
	    		 ((ImageView)(PicDetailFragment.this.view.findViewById(R.id.pic_detail_image))).setImageBitmap(profileImg);
	    	 } else {
	    		 // set some default image here
	    	 }
	     }

	}
}
