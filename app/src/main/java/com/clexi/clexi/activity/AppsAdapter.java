package com.clexi.clexi.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clexi.clexi.R;
import com.clexi.clexi.model.object.AppInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yousef on 5/30/2017.
 */

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder>
{
    public static final String TAG = AppsAdapter.class.getSimpleName();

    private Context              mContext;
    private List<AppInfo>        mDataset;
    private AppsAdapter.Callback mCallback;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AppsAdapter(Context context, List<AppInfo> dataset, AppsAdapter.Callback callback)
    {
        mContext = context;
        mDataset = dataset;
        mCallback = callback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AppsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);

        // set the view's size, margins, paddings and layout parameters
        //...

        AppsAdapter.ViewHolder vh = new AppsAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AppsAdapter.ViewHolder holder, final int position)
    {
        final AppInfo item = mDataset.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        // init views
        holder.title.setText(item.appName);
        holder.icon.setImageDrawable(item.icon);

        holder.rootLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doSomething(item);
            }
        });
    }

    private void doSomething(AppInfo item)
    {
        mCallback.onSelect(item);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    public void add(AppInfo item, int position)
    {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(BluetoothDevice item)
    {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.rootLayout)
        ViewGroup rootLayout;
        @BindView(R.id.title)
        TextView  title;
        @BindView(R.id.icon)
        ImageView icon;

        public ViewHolder(View view)
        {
            super(view);

            // find views
            // bind ButterKnife
            ButterKnife.bind(this, view);

            // statics
            //...
        }
    }

    /**
     * Callback
     */
    interface Callback
    {
        void onSelect(AppInfo item);
    }

    /**
     * Update
     */
    public void updateList(List<AppInfo> fDataset)
    {
        mDataset = fDataset;
        notifyDataSetChanged();
    }
}
