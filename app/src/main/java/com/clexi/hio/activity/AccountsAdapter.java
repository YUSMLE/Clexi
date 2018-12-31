package com.clexi.hio.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.clexi.hio.R;
import com.clexi.hio.helper.PackageManagerHelper;
import com.clexi.hio.model.object.Account;
import com.clexi.hio.model.object.AppInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yousef on 4/26/2017.
 */

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder>
{
    public static final String TAG = AccountsAdapter.class.getSimpleName();

    private Context                  mContext;
    private List<Account>            mDataset;
    private AccountsAdapter.Callback mCallback;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AccountsAdapter(Context context, List<Account> dataset, AccountsAdapter.Callback callback)
    {
        mContext = context;
        mDataset = dataset;
        mCallback = callback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AccountsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);

        // set the view's size, margins, paddings and layout parameters
        // Nothing

        AccountsAdapter.ViewHolder vh = new AccountsAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AccountsAdapter.ViewHolder holder, final int position)
    {
        final Account item = mDataset.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        // init views
        holder.title.setText(item.getTitle());
        holder.username.setText(item.getUsername());
        holder.icon.setImageDrawable(getIcon(item.getTitle(), item.getAppId()));

        holder.rootLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doSomething(item);
            }
        });

    }

    private void doSomething(Account item)
    {
        mCallback.onSelect(item);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    public void add(Account item, int position)
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
        @BindView(R.id.username)
        TextView  username;
        @BindView(R.id.icon)
        ImageView icon;

        public ViewHolder(View view)
        {
            super(view);

            // find views
            // bind ButterKnife
            ButterKnife.bind(this, view);

            // statics
            // Nothing
        }
    }

    /**
     * Callback
     */
    interface Callback
    {
        void onSelect(Account item);
    }

    /**
     * Update
     */
    public void updateList(List<Account> fDataset)
    {
        mDataset = fDataset;
        notifyDataSetChanged();
    }

    /**
     * Icon
     */
    private Drawable getIcon(String title, String appId)
    {
        AppInfo appInfo = PackageManagerHelper.getAppInfo(mContext, appId);

        if (appInfo != null)
        {
            Drawable appIcon = PackageManagerHelper.getAppInfo(mContext, appInfo.packageName).icon;
                /*Drawable appIconSmall = new BitmapDrawable(mContext.getResources(),
                        Bitmap.createScaledBitmap(((BitmapDrawable) appIcon).getBitmap(),
                                ScreenHelper.convertToPixels(mContext, 24),
                                ScreenHelper.convertToPixels(mContext, 24),
                                true));*/

            return appIcon;
        }
        else
        {
            // using TextDrawable library for generating custom icon

            ColorGenerator colorGenerator;
            TextDrawable   textDrawable;

            colorGenerator = ColorGenerator.MATERIAL;
            textDrawable = TextDrawable.builder()
                    .beginConfig().toUpperCase().endConfig()
                    .buildRound(title.substring(0, 2), colorGenerator.getColor(title));

            return textDrawable;
        }
    }
}
