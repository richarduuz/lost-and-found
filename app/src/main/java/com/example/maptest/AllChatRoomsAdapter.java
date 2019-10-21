package com.example.maptest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllChatRoomsAdapter extends ArrayAdapter<ChatUser> {

    static class ViewHolder{
        @BindView(R.id.list_image)
        ImageView image;
        @BindView(R.id.list_text)
        TextView text;

        ViewHolder(View view){
            ButterKnife.bind(this,view);}
    }
    private int resourceId;

    public AllChatRoomsAdapter(Context context, int resource, List<ChatUser> chatUser){
        super(context, resource, chatUser);
        this.resourceId=resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ChatUser users=getItem(position);
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
        viewHolder.image.setImageResource(users.getUserIcon());
        viewHolder.text.setText(users.getUserName());
        return view;
    }
}
