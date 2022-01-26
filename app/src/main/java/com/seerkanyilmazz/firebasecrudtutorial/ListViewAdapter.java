package com.seerkanyilmazz.firebasecrudtutorial;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter<userList> extends BaseAdapter {

    Activity activity;
    List<User> userList;
    LayoutInflater layoutInflater;

    public ListViewAdapter(Activity activity, List<User> userList) {
        this.activity = activity;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView  = layoutInflater.inflate(R.layout.listview_item, null);

        TextView txtUserName  = itemView.findViewById(R.id.listUserName);
        TextView txtUserEmail = itemView.findViewById(R.id.listUserEmail);

        txtUserName.setText(userList.get(position).getName());
        txtUserEmail.setText(userList.get(position).getEmail());

        return itemView;
    }
}
