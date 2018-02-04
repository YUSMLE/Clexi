package com.clexi.clexi.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.clexi.clexi.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yousef on 2/4/2018.
 */

public class ConnectionSettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    public static final String TAG = ConnectionSettingsAdapter.class.getSimpleName();

    private final int TYPE_PAIRED_DEVICE_LABEL = 0;
    private final int TYPE_FOUND_DEVICE_LABEL  = 1;
    private final int TYPE_PAIRED_DEVICE       = 2;
    private final int TYPE_FOUND_DEVICE        = 3;

    /****************************************************
     * Values
     ***************************************************/

    private Context                            mContext;
    private List<BluetoothDevice>              mDataset;
    private ConnectionSettingsAdapter.Callback mCallback;

    /****************************************************
     * Provide a suitable constructor (depends on the kind of dataset)
     ***************************************************/

    public ConnectionSettingsAdapter(Context context, List<BluetoothDevice> dataset, ConnectionSettingsAdapter.Callback callback)
    {
        mContext = context;
        mDataset = dataset;
        mCallback = callback;
    }

    /****************************************************
     * Create new views (invoked by the layout manager)
     ***************************************************/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder holder   = null;
        LayoutInflater          inflater = LayoutInflater.from(parent.getContext());

        switch (viewType)
        {
            case TYPE_PAIRED_DEVICE_LABEL:
                View view0 = inflater.inflate(R.layout.item_connection_settings_paired_devices_label, parent, false);
                holder = new ViewHolder0(view0);
                // todo later...
                break;

            case TYPE_FOUND_DEVICE_LABEL:
                View view1 = inflater.inflate(R.layout.item_connection_settings_found_devices_label, parent, false);
                holder = new ViewHolder0(view1);
                // todo later...
                break;

            case TYPE_PAIRED_DEVICE:
                View view2 = inflater.inflate(R.layout.item_connection_settings_paired_device, parent, false);
                holder = new ViewHolder1(view2);
                // todo later...
                break;

            case TYPE_FOUND_DEVICE:
                View view3 = inflater.inflate(R.layout.item_connection_settings_found_device, parent, false);
                holder = new ViewHolder2(view3);
                // todo later...
                break;
        }

        return holder;

        // create a new view
        // set the view's size, margins, paddings and layout parameters
        // return view holder
    }

    /****************************************************
     * Replace the contents of a view (invoked by the layout manager)
     ***************************************************/

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        //final BluetoothDevice item = mDataset.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        switch (holder.getItemViewType())
        {
            case TYPE_PAIRED_DEVICE_LABEL:
                ViewHolder0 holder0 = (ViewHolder0) holder;
                configureViewHolder0(holder0, position);
                break;

            case TYPE_FOUND_DEVICE_LABEL:
                ViewHolder1 holder1 = (ViewHolder1) holder;
                configureViewHolder1(holder1, position);
                break;

            case TYPE_PAIRED_DEVICE:
                ViewHolder2 holder2 = (ViewHolder2) holder;
                configureViewHolder2(holder2, position);
                break;

            case TYPE_FOUND_DEVICE:
                ViewHolder3 holder3 = (ViewHolder3) holder;
                configureViewHolder3(holder3, position);
                break;
        }

        // init views in configureViewHolder()
    }

    // Init View TYPE_PAIRED_DEVICE_LABEL
    private void configureViewHolder0(ViewHolder0 holder, int position)
    {
        // todo later...
    }

    // Init View TYPE_FOUND_DEVICE_LABEL
    private void configureViewHolder1(ViewHolder1 holder, int position)
    {
        // todo later...
    }

    // Init View TYPE_PAIRED_DEVICE
    private void configureViewHolder2(ViewHolder2 holder, int position)
    {
        // todo later...
    }

    // Init View TYPE_FOUND_DEVICE
    private void configureViewHolder3(ViewHolder3 holder, int position)
    {
        // todo later...
    }

    /****************************************************
     * Functionality
     ***************************************************/

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        // Return view type depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous

        // todo later...

        return 0;
    }

    public void add(BluetoothDevice item, int position)
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

    private void doSomething(BluetoothDevice item)
    {
        mCallback.onSelect(item);
    }

    /****************************************************
     * Provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     ***************************************************/

    // View TYPE_LABEL
    public static class ViewHolder0 extends RecyclerView.ViewHolder
    {
        @BindView(R.id.rootLayout) ViewGroup rootLayout;
        @BindView(R.id.title)      TextView  title;

        public ViewHolder0(View view)
        {
            super(view);

            // Bind views using ButterKnife
            ButterKnife.bind(this, view);

            // Statics todo later...
        }
    }

    // View TYPE_PAIRED_DEVICE
    public static class ViewHolder1 extends RecyclerView.ViewHolder
    {
        @BindView(R.id.rootLayout) ViewGroup   rootLayout;
        @BindView(R.id.title)      TextView    title;
        @BindView(R.id.icon)       ImageButton search;

        public ViewHolder1(View view)
        {
            super(view);

            // Bind views using ButterKnife
            ButterKnife.bind(this, view);

            // Statics todo later...
        }
    }

    // View TYPE_PAIRED_DEVICE
    public static class ViewHolder2 extends RecyclerView.ViewHolder
    {
        @BindView(R.id.rootLayout) ViewGroup rootLayout;
        @BindView(R.id.icon)       ImageView icon;
        @BindView(R.id.title)      TextView  title;
        @BindView(R.id.subtitle)   TextView  subtitle;
        @BindView(R.id.setAsDefault) Switch    setAsDefault;

        public ViewHolder2(View view)
        {
            super(view);

            // Bind views using ButterKnife
            ButterKnife.bind(this, view);

            // Statics todo later...
        }
    }

    // View TYPE_FOUND_DEVICE
    public static class ViewHolder3 extends RecyclerView.ViewHolder
    {
        @BindView(R.id.rootLayout) ViewGroup rootLayout;
        @BindView(R.id.icon)       ImageView icon;
        @BindView(R.id.title)      TextView  title;
        @BindView(R.id.subtitle)   TextView  subtitle;
        @BindView(R.id.pair)       Button    pair;

        public ViewHolder3(View view)
        {
            super(view);

            // Bind views using ButterKnife
            ButterKnife.bind(this, view);

            // Statics todo later...
        }
    }

    /****************************************************
     * Callback
     ***************************************************/

    interface Callback
    {
        void onSelect(BluetoothDevice item);
    }
}
