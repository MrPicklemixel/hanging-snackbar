package com.picklemixel.mister.hangingsnackbar;

/**
 * Contains interfaces for listening to action clicks and dismisses
 * @author Paul Jeffries Jones (Mr.Picklemixel)
 */
public interface IHangingSnackbarCallback {
    /**
     * Used to listen to action clicks
     */
    interface OnActionClickedListener<T> {
        /**
         * Called when the action button is clicked
         * @param obj The object stored in the {@link HangingSnackbar}
         */
        void actionClicked(T obj);
    }

    /**
     * Used to listen for a {@link HangingSnackbar} to be dismissed
     */
    interface OnDismissedListener {
        /**
         * Called when the {@link HangingSnackbar} has been dismissed
         */
        void onDismissed();
    }
}
