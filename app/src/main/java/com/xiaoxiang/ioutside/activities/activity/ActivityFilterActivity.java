package com.xiaoxiang.ioutside.activities.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xiaoxiang.ioutside.R;
import com.xiaoxiang.ioutside.activities.adapter.FilterActivitiesAdapter;
import com.xiaoxiang.ioutside.activities.retrofit.Bean;
import com.xiaoxiang.ioutside.activities.retrofit.Query;
import com.xiaoxiang.ioutside.common.BaseActivity;
import com.xiaoxiang.ioutside.mine.adapter.OnItemClickListener;
import com.xiaoxiang.ioutside.mine.widget.TitleLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lwenkun on 16/9/6.
 */
public class ActivityFilterActivity extends BaseActivity implements OnItemClickListener {
    private final String TAG = "ActivityFilterActivity";

    public static final int TYPE_ACTIVITY = 0x00;
    public static final int TYPE_TRAINING = 0x01;

    private final int MENU_TYPE_DEST = 0x10;
    private final int MENU_TYPE_TYPE = 0x11;
    private final int MENU_TYPE_START_DATE = 0x12;
    private final int MENU_TYPE_PRICE = 0x13;

    private List<PopupWindowMenu.MenuItem> mTypeMenu;
    private List<PopupWindowMenu.MenuItem> mDestinationMenu;
    private List<PopupWindowMenu.MenuItem> mStartDateMenu;
    private List<PopupWindowMenu.MenuItem> mPriceMenu;

    private int resultCount;

    private final String titles[] = {"精选活动", "培训考证"};

    @Bind(R.id.title_layout)
    TitleLayout titleLayout;
    @Bind(R.id.radio_group)
    RadioGroup radio_group;
    @Bind(R.id.rv_activities)
    RecyclerView rvActivities;
    @Bind(R.id.rb_type)
    RadioButton rbType;
    @Bind(R.id.rb_destination)
    RadioButton rbDestination;
    @Bind(R.id.rb_start_date)
    RadioButton rbStartDate;
    @Bind(R.id.rb_price)
    RadioButton rbPrice;

    private int mType;
    private String mToken;

    private Integer subType;
    private Integer destination;
    private String startDate;
    private String endDate;
    private Integer lowPrice;
    private Integer highPrice;

    private PopupWindowMenu popupWindowMenu;

    private int mCurrMenuType;

    private FilterActivitiesAdapter mFilterActivitiesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_filter);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mToken = getIntent().getStringExtra("token");
        mType = getIntent().getIntExtra("type", -1);

        titleLayout.setTitleText(titles[mType - 1]);

        mFilterActivitiesAdapter = new FilterActivitiesAdapter(new ArrayList<>());
        mFilterActivitiesAdapter.setOnItemClickListener(this);
        rvActivities.setLayoutManager(new LinearLayoutManager(this));
        rvActivities.setAdapter(mFilterActivitiesAdapter);

        initData();
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.item_activities_or_training:
                viewActivityDetails(mFilterActivitiesAdapter.getDataSet().get(position));
                break;
        }
    }

    @OnClick({R.id.rb_destination, R.id.rb_price, R.id.rb_start_date, R.id.rb_type})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_destination:
                showPopupWindowMenu(MENU_TYPE_DEST);
                break;
            case R.id.rb_price:
                showPopupWindowMenu(MENU_TYPE_PRICE);
                break;
            case R.id.rb_start_date:
                showPopupWindowMenu(MENU_TYPE_START_DATE);
                break;
            case R.id.rb_type:
                showPopupWindowMenu(MENU_TYPE_TYPE);
                break;
        }
    }

    private void showPopupWindowMenu(int type) {

        mCurrMenuType = type;

        if (popupWindowMenu == null) {
            popupWindowMenu = new PopupWindowMenu(getLayoutInflater().inflate(R.layout.layout_pop_window, null));
           // popupWindowMenu.showAtLocation(rvActivities, Gravity.CENTER, 20, 20);
            popupWindowMenu.setOnMenuItemSelectedListener(item -> {
                switch (mCurrMenuType) {
                    case MENU_TYPE_DEST:
                        destination = item.position == 0 ? null : item.menuItemId;
                        rbDestination.setText(item.menuItemText);
                        break;
                    case MENU_TYPE_PRICE:
                        if (item.position != 0) {
                            lowPrice = item.position * 1000;
                            highPrice = item.position * 1000 + 1000;
                        } else {
                            lowPrice = null;
                            highPrice = null;
                        }
                        Log.d(TAG, "lowPrice -->" + lowPrice + ", highPrice -->" + highPrice);
                        rbPrice.setText(item.menuItemText);
                        break;
                    case MENU_TYPE_START_DATE:
                        if (item.position != 0) {
                            startDate = getStartDate(item.menuItemText);
                            endDate = getEndDate(item.menuItemText);
                        } else {
                            startDate = null;
                            endDate = null;
                        }
                        rbStartDate.setText(item.menuItemText);
                        break;
                    case MENU_TYPE_TYPE:
                        subType = item.position == 0 ? null : item.menuItemId;
                        rbType.setText(item.menuItemText);
                        break;
                }
                popupWindowMenu.dismiss();
                updateActivities();
            });
        }

        switch (type) {
            case MENU_TYPE_DEST:
                popupWindowMenu.setMenuList(mDestinationMenu);
                break;
            case MENU_TYPE_PRICE:
                popupWindowMenu.setMenuList(mPriceMenu);
                break;
            case MENU_TYPE_START_DATE:
                popupWindowMenu.setMenuList(mStartDateMenu);
                break;
            case MENU_TYPE_TYPE:
                popupWindowMenu.setMenuList(mTypeMenu);
                break;
        }

        popupWindowMenu.showAsDropDown(radio_group);

    }

    private String getStartDate(String in) {
        //ToastUtils.show(in + "-01");
        return in + "-01";
    }

    private String getEndDate(String in) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = dateFormat.parse(in);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
     //   ToastUtils.show(dateFormat.format(calendar.getTime()));
        return dateFormat.format(calendar.getTime()) + "-01";
    }

    private void updateActivities() {
        loadActivities(mType, 1, 20, subType, destination, startDate, endDate, lowPrice, highPrice);
    }

    private void viewActivityDetails(Bean.FilterActivities.Data.Item item) {
        Intent viewActivityDetails = new Intent(this, DetailActivity.class);
        viewActivityDetails.putExtra("token", mToken);
        viewActivityDetails.putExtra("activityId", item.activityId);
        startActivity(viewActivityDetails);
    }

    private void initData() {
        loadActivities(mType, 1, 20, null, null, null, null, null, null);
        initPriceMenu();
        getDestination();
        getStartDate();
        getType();
    }

    private void getType() {
        Query.getInstance().activityType(mType)
                .map(activityType -> activityType.data)
                .subscribe(data -> {
                    mTypeMenu = type2Menu(data.activitySubType);
                    resultCount++;
                });
    }

    private List<PopupWindowMenu.MenuItem> type2Menu(List<Bean.ActivityType.Data.Item> typeList) {
        List<PopupWindowMenu.MenuItem> items = new ArrayList<>();
        items.add(new PopupWindowMenu.MenuItem(0, "不限", null));
        for (int i = 0; i < typeList.size(); i++) {
            items.add(new PopupWindowMenu.MenuItem(i + 1, typeList.get(i).name, typeList.get(i).id));
        }
        return items;
    }

    private void getDestination() {
        Query.getInstance().destination(mType)
                .map(destination -> destination.data)
                .subscribe(data -> {
                    mDestinationMenu = destination2Menu(data.activityDestination);
                    resultCount++;
                });
    }

    private List<PopupWindowMenu.MenuItem> destination2Menu(List<Bean.Destination.Data.Item> destinations) {
        List<PopupWindowMenu.MenuItem> items = new ArrayList<>();
        items.add(new PopupWindowMenu.MenuItem(0, "不限", null));
        for (int i = 0; i < destinations.size(); i++) {
            items.add(new PopupWindowMenu.MenuItem(i + 1, destinations.get(i).name, destinations.get(i).id));
        }

        return items;
    }

    private void initPriceMenu() {
        List<PopupWindowMenu.MenuItem> items = new ArrayList<>();
        items.add(new PopupWindowMenu.MenuItem(0, "不限", null));
        for (int i = 0, price = 1000; price < 10000; price += 1000, i++) {
            items.add(new PopupWindowMenu.MenuItem(i + 1,
                    String.valueOf(price) + "-" + String.valueOf(price + 1000), null));
        }
        items.add(new PopupWindowMenu.MenuItem(10, "10000以上", null));
        mPriceMenu = items;
    }

    private void getStartDate() {
        Query.getInstance().activityDate(mType)
                .map(activityDate -> activityDate.data)
                .subscribe(data -> {
                    mStartDateMenu = startDate2Menu(data.activityDate);
                    resultCount++;
                });
    }

    private List<PopupWindowMenu.MenuItem> startDate2Menu(List<String> startDateList) {
        List<PopupWindowMenu.MenuItem> items = new ArrayList<>();
        items.add(new PopupWindowMenu.MenuItem(0, "不限", null));
        for (int i = 0; i < startDateList.size(); i++) {
            items.add(new PopupWindowMenu.MenuItem(i + 1, startDateList.get(i), null));
        }
        return items;
    }

    private void loadActivities(int type, int pageNo, int pageSize, Integer subType,
                                Integer destination, String startDate, String endDate,
                                Integer lowPrice, Integer highPrice) {
        Query.getInstance().filterActivities(type, pageNo, pageSize, subType, destination, startDate,
                endDate, lowPrice, highPrice)
                .map(filterActivities -> filterActivities.data)
                .subscribe(data -> mFilterActivitiesAdapter.replaceItems(data.activity));
    }

    private static class PopupWindowMenu {

        private PopupWindow mWindow;

        private List<MenuItem> mMenuItems = new ArrayList<>();

        private OnMenuItemSelectedListener mListener;

        private ListView mLvMenuItems;

        private Context mContext;

        private ArrayAdapter mAdapter;

        public PopupWindowMenu(View contentView) {
            mContext = contentView.getContext();
            mWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, true);
            initContentView(contentView);
        }

        private void initContentView(View contentView) {

            mLvMenuItems = (ListView) contentView.findViewById(R.id.lv_menu_items);
            mAdapter = new MenuItemsAdapter(mContext, R.layout.item_menu_item, mMenuItems);
            mLvMenuItems.setAdapter(mAdapter);
            mLvMenuItems.setOnItemClickListener((parent, view, position, id) -> {
                if (mListener != null && mMenuItems != null) {
                    mListener.onMenuItemSelected(mMenuItems.get(position));
                }
            });
        }

        private void setOnMenuItemSelectedListener(OnMenuItemSelectedListener listener) {
            mListener = listener;
        }

        public interface OnMenuItemSelectedListener {
            void onMenuItemSelected(MenuItem item);
        }

        private void setMenuList(List<MenuItem> menuItems) {
            mMenuItems.clear();
            mMenuItems.addAll(menuItems);
            mAdapter.notifyDataSetChanged();
        }

        public void showAsDropDown(View anchorView) {
            mWindow.showAsDropDown(anchorView);
        }

        public void dismiss() {
            if (mWindow != null) {
                mWindow.dismiss();
            }
        }

        public static class MenuItem {
            private int position;
            private String menuItemText;
            private Integer menuItemId;

            public MenuItem(int position, String menuItemText, Integer menuItemId) {
                this.position = position;
                this.menuItemId = menuItemId;
                this.menuItemText = menuItemText;
            }
        }

        private class MenuItemsAdapter extends ArrayAdapter<MenuItem> {

            private int resourceId;

            public MenuItemsAdapter(Context context, int resource, List<MenuItem> objects) {
                super(context, resource, objects);
                resourceId = resource;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                MenuItem item = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                viewHolder.tvMenuItem.setText(item.menuItemText);

                return convertView;
            }

            @Override
            public int getCount() {
                return mMenuItems == null ? 0 : mMenuItems.size();
            }

            private class ViewHolder {
                TextView tvMenuItem;

                public ViewHolder(View view) {
                    tvMenuItem = (TextView) view.findViewById(R.id.tv_menu_text);
                }
            }

        }

    }

}
