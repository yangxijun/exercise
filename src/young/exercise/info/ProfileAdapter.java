package young.exercise.info;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProfileAdapter extends BaseAdapter{

	private Context mContext;
	private final List<Profile> mItems = new ArrayList<Profile>();
	
	public ProfileAdapter(Context context){
		this.mContext = context;
	}
	
	public void add(Profile profile){
		mItems.add(profile);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {

		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final Profile profile = (Profile)getItem(position);
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final RelativeLayout itemLayout = (RelativeLayout)inflater.inflate(R.layout.list_item, null);
		TextView profileId = (TextView)itemLayout.findViewById(R.id.profile_id);
		
		return null;
	}

}
