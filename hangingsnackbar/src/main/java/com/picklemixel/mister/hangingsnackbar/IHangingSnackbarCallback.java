package com.picklemixel.mister.hangingsnackbar;

/**
 * Masterfully pieced together by the Al-Mighty Paul on 19/09/16.
 */
public interface IHangingSnackbarCallback {
    interface OnActionClickedListener<T> {
        void actionClicked(T obj);
    }
    interface OnDismissedListener {
        void onDismissed();
    }
}
