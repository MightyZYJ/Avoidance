package com.juno.avoidance.utils;

import android.content.Context;
import android.support.annotation.IntRange;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_FAIL;
import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_INFO;
import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_LOADING;
import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_NOTHING;
import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_SUCCESS;

/**
 * Created by Juno.
 * Date : 2019/4/16.
 * Time : 21:44.
 * When I met you in the summer.
 * Description : 依赖QMUI
 * <a href="https://qmuiteam.com/android/documents"></a>
 */
public class QMUIUtil {

    /**
     * Created by Juno at 21:46, 2019/4/16.
     * load description : 产生一个QMUITipDialog，包含加载、失败、成功等状态
     */
    public static class TipDialog {

        public static QMUITipDialog load(Context context, String tip) {
            return dialog(context, tip, ICON_TYPE_LOADING, false);
        }

        public static QMUITipDialog fail(Context context, String tip) {
            return dialog(context, tip, ICON_TYPE_FAIL, true);
        }

        public static QMUITipDialog success(Context context, String tip) {
            return dialog(context, tip, ICON_TYPE_SUCCESS, true);
        }

        public static QMUITipDialog info(Context context, String tip) {
            return dialog(context, tip, ICON_TYPE_INFO, true);
        }

        private static QMUITipDialog dialog(Context context, String tip, @IntRange(from = ICON_TYPE_NOTHING, to = ICON_TYPE_INFO) int type, boolean cancelable) {
            QMUITipDialog dialog = new QMUITipDialog.Builder(context)
                    .setIconType(type)
                    .setTipWord(tip)
                    .create();
            dialog.setCanceledOnTouchOutside(cancelable);
            dialog.setCancelable(cancelable);
            return dialog;
        }
    }


}
