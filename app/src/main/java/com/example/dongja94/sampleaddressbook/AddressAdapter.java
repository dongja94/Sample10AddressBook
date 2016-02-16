package com.example.dongja94.sampleaddressbook;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2016-02-16.
 */
public class AddressAdapter extends BaseAdapter {
    List<AddressData> items = new ArrayList<AddressData>();
    public void addAll(List<AddressData> list) {
        items.addAll(list);
        notifyDataSetInvalidated();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position)._id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AddressView view;
        if (convertView == null) {
            view = new AddressView(parent.getContext());
        } else {
            view = (AddressView)convertView;
        }
        view.setAddressData(items.get(position));
        return view;
    }
}
