package com.example.lwq.damaiclient.widget;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.lwq.damaiclient.R;

/**
 * 下拉和上拉的刷新
 * @author lwq
 * */
public class PullToRefreshView extends LinearLayout {
	private static final String TAG = "PullToRefreshView";

	/** 显示格式化日期模板 */
	private final static String DATE_FORMAT_STR = "yyyy年MM月dd日 HH:mm";
	// refresh states
	private static final int PULL_TO_REFRESH = 2;
	private static final int RELEASE_TO_REFRESH = 3;
	private static final int REFRESHING = 4;
	// pull state
	private static final int PULL_UP_STATE = 0;
	private static final int PULL_DOWN_STATE = 1;
	private static boolean noMore=false;
	/**
	 * last y
	 */
	private int mLastMotionY;
	/**
	 * lock
	 */
	private boolean mLock;
	/**
	 * header view
	 */
	private View mHeaderView;
	/**
	 * footer view
	 */
	private View mFooterView;
	/**
	 * list or grid
	 */
	private AdapterView<?> mAdapterView;
	/**
	 * scrollview
	 */
	private ScrollView mScrollView;
	/**
	 * header view height
	 */
	private int mHeaderViewHeight;
	/**
	 * footer view height
	 */
	private int mFooterViewHeight;
	/**
	 * header view image
	 */
	private ImageView mHeaderImageView;
	/**
	 * footer view image
	 */
	private ImageView mFooterImageView;
	/**
	 * header tip text
	 */
	private TextView mHeaderTextView;
	/**
	 * footer tip text
	 */
	private TextView mFooterTextView;
	/**
	 * header refresh time
	 */
	private TextView mHeaderUpdateTextView;
	/**
	 * footer refresh time
	 */
	// private TextView mFooterUpdateTextView;
	/**
	 * header progress bar
	 */
	private ProgressBar mHeaderProgressBar;
	/**
	 * footer progress bar
	 */
	private ProgressBar mFooterProgressBar;
	/**
	 * layout inflater
	 */
	private LayoutInflater mInflater;
	/**
	 * header view current state
	 */
	private int mHeaderState;
	/**
	 * footer view current state
	 */
	private int mFooterState;
	/**
	 * pull state,pull up or pull down;PULL_UP_STATE or PULL_DOWN_STATE
	 */
	private int mPullState;
	/**
	 * 变为向下的箭头,改变箭头方向
	 */
	private RotateAnimation mFlipAnimation;
	/**
	 * 变为逆向的箭头,旋转
	 */
	private RotateAnimation mReverseFlipAnimation;
	/**
	 * footer refresh listener
	 */
	private OnFooterRefreshListener mOnFooterRefreshListener;
	/**
	 * footer refresh listener
	 */
	private OnHeaderRefreshListener mOnHeaderRefreshListener;
	/**
	 * last update time
	 */
	//	private String mLastUpdateTime;

	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullToRefreshView(Context context) {
		super(context);
		init();
	}

	/**
	 * init
	 * 
	 * @description
	 *  lwq 2012-7-26上午10:08:33
	 */
	private void init() {
		isLoadMore =true;//是否存在上拉功能
		noMore=false;
		mFlipAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(250);
		mFlipAnimation.setFillAfter(true);
		mReverseFlipAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(250);
		mReverseFlipAnimation.setFillAfter(true);

		mInflater = LayoutInflater.from(getContext());
		// header view 在此添加,保证是第一个添加到linearlayout的最上端
		addHeaderView();
	}

	private void addHeaderView() {
		// header view
		mHeaderView = mInflater.inflate(R.layout.refresh_header, this, false);

		mHeaderImageView = (ImageView) mHeaderView
				.findViewById(R.id.pull_to_refresh_image);
		mHeaderTextView = (TextView) mHeaderView
				.findViewById(R.id.pull_to_refresh_text);
		mHeaderUpdateTextView = (TextView) mHeaderView
				.findViewById(R.id.pull_to_refresh_updated_at);
		mHeaderProgressBar = (ProgressBar) mHeaderView
				.findViewById(R.id.pull_to_refresh_progress);
		// header layout
		measureView(mHeaderView);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				mHeaderViewHeight);
		// 设置topMargin的值为负的header View高度,即将其隐藏在最上方
		params.topMargin = -(mHeaderViewHeight);
		// mHeaderView.setLayoutParams(params1);
		addView(mHeaderView, params);

	}

	private void addFooterView() {
		// footer view
		mFooterView = mInflater.inflate(R.layout.refresh_footer, this, false);
		mFooterImageView = (ImageView) mFooterView
				.findViewById(R.id.pull_to_load_image);
		mFooterTextView = (TextView) mFooterView
				.findViewById(R.id.pull_to_load_text);
		mFooterProgressBar = (ProgressBar) mFooterView
				.findViewById(R.id.pull_to_load_progress);
		// footer layout
		measureView(mFooterView);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				mFooterViewHeight);
		// int top = getHeight();
		// params.topMargin
		// =getHeight();//在这里getHeight()==0,但在onInterceptTouchEvent()方法里getHeight()已经有值了,不再是0;
		// getHeight()什么时候会赋值,稍候再研究一下
		// 由于是线性布局可以直接添加,只要AdapterView的高度是MATCH_PARENT,那么footer view就会被添加到最后,并隐藏
		addView(mFooterView, params);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// footer view 在此添加保证添加到linearlayout中的最后
		addFooterView();
		initContentAdapterView();
	}

	/**
	 * init AdapterView like ListView,GridView and so on;or init ScrollView
	 * 
	 * @description hylin 2012-7-30下午8:48:12
	 */
	private void initContentAdapterView() {
		int count = getChildCount();
		if (count < 3) {
			throw new IllegalArgumentException(
					"this layout must contain 3 child views,and AdapterView or ScrollView must in the second position!");
		}
		View view = null;
		for (int i = 0; i < count - 1; ++i) {
			view = getChildAt(i);
			if (view instanceof AdapterView<?>) {
				mAdapterView = (AdapterView<?>) view;
			}
			if (view instanceof ScrollView) {
				// finish later
				mScrollView = (ScrollView) view;
			}
		}
		if (mAdapterView == null && mScrollView == null) {
			throw new IllegalArgumentException(
					"must contain a AdapterView or ScrollView in this layout!");
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int y = (int) e.getRawY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 首先拦截down事件,记录y坐标
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// deltaY > 0 是向下运动,< 0是向上运动
			int deltaY = y - mLastMotionY;
			if (isRefreshViewScroll(deltaY)) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return false;
	}

	/*
	 * 如果在onInterceptTouchEvent()方法中没有拦截(即onInterceptTouchEvent()方法中 return
	 * false)则由PullToRefreshView 的子View来处理;否则由下面的方法来处理(即由PullToRefreshView自己来处理)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mLock) {
			return true;
		}
		int y = (int) event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// onInterceptTouchEvent已经记录
			// mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = y - mLastMotionY;
			if (mPullState == PULL_DOWN_STATE) {//下拉刷新
				// PullToRefreshView执行下拉
				Log.i(TAG, " pull down!parent view move!");
				headerPrepareToRefresh(deltaY);
				// setHeaderPadding(-mHeaderViewHeight);
			} else if (mPullState == PULL_UP_STATE) {//上拉加载更多
				// PullToRefreshView执行上拉
				Log.i(TAG, "pull up!parent view move!");
				if(isLoadMore){
					footerPrepareToRefresh(deltaY);
				}

			}
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL://松开的监听
			int topMargin = getHeaderTopMargin();
			if (mPullState == PULL_DOWN_STATE) {
				if (topMargin >= 0) {
					// 开始刷新
					headerRefreshing();
				} else {
					// 还没有执行刷新，重新隐藏
					setHeaderTopMargin(-mHeaderViewHeight);
				}
			} else if (mPullState == PULL_UP_STATE) {
				if(isLoadMore){
					if (!noMore) {
						// 开始执行footer 刷新
						footerRefreshing();
					} else {
						// 还没有执行刷新，重新隐藏
						setHeaderTopMargin(-mHeaderViewHeight);
					}
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 是否应该到了父View,即PullToRefreshView滑动
	 * 
	 * @param deltaY
	 *            , deltaY > 0 是向下运动,< 0是向上运动
	 * @return
	 */
	private boolean isRefreshViewScroll(int deltaY) {
		if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
			return false;
		}
		//对于ListView和GridView
		if (mAdapterView != null) {
			// 子view(ListView or GridView)滑动到最顶端
			if (deltaY > 0) {

				View child = mAdapterView.getChildAt(0);
				if (child == null) {
					// 如果mAdapterView中没有数据,不拦截
					return false;
				}
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& child.getTop() == 0) {
					mPullState = PULL_DOWN_STATE;
					return true;
				}
				int top = child.getTop();
				int padding = mAdapterView.getPaddingTop();
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& Math.abs(top - padding) <= 8) {//这里之前用3可以判断,但现在不行,还没找到原因
					mPullState = PULL_DOWN_STATE;
					return true;
				}

			} else if (deltaY < 0) {
				View lastChild = mAdapterView.getChildAt(mAdapterView
						.getChildCount() - 1);
				if (lastChild == null) {
					// 如果mAdapterView中没有数据,不拦截
					return false;
				}
				// 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
				// 等于父View的高度说明mAdapterView已经滑动到最后
				if (lastChild.getBottom() <= getHeight()
						&& mAdapterView.getLastVisiblePosition() == mAdapterView
						.getCount() - 1) {
					mPullState = PULL_UP_STATE;
					return true;
				}
			}
		}
		// 对于ScrollView
		if (mScrollView != null) {
			// 子scroll view滑动到最顶端
			View child = mScrollView.getChildAt(0);
			if (deltaY > 0 && mScrollView.getScrollY() == 0) {
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < 0
					&& child.getMeasuredHeight() <= getHeight()
					+ mScrollView.getScrollY()) {
				mPullState = PULL_UP_STATE;
				return true;
			}
		}
		return false;
	}

	/**
	 * header 准备刷新,手指移动过程,还没有释放
	 * 
	 * @param deltaY
	 *            ,手指滑动的距离
	 */
	private void headerPrepareToRefresh(int deltaY) {
		 updateTextTime();
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// 当header view的topMargin>=0时，说明已经完全显示出来了,修改header view 的提示状态
		if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {
			mHeaderTextView.setText(R.string.pull_to_refresh_release_label);
			mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimation);
			mHeaderState = RELEASE_TO_REFRESH;
		} else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {// 拖动时没有释放
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimation);
			// mHeaderImageView.
			mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
			mHeaderState = PULL_TO_REFRESH;
		}
	}

	/**
	 * footer 准备刷新,手指移动过程,还没有释放 移动footer view高度同样和移动header view
	 * 高度是一样，都是通过修改header view的topmargin的值来达到
	 * 
	 * @param deltaY
	 *            ,手指滑动的距离
	 */
	private void footerPrepareToRefresh(int deltaY) {

		@SuppressWarnings("unused")
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// 如果header view topMargin 的绝对值大于或等于header + footer 的高度
		// 说明footer view 完全显示出来了，修改footer view 的提示状态
		if(noMore){
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimation);
			mFooterTextView.setText("已全部加载完毕");
			mFooterState = PULL_TO_REFRESH;
			return;
		} 
		if (mFooterState != RELEASE_TO_REFRESH) {
			mFooterTextView
			.setText(R.string.pull_to_refresh_footer_release_label);
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimation);
			mFooterState = RELEASE_TO_REFRESH;
		}  
	}

	/**
	 * 修改Header view top margin的值
	 * 
	 * @description
	 * @param deltaY
	 * @return lwq 2012-7-31下午1:14:31
	 */
	private int changingHeaderViewTopMargin(int deltaY) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		float newTopMargin = params.topMargin + deltaY * 0.3f;
		//这里对上拉做一下限制,因为当前上拉后然后不释放手指直接下拉,会把下拉刷新给触发了,
		//表示如果是在上拉后一段距离,然后直接下拉
		if(deltaY>0&&mPullState == PULL_UP_STATE&&Math.abs(params.topMargin) <= mHeaderViewHeight){
			return params.topMargin;
		}
		//同样地,对下拉做一下限制,避免出现跟上拉操作时一样的bug
		if(deltaY<0&&mPullState == PULL_DOWN_STATE&&Math.abs(params.topMargin)>=mHeaderViewHeight){
			return params.topMargin;
		}
		params.topMargin = (int) newTopMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
		return params.topMargin;
	}

	/**
	 * 下拉刷新进行时
	 * 
	 * @description lwq 2012-7-31上午9:10:12
	 */
	private void headerRefreshing() {
		mHeaderState = REFRESHING;
		noMore=false;
		setHeaderTopMargin(0);
		mHeaderImageView.setVisibility(View.GONE);
		mHeaderImageView.clearAnimation();
		mHeaderImageView.setImageDrawable(null);
		mHeaderProgressBar.setVisibility(View.VISIBLE);
		mHeaderTextView.setText(R.string.pull_to_refresh_refreshing_label);
		if (mOnHeaderRefreshListener != null) {
			mOnHeaderRefreshListener.onHeaderRefresh(this);
		}
	}

	/**
	 * 加载更多进行时
	 * 
	 * @description lwq  
	 */
	private void footerRefreshing() {
		mFooterState = REFRESHING;
		int top = mHeaderViewHeight + mFooterViewHeight;
		setHeaderTopMargin(-top);
		mFooterImageView.setVisibility(View.GONE);
		mFooterImageView.clearAnimation();
		mFooterImageView.setImageDrawable(null);
		mFooterProgressBar.setVisibility(View.VISIBLE);
		mFooterTextView
		.setText(R.string.pull_to_refresh_footer_refreshing_label);
		if (mOnFooterRefreshListener != null) {
			mOnFooterRefreshListener.onFooterRefresh(this);
		}
	}

	/**
	 * 设置header view 的topMargin的值
	 * 
	 * @description
	 * @param topMargin
	 *            ，为0时，说明header view 刚好完全显示出来； 为-mHeaderViewHeight时，说明完全隐藏了
	 *            lwq 2012-7-31上午11:24:06
	 */
	private void setHeaderTopMargin(int topMargin) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		params.topMargin = topMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
	}

	/**
	 * header view 完成更新后恢复初始状态
	 * 
	 * @description lwq 2012-7-31上午11:54:23
	 */
	public void onHeaderRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mHeaderImageView.setVisibility(View.VISIBLE);
		mHeaderImageView.setImageResource(R.mipmap.arrow);//下拉图标
		mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
		mHeaderProgressBar.setVisibility(View.GONE);
		// mHeaderUpdateTextView.setText("");
		mHeaderState = PULL_TO_REFRESH;
//		try {//更新时间
//			mLastUpdateTimeMillis = System.currentTimeMillis();
//			SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(
//					Tag, Application.MODE_PRIVATE);
//			sharedPreferences.edit().putLong(LAST_SAVE_TIME, mLastUpdateTimeMillis).commit();
//		} catch (Exception e) {
//		}
	}

	/**
	 * 下拉刷新完成
	 */
	public void onHeaderRefreshComplete(CharSequence lastUpdated) {
		setLastUpdated(lastUpdated);
		onHeaderRefreshComplete();
	}

	/**
	 * footer view 完成更新后恢复初始状态
	 */
	public void onFooterRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mFooterImageView.setVisibility(View.VISIBLE);
		mFooterImageView.setImageResource(R.mipmap.arrow);//加载更多的下拉图标
		mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
		mFooterProgressBar.setVisibility(View.GONE);
		// mHeaderUpdateTextView.setText("");
		mFooterState = PULL_TO_REFRESH;

	}

	/**
	 * 下拉刷新的文字提示
	 */
	public void setLastUpdated(CharSequence lastUpdated) {
		if (lastUpdated != null) {
			mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			mHeaderUpdateTextView.setText(lastUpdated);
		} else {
			mHeaderUpdateTextView.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取当前header view 的topMargin
	 * 
	 * @description
	 * @return lwq 2012-7-31上午11:22:50
	 */
	private int getHeaderTopMargin() {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		return params.topMargin;
	}


	/**
	 * 设置头部下拉的监听（下拉刷新）
	 * 
	 * @description
	 * @param headerRefreshListener
	 *   lwq 2012-7-31上午11:43:58
	 */
	public void setOnHeaderRefreshListener(
			OnHeaderRefreshListener headerRefreshListener) {
		mOnHeaderRefreshListener = headerRefreshListener;
	}
	/**
	 * 设置底部上拉的监听（加载更多）
	 */
	public void setOnFooterRefreshListener(
			OnFooterRefreshListener footerRefreshListener) {
		mOnFooterRefreshListener = footerRefreshListener;
	}

	public interface OnFooterRefreshListener {
		public void onFooterRefresh(PullToRefreshView view);
	}

	public interface OnHeaderRefreshListener {
		public void onHeaderRefresh(PullToRefreshView view);
	}

	public void setNoMore(){//这个设置不是唯一，当下拉刷新的时候这个已经初始化过了为false
		noMore=true;
	}
	public  void setHasMore(){ 
		noMore=false;
	}
	public static void setForHasMore(){ 
		noMore=false;
	}
	boolean isLoadMore =true;//是否存在上拉功能
	public void setIsCancleLoadMore(){//取消下拉功能
		isLoadMore=false;
	}

	 
	/**
	 * 主要更新一下刷新时间啦！
	 * 
	 * @param adapter
	 */

	long _LastSaveTimeMillis;//最近更新的时间
	long mLastUpdateTimeMillis;//最近更新的时间
	long mLastComparedDiffMillis;//最近更新的时间
	//	  _LastSaveTimeMillis = mLastUpdateTimeMillis;
	private static final String LAST_SAVE_TIME = "lastSaveTime";
	/** 更新间隔 ————> 10秒    ————> 转换为毫秒   */
	//	private static final int mUpdateIntervalMillis = 1 * 3 * 1000;
	/**
	 * 主要更新一下刷新时间啦！
	 * 
	 * @param
	 */
	public void updateTextTime() {
		mHeaderUpdateTextView.setText(getResources().getString(
				R.string.p2refresh_refresh_lasttime)
				+new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA)
				.format(new Date()));
//		try {
//			SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences(
//					Tag, Application.MODE_PRIVATE);
//			mLastUpdateTimeMillis = sharedPreferences.getLong(LAST_SAVE_TIME, -1);
//
//			if(null!=mHeaderUpdateTextView){
//				if(mLastUpdateTimeMillis==-1){
//					mHeaderUpdateTextView.setText(getResources().getString(
//							R.string.p2refresh_refresh_lasttime)
//							+new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA)
//					.format(new Date()));
//				}else{
//					mLastComparedDiffMillis = System.currentTimeMillis() - mLastUpdateTimeMillis;
//					_TimeDiffSeconds = mLastComparedDiffMillis / 1000;
//					// 最近更新: Time
//					mHeaderUpdateTextView.setText(getResources().getString(
//							R.string.p2refresh_refresh_lasttime)
//							+ getLastUpdatedText(_TimeDiffSeconds));
//				}
//			}
//
//		} catch (Exception e) {
//		}

	}
	
	long _TimeDiffSeconds;
	/**
	 * 第一次自动更新
	 * */
	public void firstGet(){
		mHandler.sendEmptyMessageDelayed(111, 2000);
	}
	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case 111://首页
				updateTextTime();
				headerRefreshing();
				break;
			}
		}
	};

	//	//定时器/**  不断地去检查是否达到自动更新的条件   */
	//	private	Runnable mCheckRefRunnable=new Runnable(){
	//		@Override
	//		public void run() {
	//		}
	//	};

	private String getLastUpdatedText(final long pTimeDiffSeconds){
		String _Text = "无更新";
		try {
			if(pTimeDiffSeconds < 60){// 小于一分钟
				_Text = pTimeDiffSeconds + "秒前";
			}else if(pTimeDiffSeconds >= (1 * 60) && pTimeDiffSeconds < (60 * 60)){// 大于等于1分钟，小于一小时。
				long _MinNum = pTimeDiffSeconds / 60;
				_Text = _MinNum+"分钟前";
			}else if(pTimeDiffSeconds >= (60 * 60) && pTimeDiffSeconds < (24 * 60 * 60)){// 大于等于一小时
				long _HourNum = pTimeDiffSeconds / (60 * 60);
				_Text = _HourNum+"小时前";
			}else if(pTimeDiffSeconds >= (24 * 60 * 60)){// 大于等于一小时
				long _DayNum = pTimeDiffSeconds / (24 * 60 * 60);
				_Text = _DayNum+"天前";
			}else if(pTimeDiffSeconds >= (24 * 60 * 60*30)){// 大于等于一个月
				_Text = "1个月以前";
			}
		} catch (Exception e) {
		}
		return _Text;
	}
	public  String Tag="tag";
}
