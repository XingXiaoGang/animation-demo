package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xingxiaogang.animationdemo.view.ColorDotLoadingDrawable;
import com.example.xingxiaogang.animationdemo.view.RadarView;
import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private RadarView iconScanView;
    private ViewGroup mRootView;
    private String TAG = "TEST_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconScanView = (RadarView) findViewById(R.id.icon);
        mRootView = (ViewGroup) findViewById(R.id.rootView);

        ColorDotLoadingDrawable colorDotLoadingDrawable = new ColorDotLoadingDrawable(SizeUtils.dp2px(this, 2));
        ((ImageView) findViewById(R.id.loading_icon)).setImageDrawable(colorDotLoadingDrawable);

        iconScanView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //抛物线
                ParticleSystem ps2 = new ParticleSystem(MainActivity.this, 100, R.mipmap.star_pink, 4000);
                ps2.setScaleRange(0.5f, 1.0f);
                ps2.setSpeedModuleAndAngleRange(0.02f, 0.08f, 265, 360);
                ps2.setRotationSpeedRange(90, 360);
                ps2.setAcceleration(0.0001f, 80);
                ps2.setFadeOut(800);
                ps2.emit(iconScanView, 10);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    iconScanView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    iconScanView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iconScanView.stopFlat();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start: {
                iconScanView.start();
                break;
            }
            case R.id.stop: {
                iconScanView.stopFlat();
                break;
            }
            case R.id.cornerImage_window: {
                startActivity(new Intent(this, CornerImageActivity.class));
                break;
            }
            case R.id.list: {
                startActivity(new Intent(this, ListActivity.class));
                break;
            }
            case R.id.focus_open: {
                startActivity(new Intent(this, FocusActivity.class));
                break;
            }
            case R.id.sensor: {
                startActivity(new Intent(this, SensorActivity.class));
                break;
            }
            case R.id.welcome: {
                startActivity(new Intent(this, WelcomeActivity.class));
                break;
            }
            case R.id.window: {

                Dialog dialog = new Dialog(MainActivity.this, R.style.ShareDialog);
                dialog.show();


                break;
            }
        }
    }

    class Dialog extends android.app.Dialog {
        List<ResolveInfo> intentInfos = new ArrayList<>();

        public Dialog(@NonNull Context context, int style) {
            super(context, style);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            View item = View.inflate(MainActivity.this, R.layout.pop_bottom_menu, null);

            //加载数据 todo 后台
            loadShareInfos();
            //显示数据
            bindView((ViewGroup) item, intentInfos);

            getWindow().setGravity(Gravity.BOTTOM);
            getWindow().setContentView(item);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            lp.width = metrics.widthPixels;
            lp.height = (int) (metrics.heightPixels * 1.0f / 5 * 2);
            getWindow().setAttributes(lp);
            setCanceledOnTouchOutside(true);
        }

        private void loadShareInfos() {
            Intent intent = new Intent(Intent.ACTION_SEND, null);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("text/plain");
            PackageManager pm = getPackageManager();
            intentInfos = pm.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            ResolveInfo resolveInfo = new ResolveInfo();
            resolveInfo.activityInfo = new ActivityInfo();
            resolveInfo.activityInfo.packageName = "setting";
            resolveInfo.activityInfo.targetActivity = "edit";
            resolveInfo.nonLocalizedLabel = "设置";
            intentInfos.add(resolveInfo);
            for (ResolveInfo info : intentInfos) {
                Log.d("TEST_", "loadShareInfos: " + info.loadLabel(pm) + "," + info.activityInfo);
            }
        }

        void bindView(ViewGroup view, List<ResolveInfo> intentInfos) {
            ViewPager pager = (ViewPager) view.findViewById(R.id.view_pager);
            pager.setAdapter(new MenuPageAdapter(intentInfos));
        }
    }

    public static int rowCount = 2;
    public static int columns = 3;

    //页
    class MenuPageAdapter extends PagerAdapter {

        private List<ResolveInfo> mAllInfos = new ArrayList<>();


        public MenuPageAdapter(List<ResolveInfo> intentInfos) {
            //todo 动态计算行列
            mAllInfos.addAll(intentInfos);
        }

        @Override
        public int getCount() {
            return Math.round(mAllInfos.size() * 1.0f / (rowCount * columns));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            MenuPage page = new MenuPage(container.getContext(), getInfosForPage(position));
            container.addView(page);
            return page;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((MenuPage) object).destroy(container);
            object = null;

        }

        private List<ResolveInfo> getInfosForPage(int page) {
            int pageSize = rowCount * columns;
            List<ResolveInfo> infos = new ArrayList<>();
            for (int position = pageSize * page; position < pageSize * (page + 1) && position < mAllInfos.size(); position++) {
                infos.add(mAllInfos.get(position));
            }
            Log.d(TAG, "getInfosForPage: page=" + page + " size=" + infos.size() + " all size=" + mAllInfos.size());
            return infos;
        }
    }


    class MenuPage extends FrameLayout {

        GridView mGridView;

        public MenuPage(@NonNull Context context, List<ResolveInfo> infos) {
            super(context);
            inflate(context, R.layout.grid_layout, this);
            mGridView = (GridView) findViewById(R.id.grid_view);
            mGridView.setNumColumns(columns);
            int width = Resources.getSystem().getDisplayMetrics().widthPixels / columns;
            mGridView.setColumnWidth(width);
            mGridView.setVerticalSpacing(SizeUtils.dp2px(8));
            Adapter adapter = new Adapter(infos);
            mGridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        void destroy(ViewGroup parent) {
            if (getParent() == parent) {
                parent.removeView(this);
            }
        }

        class Adapter extends BaseAdapter {

            private List<ResolveInfo> mPageInfos;

            public Adapter(@NonNull List<ResolveInfo> pageInfos) {
                this.mPageInfos = pageInfos;
            }

            @Override
            public int getCount() {
                return mPageInfos.size();
            }

            @Override
            public ResolveInfo getItem(int position) {
                return mPageInfos.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Holder holder = null;
                if (convertView == null) {
                    convertView = View.inflate(parent.getContext(), R.layout.grid_item, null);
                    holder = new Holder(convertView);
                } else {
                    holder = (Holder) convertView.getTag();
                }
                convertView.setTag(holder);

                holder.bind(convertView.getContext(), getItem(position));

                return convertView;
            }
        }

        class Holder {
            ImageView mImageView;
            TextView mTextView;

            Holder(View itemView) {
                mImageView = (ImageView) itemView.findViewById(R.id.grid_img);
                mTextView = (TextView) itemView.findViewById(R.id.grid_txt);
            }


            public void bind(Context context, Object item) {
                ResolveInfo info = ((ResolveInfo) item);
                if (TextUtils.equals(info.activityInfo.targetActivity, "edit") && TextUtils.equals(info.activityInfo.packageName, "setting")) {
                    mImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.notification_icon));
                } else {
                    mImageView.setImageDrawable(info.loadIcon(context.getPackageManager()));
                }
                String l = "";
                CharSequence label = info.loadLabel(context.getPackageManager());
                if (label != null) {
                    l = label.toString().replaceAll("\\s*", "");
                }
                mTextView.setText(l);
            }
        }

    }


}
