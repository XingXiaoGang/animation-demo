package com.example.xingxiaogang.animationdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
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
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xingxiaogang.animationdemo.view.PagerTabIndicatorView;
import com.example.xingxiaogang.animationdemo.view.RadarView;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity implements View.OnClickListener, PagerTabIndicatorView.onPagerTabSelectListener {

    private String TAG = "TEST_";
    public static int rowCount = 2;
    public static int columns = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initParticleAt(findViewById(R.id.icon));

        playWebp();
    }

    private void initParticleAt(final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //抛物线
                ParticleSystem ps2 = new ParticleSystem(MainActivity.this, 100, R.mipmap.star_pink, 4000);
                ps2.setScaleRange(0.5f, 1.0f);
                ps2.setSpeedModuleAndAngleRange(0.02f, 0.08f, 265, 360);
                ps2.setRotationSpeedRange(90, 360);
                ps2.setAcceleration(0.0001f, 80);
                ps2.setFadeOut(800);
                ps2.emit(view, 10);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });


        Observable.just("").doOnNext(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "call:  Observable.doOnNext " + (Looper.myLooper() == Looper.getMainLooper()));

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "call:  Observable.subscribe " + (Looper.myLooper() == Looper.getMainLooper()));

            }
        });


//        Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//
//                if (DEBUG) {
//                    Log.i(TAG, "call:  Observable.create " + (Looper.myLooper() == Looper.getMainLooper()));
//                }
//                subscriber.onNext("哈哈");
//            }
//        }).observeOn(Schedulers.io()).map(new Func1<String, String>() {
//            @Override
//            public String call(String s) {
//                if (DEBUG) {
//                    Log.i(TAG, "call:  map " + (Looper.myLooper() == Looper.getMainLooper()));
//                }
//                return null;
//            }
//        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                if (DEBUG) {
//                    Log.i(TAG, "call:  subscribe " + (Looper.myLooper() == Looper.getMainLooper()));
//                }
//            }
//        });

    }

    private void playWebp() {
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.img_user_type_effect);
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.drawable.user_noble_king_border))
                .build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                .build();
        draweeView.setController(draweeController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((RadarView) findViewById(R.id.icon)).stopFlat();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start: {
                ((RadarView) findViewById(R.id.icon)).start();
                break;
            }
            case R.id.stop: {
                ((RadarView) findViewById(R.id.icon)).stopFlat();
                break;
            }
            case R.id.image_window: {
                startActivity(new Intent(this, ImageActivity.class));
                break;
            }
            case R.id.list: {
                startActivity(new Intent(this, ListActivity.class));
                break;
            }
            case R.id.recycler: {
                startActivity(new Intent(this, RecylerActivity.class));
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
            case R.id.draw: {
                startActivity(new Intent(this, DrawTestActivity.class));
                break;
            }
            case R.id.action_menu: {
                startActivity(new Intent(this, ActionMenuActivity.class));
                break;
            }
            case R.id.swicher: {
                startActivity(new Intent(this, SwitchActivity.class));
                break;
            }
            case R.id.progress: {
                startActivity(new Intent(this, ProgressActivity.class));
                break;
            }
            case R.id.snow: {
                startActivity(new Intent(this, SnowActivity.class));
                break;
            }
            case R.id.game: {
                startActivity(new Intent(this, GameActivity.class));
                break;
            }
            case R.id.guide: {
                startActivity(new Intent(this, VoiceGuideActivity.class));
                break;
            }
            case R.id.waves: {
                startActivity(new Intent(this, MusicWaveActivity.class));
                break;
            }
            case R.id.flow_layout: {
                startActivity(new Intent(this, FlowLayoutActivity.class));
                break;
            }
        }
    }

    @Override
    public void onPageSelect(int tabIndex) {
        Log.d("GANG_", "onPageSelect: 首页tab选中 index=" + tabIndex);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "onConfigurationChanged: " + newConfig);
    }

    class Dialog extends android.app.Dialog implements ViewPager.OnPageChangeListener {
        private List<ResolveInfo> intentInfos = new ArrayList<>();
        private List<CheckedTextView> mIndicators = new ArrayList<>();
        private LinearLayout mIndicatorContainer;
        boolean isUpdatedHeight;

        public Dialog(@NonNull Context context, int style) {
            super(context, style);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final View item = View.inflate(MainActivity.this, R.layout.pop_bottom_menu, null);
            mIndicatorContainer = (LinearLayout) item.findViewById(R.id.dot_container);

            //加载数据 todo 后台
            loadShareInfos();
            //显示数据
            bindView((ViewGroup) item, intentInfos);

            getWindow().setGravity(Gravity.BOTTOM);
            getWindow().setContentView(item);

//            item.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//
//                    WindowManager.LayoutParams lp = getWindow().getAttributes();
//                    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
//                    lp.width = metrics.widthPixels;
//                    lp.height = item.getMeasuredHeight();
//                    getWindow().setAttributes(lp);
//
//                    item.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                }
//            });

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
                Log.i("TEST_", "loadShareInfos: " + info.loadLabel(pm) + "," + info.activityInfo);
            }
        }

        void bindView(ViewGroup menuView, List<ResolveInfo> intentInfos) {
            ViewPager pager = (ViewPager) menuView.findViewById(R.id.view_pager);
            MenuPageAdapter adapter = new MenuPageAdapter(intentInfos);
            pager.setAdapter(adapter);
            pager.addOnPageChangeListener(this);

            //指示器
            int pageCount = adapter.getCount();
            int size = SizeUtils.dp2px(5);
            if (pageCount > 1) {
                for (int i = 0; i < pageCount; i++) {
                    CheckedTextView dot = new CheckedTextView(menuView.getContext());
                    dot.setBackgroundResource(R.drawable.indicator_selector);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
                    lp.leftMargin = size;
                    lp.rightMargin = size;
                    mIndicatorContainer.addView(dot, lp);
                    mIndicators.add(dot);
                }
                updateDots(0);
            }

        }

        private void updateDots(int position) {
            if (mIndicators.size() > 1) {
                for (int i = 0; i < mIndicators.size(); i++) {
                    CheckedTextView checkble = mIndicators.get(i);
                    checkble.setChecked(position == i);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            updateDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        //页
        class MenuPageAdapter extends PagerAdapter {

            private List<ResolveInfo> mAllInfos = new ArrayList<>();


            public MenuPageAdapter(List<ResolveInfo> intentInfos) {
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
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                container.addView(page, lp);
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
                Log.i(TAG, "getInfosForPage: page=" + page + " size=" + infos.size() + " all size=" + mAllInfos.size());
                return infos;
            }
        }

        class MenuPage extends FrameLayout {

            GridView mGridView;

            public MenuPage(@NonNull Context context, List<ResolveInfo> infos) {
                super(context);
                final View child = View.inflate(context, R.layout.grid_layout, null);
                addView(child, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                //测量内容高度，以给viewpager设置
                if (!isUpdatedHeight) {
                    child.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            int childHeight = child.getMeasuredHeight();
                            Log.i(TAG, "onGlobalLayout: " + childHeight);
                            //更新窗口高度
                            WindowManager.LayoutParams lp = getWindow().getAttributes();
                            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
                            lp.width = metrics.widthPixels;
                            lp.height = childHeight + SizeUtils.dp2px(35);
                            getWindow().setAttributes(lp);

                            child.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    });
                    isUpdatedHeight = true;
                }
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


}
