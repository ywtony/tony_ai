package com.tony.ced.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AlertDialog;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tony.ced.R;
import com.tony.ced.utils.CommonUtils;
import com.tony.ced.utils.ContextUtils;
import com.tony.ced.utils.DisplayUtil;
import com.tony.ced.utils.PixelMethod;
import com.tony.ced.weight.RoundedLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by DYJ on 2016/4/26.
 */
public class AlertSheetDialog implements IAlertSheetDialog {
	private Context mContext;
	private boolean isFirstTitle;
	private boolean isShowDivideLine; //显示item之间8dp的分割线
	private AlertDialog mAlertDialog;
	private View customView;
	private LinearLayout contentView, itemViews;
	private AlertSheetClickListener alertSheetClickListener;
	private List<String> items = new ArrayList<>();
	private List<String> colors;
	private boolean isFouce = true, isNeed = true, isTouch;//popupwindow 焦点及弹出退出动画,点击是否可以取消
	private boolean change = false;
	private String Text, TextColor, BgColor;
	private float width = 0;
	private float height = 0;
	private int cancelTextColor = R.color.FF007AED;

	/**
	 * 1.双检锁/双重校验锁
	 */
	private static AlertSheetDialog mInstance;

	public static AlertSheetDialog getInstance(Context mContext) {
		if (mInstance == null) {
			synchronized (AlertSheetDialog.class) {
				if (mInstance == null) {
					mInstance = new AlertSheetDialog(mContext);
				}
			}
		}
		return mInstance;
	}

	public AlertSheetDialog(List<String> items) {
		this.items.addAll(items);
		init();
	}

	private AlertSheetDialog(Context mContext) {
		this.mContext = mContext;
		init();
	}

	public AlertSheetDialog(Context mContext, List<String> items) {
		this.mContext = mContext;
		this.items.clear();
		this.items.addAll(items);
		init();
	}

	public AlertSheetDialog(Context mContext, List<String> items, boolean isFirstTitle) {
		this(mContext, items);
		this.isFirstTitle = isFirstTitle;
	}

	public AlertSheetDialog(Context context, View view, boolean isFouce, boolean isNeed, boolean isTouch) {
		this.mContext = context;
		this.isFouce = isFouce;
		this.isNeed = isNeed;
		this.isTouch = isTouch;
		init();
		setView(view);
	}

	public AlertSheetDialog(Context mContext, View view) {
		this.mContext = mContext;
		this.customView = view;
		init();
	}

	public AlertSheetDialog(Context mContext, String[] items) {
		this.mContext = mContext;
		this.items = Arrays.asList(items);
		init();
	}

	public AlertSheetDialog(Context mContext, String[] items, int cancelTextColor) {
		this.mContext = mContext;
		this.items = Arrays.asList(items);
		this.cancelTextColor = cancelTextColor;
		init();
	}

	public AlertSheetDialog(Context mContext, String[] items, boolean isFirstTitle) {
		this.mContext = mContext;
		this.isFirstTitle = isFirstTitle;
		this.items = Arrays.asList(items);
		init();
	}

	public AlertSheetDialog(Context mContext, String[] items, String[] colors) {
		this.mContext = mContext;
		this.items = Arrays.asList(items);
		this.colors = Arrays.asList(colors);
		init();
	}

	public AlertSheetDialog(Context mContext, String[] items, String[] colors, int cancelTextColor, boolean isShowDivideLine) {
		this.mContext = mContext;
		this.items = Arrays.asList(items);
		this.colors = Arrays.asList(colors);
		this.cancelTextColor = cancelTextColor;
		this.isShowDivideLine = isShowDivideLine;
		init();
	}

	public AlertSheetDialog setItems(List<String> items) {
		this.items = items;
		return this;
	}

	public void setChange(boolean b) {
		change = b;
	}

	public void setText(String text) {
		Text = text;
	}

	public void setTextColor(String textColor) {
		TextColor = textColor;
	}

	public void setBackGroundColor(String Color) {
		BgColor = Color;
	}

	private void init() {
		if (!isShowDivideLine) {
			contentView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.alert_sheet_dialog, null);
		} else {
			contentView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.alert_sheet_dialog_no_divider, null);
		}
		contentView.setOnKeyListener(onKeyListener);
		itemViews = contentView.findViewById(R.id.items);
		LinearLayout.LayoutParams contentViewParams;
		if (width > 0 && height > 0) {
			contentViewParams = new LinearLayout.LayoutParams(PixelMethod.dip2px(mContext, width),
					PixelMethod.dip2px(mContext, height));
		} else {
			contentViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
					.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			contentViewParams.setMargins(AutoUtils.getPercentHeightSize(0), AutoUtils.getPercentHeightSize(0),
					AutoUtils.getPercentHeightSize(0), AutoUtils.getPercentHeightSize(0));
		}
		View cancel = LayoutInflater.from(mContext).inflate(R.layout.item_alert_list_sheet, null);
		DisplayUtil.setTextColor((TextView) cancel.findViewById(R.id.tv_alert_sheet_title), cancelTextColor);
		if (!isShowDivideLine) {
			RoundedLayout roundedLayout = new RoundedLayout(mContext);
			roundedLayout.setRoundLayoutRadius(ContextUtils.getContext().getResources()
					.getDimensionPixelOffset(R.dimen.dimens_8));
			roundedLayout.setLayoutParams(contentViewParams);
			roundedLayout.addView(cancel);
			contentView.addView(roundedLayout);
		} else {
			cancel.findViewById(R.id.line).setVisibility(View.VISIBLE);
			contentView.addView(cancel);
		}

		mAlertDialog = new AlertDialog.Builder(mContext, R.style.dialog).create();
		mAlertDialog.setCanceledOnTouchOutside(true);
		mAlertDialog.setCancelable(true);

		mAlertDialog.setOnDismissListener(dialog -> {
			if (alertSheetClickListener != null) {
				alertSheetClickListener.onSheetDialogDismiss();
			}
		});

		if (change) {

		} else {
			contentView.setOnClickListener(view -> mAlertDialog.dismiss());
		}
	}

	private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (mAlertDialog != null) {
					mAlertDialog.dismiss();
				}
				return true;
			}
			return false;
		}
	};

	@Override
	public AlertSheetDialog addItem(String itemStr) {
		return this;
	}

	@Override
	public synchronized AlertSheetDialog show() {
		if (CommonUtils.isShowSoftInput(mContext)) {
			CommonUtils.hideSoftInput(mContext, ((Activity) mContext).getWindow().getDecorView());
		}
		//boolean isDestroyed=((Activity) mContext).isFinishing();
		//if (!isDestroyed)
		{
			if (mAlertDialog != null && !mAlertDialog.isShowing()) {
				mAlertDialog.show();
				Window window = mAlertDialog.getWindow();
				if (window != null) {
					window.setGravity(Gravity.BOTTOM);
					window.setWindowAnimations(R.style.AnimBottom);
					if (customView != null) {
						window.setContentView(customView);
					} else {
						initData();
						window.setContentView(contentView);
					}
					window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				}
			}
		}
		return this;
	}


	public AlertSheetDialog showDialog(Context context) {
		mContext = context;
//        init();
		show();
		return this;
	}

	private void initData() {
		itemViews.removeAllViews();
		if (items != null && items.size() > 0) {
			for (int i = 0; i < items.size(); i++) {
				View view = LayoutInflater.from(mContext).inflate(R.layout.item_alert_list_sheet, null);
				final int finalI = i;
				view.setOnClickListener(view1 -> {
					if (finalI == 0 && isFirstTitle) return;
					if (alertSheetClickListener != null) {
						alertSheetClickListener.onSheetItemClickListener(view1, finalI);
					}
					if (null != mAlertDialog && mAlertDialog.isShowing()) {
						mAlertDialog.dismiss();
					}
				});
				if (isShowDivideLine) {
					if (i == 0) {
						view.findViewById(R.id.line).setVisibility(View.GONE);
					} else {
						view.findViewById(R.id.line).setVisibility(View.VISIBLE);
					}
				}
				TextView itemView = (TextView) view.findViewById(R.id.tv_alert_sheet_title);
				if (colors != null && colors.size() > 0 && colors.size() == items.size()) {
					itemView.setTextColor(Color.parseColor(colors.get(i)));
				}
				itemView.setText(items.get(i));
				itemViews.addView(view);
			}
		}
	}

	@Override
	public AlertSheetDialog showCenter() {
		if (CommonUtils.isShowSoftInput(mContext)) {
			CommonUtils.hideSoftInput(mContext, ((Activity) mContext).getWindow().getDecorView());
		}
		if (mAlertDialog != null && !mAlertDialog.isShowing()) {
			mAlertDialog.show();
			Window window = mAlertDialog.getWindow();
			if (window != null) {
				window.setGravity(Gravity.CENTER);
				WindowManager.LayoutParams attributes = window.getAttributes();
				attributes.gravity = Gravity.CENTER;
				attributes.dimAmount = 0.28f;
				window.setAttributes(attributes);
				if (customView != null) {
					window.setContentView(customView);
				} else {
					initData();
					window.setContentView(contentView);
				}
				window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			}
		}
		return this;
	}

	public AlertSheetDialog showGravity(int gravity) {
		if (CommonUtils.isShowSoftInput(mContext)) {
			CommonUtils.hideSoftInput(mContext, ((Activity) mContext).getWindow().getDecorView());
		}
		if (mAlertDialog != null && !mAlertDialog.isShowing()) {
			mAlertDialog.show();
			Window window = mAlertDialog.getWindow();
			if (window != null) {
				window.setGravity(gravity);
				WindowManager.LayoutParams attributes = window.getAttributes();
				attributes.gravity = gravity;
				attributes.dimAmount = 0.28f;
				window.setAttributes(attributes);
				if (customView != null) {
					window.setContentView(customView);
				} else {
					initData();
					window.setContentView(contentView);
				}
				if (width > 0 && height > 0) {
					window.setLayout(PixelMethod.dip2px(mContext, width), PixelMethod.dip2px(mContext, height));
				} else {
					window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				}
			}
		}
		return this;
	}

	@Override
	public AlertSheetDialog show(View view) {
		if (mAlertDialog != null && !mAlertDialog.isShowing()) {
			mAlertDialog.setView(view);
			mAlertDialog.show();
		}
		return this;
	}

	@Override
	public void dismiss() {
		if (mAlertDialog != null && mAlertDialog.isShowing()) {
			mAlertDialog.dismiss();
			mAlertDialog = null;
			contentView = null;
			alertSheetClickListener = null;
		}
	}

	/**
	 * 当前Dialog是否显示
	 *
	 * @return
	 */
	public boolean isShowing() {
		if (mAlertDialog == null) {
			return false;
		}
		return mAlertDialog.isShowing();
	}

	@Override
	public void setView(View view) {
		this.customView = view;
	}

	public void setView(View view, @Dimension(unit = Dimension.DP) float width, @Dimension(unit = Dimension.DP) float height) {
		this.height = height;
		this.width = width;
		this.customView = view;
	}

	@Override
	public View getView() {
		if (contentView == null) {
			throw new IllegalArgumentException("你需要先调用setView()方法，或者使用其他构造方法");
		}
		return contentView;
	}

	public void hide() {
		if (mAlertDialog != null && mAlertDialog.isShowing()) {
			mAlertDialog.dismiss();
		}
	}

	@Override
	public void setView(int resId) {
		setView(LayoutInflater.from(mContext).inflate(resId, null));
	}

	public AlertSheetDialog setAlertSheetClickListener(AlertSheetClickListener alertSheetClickListener) {
		this.alertSheetClickListener = alertSheetClickListener;
		return this;
	}

}
