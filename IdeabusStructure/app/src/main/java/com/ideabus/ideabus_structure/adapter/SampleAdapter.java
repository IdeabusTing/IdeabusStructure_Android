package com.ideabus.ideabus_structure.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class SampleAdapter extends BaseAdapter {

	private ArrayList<String> mUserData;
	private Context context;
	private Resources res;

	public interface OnClickListener {
		void onDelete(String name);
		void onGuestIn();
		void onSignIn(String name);
	}
	public OnClickListener mOnClickListener;
	public void setOnClickListener(OnClickListener l){
		mOnClickListener = l;
	}

	public SampleAdapter(Context context) {
		this.context = context;
		res = context.getResources();
	}

	public void setUserData(ArrayList<String> data) {
		mUserData = data == null ? new ArrayList<String>() : data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mUserData == null ? 0 : mUserData.size();
	}

	@Override
	public Object getItem(int i) {
		return mUserData.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
//		ViewHolder viewHolder;
//		if (view == null) {
//			view = View.inflate(context, R.layout.item_user_profiles, null);
//			viewHolder = new ViewHolder();
//			viewHolder.item_delete_imageview = (ImageView) view.findViewById(R.id.item_delete_imageview);
//			viewHolder.item_user_layout = (LinearLayout) view.findViewById(R.id.item_user_layout);
//
//			view.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) view.getTag();
//		}
//
//		if(position == 0){
//			viewHolder.item_delete_imageview.setVisibility(View.GONE);
//		}else{
//			final String name = mUserData.get(position).getName();
//			viewHolder.item_delete_imageview.setVisibility(View.VISIBLE);
//			viewHolder.item_delete_imageview.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					mOnClickListener.onDelete(name);
//				}
//			});
//		}

		return view;
	}

	class ViewHolder {
		LinearLayout item_user_layout;
		ImageView item_delete_imageview;
	}
}
