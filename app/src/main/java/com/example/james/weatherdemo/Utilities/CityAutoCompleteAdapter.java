package com.example.james.weatherdemo.Utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;


public class CityAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    private Context mContext;
    private List<String> showUpList;
    private List<String> searchList;
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.toString().length() > 0) {

                List<String> founded = new ArrayList<>();
                for (String item : searchList) {
                    if (item.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        founded.add(item);
                        if (founded.size() > 10) break;
                    }
                }
                filterResults.values = founded;
                filterResults.count = founded.size();
            } else {
                synchronized (this) {
                    filterResults.values = new ArrayList<>();
                    filterResults.count = 0;

                }
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {

            notifyDataSetChanged();
            List<String> filteredList = (List<String>) filterResults.values;
            showUpList.clear();

            for (int i = 0; i < filteredList.size(); i++) {
                showUpList.add(filteredList.get(i));
            }
            notifyDataSetChanged();
        }
    };


    public CityAutoCompleteAdapter(Context context, int resource, List<String> list) {
        super(context, resource);
        mContext = context;

        this.showUpList = new ArrayList<>();
        this.searchList = new ArrayList<>();
        this.searchList = list;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public int getCount() {
        return showUpList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public String getItem(int position) {
        return showUpList.get(position);
    }

}