package com.tony.ced.dialog;

import android.view.View;

/**
 * Created by Administrator on 2016/4/26.
 */
public interface IAlertSheetDialog {

    IAlertSheetDialog addItem(String itemStr);

    IAlertSheetDialog show();

    IAlertSheetDialog show(View view);

    IAlertSheetDialog showCenter();


    void dismiss();

    void setView(View view);

    void setView(int resId);

    View getView();

}
