package cz.johnyapps.eddiehostopky.tools;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboardUtils {
    private static final String TAG = "SoftKeyboardUtils";

    public static void hideKeyboard(View... focusedViews) {
        for (View focusedView : focusedViews) {
            if (focusedView.isFocused()) {
                InputMethodManager inputMethodManager = (InputMethodManager) focusedView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                } else {
                    Logger.e(TAG, "hideKeyboard: InputMethodManager is null");
                }
            } else {
                Logger.w(TAG, "hideKeyboard: view is not focused");
            }
        }
    }

    public static void showKeyboard(View view) {
        view.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
