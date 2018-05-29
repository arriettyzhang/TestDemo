package com.example.arrietty.demoapp.viewpage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.arrietty.demoapp.R;

public class NSViewPagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private NoScrollViewPager noScrollViewPager;
    private ImageView imageView1, imageView2;
    private int [] waveImg={R.drawable.a3201_img_original_wave, R.drawable.a3201_img_bassboost_wave, R.drawable.a3201_img_office_wave};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ns_viewpager);
        imageView1 = (ImageView)findViewById(R.id.showView);
        imageView2 = (ImageView)findViewById(R.id.hideView);
        noScrollViewPager = (NoScrollViewPager)findViewById(R.id.no_view_pager);
        noScrollViewPager.addOnPageChangeListener(this);
        noScrollViewPager.setNoScroll(true);
        noScrollViewPager.setAdapter(new MyAdapter(this, waveImg));
        noScrollViewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noScrollViewPager.removeOnPageChangeListener(this);
    }

    private int count=0;
    public void onCLick(View view){
        switch (view.getId()){
            case R.id.viewpageBtn:
                count++;
                noScrollViewPager.setCurrentItem(count%waveImg.length);
                break;
            case R.id.animBtn:
                if(imageView1.getVisibility() ==View.VISIBLE){
                    setHideAnimation(imageView1, 2000);
                    setShowAnimation(imageView2,2000);

                }else{
                    setHideAnimation(imageView2, 2000);
                    setShowAnimation(imageView1,2000);
                }
                break;
        }


    }

    private void animSet(final View showView){
        AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.view_anim_in);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                showView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        showView.startAnimation(animationSet);
    }

    private AnimationSet /*AlphaAnimation*/ mHideAnimation, mShowAnimation;
    /**
     * View渐隐动画效果
     */
    public  void setHideAnimation( final View view, int duration)
    {
        if (null == view || duration < 0)
        {
            return;
        }

        if (null != mHideAnimation)
        {
            mHideAnimation.cancel();
        }
        // 监听动画结束的操作
//        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
//        mHideAnimation.setDuration(duration);
//        mHideAnimation.setFillAfter(true);
        mHideAnimation = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.view_anim_out);
        mHideAnimation.setAnimationListener(new Animation.AnimationListener()
        {

            @Override
            public void onAnimationStart(Animation arg0)
            {

            }

            @Override
            public void onAnimationRepeat(Animation arg0)
            {

            }

            @Override
            public void onAnimationEnd(Animation arg0)
            {
                view.setVisibility(View.GONE);
            }
        });
        view.startAnimation(mHideAnimation);
    }

    /**
     * View渐现动画效果
     */
    public  void setShowAnimation( final View view, int duration)
    {
        if (null == view || duration < 0)
        {
            return;
        }
        if (null != mShowAnimation)
        {
            mShowAnimation.cancel();
        }
//        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
//        mShowAnimation.setDuration(duration);
//        mShowAnimation.setFillAfter(true);
        mShowAnimation = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.view_anim_in);
        mShowAnimation.setAnimationListener(new Animation.AnimationListener()
        {

            @Override
            public void onAnimationStart(Animation arg0)
            {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation arg0)
            {

            }

            @Override
            public void onAnimationEnd(Animation arg0)
            {

            }
        });
        view.startAnimation(mShowAnimation);
    }


    private void show2(View showView, int duration){
        showView.setVisibility(View.VISIBLE);
        showView.animate()
                .alpha(1f)
                .setDuration(duration)
                .setListener(null);
    }
    private void hide2(final View hideView, int duration){
        hideView.animate()
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideView.setVisibility(View.GONE);
                    }
                });
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    class MyAdapter extends PagerAdapter{
        private int[] imgArr;
        private Context mContext;
        public MyAdapter(Context context, int[] imgArr){
            this.imgArr = imgArr;
            mContext = context;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view  = LayoutInflater.from(mContext).inflate(R.layout.item_no_scroll_viewpager, container, false);
            ImageView modeImg = (ImageView) view.findViewById(R.id.img);
            modeImg.setImageResource(imgArr[position]);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public int getCount() {
            return imgArr.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
             return view == object;
        }
    }
}
