package com.matrixmuto.myandroidpractice;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WhetstoneActivity extends AppCompatActivity {

    private static final String CATEGORY_EXAMPLE = WhetstoneActivity.class.getPackage().getName();

    private static final String TAG_CLASS_NAME = "classname";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_INTENT = "intent";

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private String[] myDataset;
    private GestureDetectorCompat mGesture;

    @Bind(R.id.my_textview) TextView my_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whetstone);
        ButterKnife.bind(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myDataset = new String[]{"1","2","3","3","3","3","3","7"};
//        mAdapter = new MyAdapter(myDataset);
        mAdapter = new MyAdapter(getData());
        mRecyclerView.setAdapter(mAdapter);

        mGesture = new GestureDetectorCompat(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                int position = mRecyclerView.getChildPosition(view);
                my_textview.setText("pos="+position);
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = (Intent) mAdapter.mDataset.get(position).get(TAG_INTENT);
//                Intent intent = (Intent) map.get(TAG_INTENT);
                startActivity(intent);
            }
        });
//        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                mGesture.onTouchEvent(e);
//                return true;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> data = new ArrayList<>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.setPackage(getApplicationContext().getPackageName());
        mainIntent.addCategory(CATEGORY_EXAMPLE);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

        if (list == null) {
            return data;
        }

        for (ResolveInfo info : list) {
            CharSequence labelSep = info.loadLabel(pm);
            String label = labelSep != null ? labelSep.toString() : info.activityInfo.name;

            String[] labelPath = label.split("/");
            for (int i = 0; i < labelPath.length; i++) {
                Log.d(CATEGORY_EXAMPLE, labelPath[i]);
            }

            String nextLabel = labelPath[0];
            if (labelPath.length == 1) {
                String nameLabel = label;
                addItem(data, nameLabel, nextLabel, activityIntent(info.activityInfo.packageName, info.activityInfo.name));
            }

        }
        //Collections.sort(data, DISPLAY_NAME_COMPARABLE);
        return data;
    }

    private Intent activityIntent(String pkg, String componentName) {
        Intent intent = new Intent();
        intent.setClassName(pkg, componentName);
        return intent;
    }

    private void addItem(List<Map<String, Object>> data, String className, String description, Intent intent) {
        Map<String, Object> temp = new HashMap<>();
        temp.put(TAG_CLASS_NAME, className);
        temp.put(TAG_DESCRIPTION, description);
        temp.put(TAG_INTENT, intent);
        data.add(temp);
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        public List<? extends Map<String, ?>> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            @Bind(R.id.textView2) TextView mTextView;
            public ViewHolder(View v) {
                super(v);
                ButterKnife.bind(this,v);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<? extends Map<String, ?>> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);
            // set the view's size, margins, paddings and layout parameters
            //TextView v = new TextView(parent.getContext());
            //v.setText("test");
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            Map data = mDataset.get(position);
            holder.mTextView.setText((String)data.get(TAG_CLASS_NAME));

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
