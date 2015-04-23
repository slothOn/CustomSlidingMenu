package com.zxc.biggis.view;

import com.zxc.biggis.R;

import android.app.Notification.Action;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * 实现主屏幕侧滑菜单
 * @author zxc
 * 
 */
public class SlidingMenu extends HorizontalScrollView {
	
	private LinearLayout mWrapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	private int mScreenWidth;
	private boolean once=false;
	private boolean isopen=false;
	private int mMenuWidth;
	
	private int mMenuRightPadding;//单位dp
	
	public SlidingMenu(Context context) {
		// TODO Auto-generated constructor stub
		this(context,null);
	}
	
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}
	/**
	 * 包含设置自定义属性
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		//获取窗口管理
		WindowManager wm=
				(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm=new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		mScreenWidth=dm.widthPixels;
		//dp 转px
		//mMenuRightPadding=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50, context.getResources().getDisplayMetrics());
		TypedArray ta=context.getTheme().
				obtainStyledAttributes(attrs, R.styleable.SlidingMenu, defStyle, 0);
		mMenuRightPadding=ta.getDimensionPixelSize(R.styleable.SlidingMenu_menuRightPadding, 
				(int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP,50, 
						context.getResources().getDisplayMetrics()));
		ta.recycle();
	}
	/*
	 * 设置子view宽高
	 * @see android.widget.HorizontalScrollView#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		if(!once){
			mWrapper=(LinearLayout) getChildAt(0);
			mMenu=(ViewGroup) mWrapper.getChildAt(0);
			mContent=(ViewGroup) mWrapper.getChildAt(1);
			mMenuWidth=mScreenWidth-mMenuRightPadding;
			mMenu.getLayoutParams().width=mMenuWidth;
			mContent.getLayoutParams().width=mScreenWidth;
			once=true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if(changed){
			//menu要移到的位置,scrollto
			this.scrollTo(mMenuWidth, 0);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action=ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			//相对屏幕原点的偏移量
			int scrollx=getScrollX();
			if(scrollx>=mMenuWidth/2){
				this.smoothScrollTo(mMenuWidth, 0);
				isopen=false;
			}else{
				this.smoothScrollTo(0, 0);
				isopen=true;
			}
			return true;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	public void toggleMenu(){
		if(isopen){
			this.smoothScrollTo(mMenuWidth, 0);
			isopen=false;
		}else{
			this.smoothScrollTo(0, 0);
			isopen=true;
		}
	}
	/**
	 * 滚动发生时
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		//调用属性动画，设置TranslationX
		float scale = l * 1.0f / mMenuWidth; // 1 ~ 0

		/**
		 * 区别1：内容区域1.0~0.7 缩放的效果 scale : 1.0~0.0 0.7 + 0.3 * scale
		 * 
		 * 区别2：菜单的偏移量需要修改
		 * 
		 * 区别3：菜单的显示时有缩放以及透明度变化 缩放：0.7 ~1.0 1.0 - scale * 0.3 透明度 0.6 ~ 1.0 
		 * 0.6+ 0.4 * (1- scale) ;
		 * 
		 */
		float rightScale = 0.7f + 0.3f * scale;
		float leftScale = 1.0f - scale * 0.3f;
		float leftAlpha = 0.6f + 0.4f * (1 - scale);

		// 调用属性动画，设置TranslationX
		mMenu.setTranslationX( mMenuWidth * scale * 0.8f);
		
		mMenu.setScaleX( leftScale);
		mMenu.setScaleY( leftScale);
		mMenu.setAlpha( leftAlpha);
		// 设置content的缩放的中心点
		mContent.setPivotX (0);
		mContent.setPivotY(mContent.getHeight() / 2);
		mContent.setScaleX(rightScale);
		mContent.setScaleY( rightScale);
	}
}
