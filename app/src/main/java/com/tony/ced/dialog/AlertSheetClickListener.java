package com.tony.ced.dialog;

import android.view.View;

/**
 * Created by Administrator on 2016/4/26.
 */
public interface AlertSheetClickListener {

    AlertSheetDialog onSheetItemClickListener(View view,int position);

    void onSheetDialogDismiss();

}