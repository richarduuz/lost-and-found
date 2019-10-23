package com.example.maptest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListDemoAdapter extends ArrayAdapter<PublishItem> {
    static class ViewHolder{
        @BindView(R.id.list_image)
        ImageView image;
        @BindView(R.id.list_text)
        TextView text;

        ViewHolder(View view){ButterKnife.bind(this,view);}
    }

    private int resourceId;

    public ListDemoAdapter(Context context, int resource, List<PublishItem> objects){
        super(context, resource, objects);
        resourceId=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        PublishItem item=getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder= new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        String image = item.getItemImage();
        if (image == null || image.equals("null")){
            viewHolder.image.setImageResource(R.drawable.noimage);
        }
        else {
            byte[] data = Base64.decode(item.getItemImage(), Base64.DEFAULT);
            Bitmap bp = BitmapFactory.decodeByteArray(data, 0, data.length);
            viewHolder.image.setImageBitmap(bp);
        }
        viewHolder.text.setText(item.getItemName()+'\n'+item.getItemDiscription());

        return view;
    }
}
