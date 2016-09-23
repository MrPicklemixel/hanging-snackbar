package com.picklemixel.mister.hangingsnackbar;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static com.picklemixel.mister.hangingsnackbar.IHangingSnackbarCallback.*;

/**
 * A (mostly) material Snackbar equivalent that drops instead of rises.
 * @author Paul Jeffries Jones (Mr.Picklemixel)
 */
public class HangingSnackbar {

    /**
     * Debug tag used for logging exceptions
     */
    private static final String TAG = "hanging-snackbar";
    /**
     * Show the snackbar for 3 seconds
     */
    public static final long LENGTH_SHORT = 3000;
    /**
     * Show the snackbar for 5 seconds
     */
    public static final long LENGTH_LONG = 5000;
    /**
     * Show the snackbar until dismissed
     */
    public static final long LENGTH_INDEFINITE = -1;
    /**
     * Controller used to manage the snackbar queue and display snackbars
     */
    private HangingSnackbarController controller;
    /**
     * Contains all of the snackbars parameters, e.g text and colours
     */
    private SnackbarParams snackbarParams;
    /**
     * Holds true when the action has been clicked once to prevent multiple clicks
     */
    private boolean actionClicked;

    /**
     * OnClickListener used to listen for action clicks
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (snackbarParams.actionCallback != null && !actionClicked) {
                actionClicked = true;
                snackbarParams.actionCallback.actionClicked(snackbarParams.callbackObject);
            }
        }
    };

    /**
     * Constructs the snackbar using any parameters provided by the {@link Builder}
     * @param params The snackbar attributes collected by the {@link Builder}
     */
    private HangingSnackbar(SnackbarParams params) {
        snackbarParams = params;
        controller = HangingSnackbarController.getInstance();
        snackbarParams.id = controller.createId();

        LayoutInflater inflater = LayoutInflater.from(snackbarParams.context);

        if (params.actionText != null) {
            if (params.text.length() > 36) {
                snackbarParams.snackView = (RelativeLayout) inflater.inflate(R.layout.snackbar_double_action, null);
                snackbarParams.offset = (int) snackbarParams.context.getResources().getDimension(R.dimen.snackbar_double_height);
            } else {
                snackbarParams.snackView = (RelativeLayout) inflater.inflate(R.layout.snackbar_single_action, null);
                snackbarParams.offset = (int) snackbarParams.context.getResources().getDimension(R.dimen.snackbar_single_height);
            }
        } else {
            if (params.text.length() > 46) {
                snackbarParams.snackView = (RelativeLayout) inflater.inflate(R.layout.snackbar_double, null);
                snackbarParams.offset = (int) snackbarParams.context.getResources().getDimension(R.dimen.snackbar_double_height);
            } else {
                snackbarParams.snackView = (RelativeLayout) inflater.inflate(R.layout.snackbar_single, null);
                snackbarParams.offset = (int) snackbarParams.context.getResources().getDimension(R.dimen.snackbar_single_height);
            }
        }

        ViewHolder holder = new ViewHolder(snackbarParams.snackView);

        if (snackbarParams.text != null) {
            holder.textTextView.setText(snackbarParams.text);
            if (snackbarParams.textColor != 0) {
                holder.textTextView.setTextColor(ContextCompat.getColor(snackbarParams.context, snackbarParams.textColor));
            }
            if (snackbarParams.textTypeface != null) {
                holder.textTextView.setTypeface(snackbarParams.textTypeface);
            }
        }

        if (snackbarParams.actionText != null) {
            holder.actionTextView.setText(snackbarParams.actionText);
            if (snackbarParams.actionColor != 0) {
                holder.actionTextView.setTextColor(ContextCompat.getColor(snackbarParams.context, snackbarParams.actionColor));
            }
            if (snackbarParams.actionTypeface != null) {
                holder.actionTextView.setTypeface(snackbarParams.actionTypeface);
            } else {
                holder.actionTextView.setTypeface(Typeface.DEFAULT_BOLD);
            }
            holder.actionTextView.setOnClickListener(onClickListener);
        }

        if (snackbarParams.backgroundColor != 0) {
            holder.backgroundRelativeLayout.setBackgroundColor(ContextCompat.getColor(snackbarParams.context, snackbarParams.backgroundColor));
        }
    }

    /**
     * Animates the snackbar onto the screen inside of the parent view or logs an error if a {@link ClassCastException} is caught
     */
    void animateIn() {
        try {
            snackbarParams.parentView.addView(snackbarParams.snackView);
            setMarginParams();
            snackbarParams.snackView.animate().translationY(snackbarParams.offset);
        } catch (ClassCastException e) {
            Log.e(TAG, "Incompatible parent layout " + snackbarParams.parentView.getClass().getSimpleName() +
                    ", please use RelativeLayout, FrameLayout, or CoordinatorLayout");
        }
    }

    /**
     * Sets the margins of the snackbar view so that it appears off screen
     * @throws ClassCastException When parent view is not of type {@link RelativeLayout}, {@link FrameLayout}, or {@link CoordinatorLayout}
     */
    private void setMarginParams() throws ClassCastException {
        switch (snackbarParams.parentView.getClass().getSimpleName()) {
            case "RelativeLayout":
                RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                relativeParams.setMargins(0, -snackbarParams.offset, 0, 0);
                snackbarParams.snackView.setLayoutParams(relativeParams);
                break;
            case "FrameLayout":
                FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                frameParams.setMargins(0, -snackbarParams.offset, 0, 0);
                snackbarParams.snackView.setLayoutParams(frameParams);
                break;
            case "CoordinatorLayout":
                CoordinatorLayout.LayoutParams coordParams = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
                coordParams.setMargins(0, -snackbarParams.offset, 0, 0);
                snackbarParams.snackView.setLayoutParams(coordParams);
                break;
            default:
                throw new ClassCastException();
        }
    }

    /**
     * Returns the time in milliseconds that the snackbar is to be shown
     * @return The time in milliseconds that the snackbar is to be shown
     */
    long getLength() {
        return snackbarParams.length;
    }

    /**
     * Animates the snackbar off of the screen
     */
    void animateOut() {
        snackbarParams.snackView.animate().translationY(0).setListener(controller);
    }

    /**
     * Removes the snackbar view from the parent
     */
    void removeSnackViewFromParent() {
        snackbarParams.parentView.removeView(snackbarParams.snackView);
        actionClicked = false;
        if (snackbarParams.dismissedCallback != null) {
            snackbarParams.dismissedCallback.onDismissed();
        }
    }

    /**
     * Tells the {@link HangingSnackbarController} to add the snackbar to the queue and display it
      */
    public void show() {
        controller.show(this);
    }

    /**
     * Removes the snackbar from view if it is visible
     */
    public void dismiss() {
        if (isInView()) {
            controller.hide();
        }
    }

    /**
     * Checks if the snackbar is waiting to be displayed
     * @return True if the snackbar is in the queue
     */
    public boolean isInQueue() {
        return controller.isSnackbarInQueue(snackbarParams.id);
    }

    /**
     * Checks if the snackbar is currently being displayed
     * @return True if the snackbar is on screen
     */
    public boolean isInView() {
        return controller.isSnackbarInView(snackbarParams.id);
    }

    /**
     * Returns the generated ID of the snackbar
     * @return The generated ID of the snackbar
     */
    public int getId() {
        return snackbarParams.id;
    }

    /**
     * Sets the {@link SnackbarParams} the required {@link HangingSnackbar} and returns the constructed {@link HangingSnackbar}
     */
    public static class Builder {

        /**
         * The desired attributes of the {@link HangingSnackbar}
         */
        private SnackbarParams params;

        /**
         * Initialises the Builder
         * @param context Used to maintain context within the {@link HangingSnackbar}
         * @param parentView The view the snackbar should appear inside
         * @param length The length in milliseconds the {@link HangingSnackbar} should appear on screen
         */
        public Builder(Context context, ViewGroup parentView, long length) {
            params = new SnackbarParams(context, parentView);
            params.length = length;
        }

        /**
         * Sets the text using a String value to be displayed on the {@link HangingSnackbar}
         * @param text The text to be displayed on the {@link HangingSnackbar}
         * @return The builder to add more attributes or build the snackbar
         */
        public Builder setText(String text) {
            params.text = text;
            return this;
        }

        /**
         * Sets the text using a resource ID to be displayed on the {@link HangingSnackbar}
         * @param textId The resource ID of the text to be displayed on the {@link HangingSnackbar}
         * @return The builder to add more attributes or build the snackbar
         */
        public Builder setText(int textId) {
            params.text = params.context.getString(textId);
            return this;
        }

        /**
         * Sets the text using a String value, and the color of the text to be displayed on the {@link HangingSnackbar}
         * @param text The text to be displayed on the {@link HangingSnackbar}
         * @param colorId The resource ID of the desired text color
         * @return The builder to add more attributes or build the snackbar
         */
        public Builder setText(String text, int colorId) {
            params.textColor = colorId;
            params.text = text;
            return this;
        }

        /**
         * Sets the text using a resource ID, and the color of the text to be displayed on the {@link HangingSnackbar}
         * @param textId The text to be displayed on the {@link HangingSnackbar}
         * @param colorId The resource ID of the desired text color
         * @return The builder to add more attributes or build the snackbar
         */
        public Builder setText(int textId, int colorId) {
            params.textColor = colorId;
            params.text = params.context.getString(textId);
            return this;
        }

        /**
         * Sets the desired typface of the {@link HangingSnackbar} text
         * @param typeface The typeface the text of the {@link HangingSnackbar} should use
         * @return The builder to add more attributes or build the snackbar
         */
        public Builder setTextTypeface(Typeface typeface) {
            params.textTypeface = typeface;
            return this;
        }

        /**
         * Sets the action of the {@link HangingSnackbar}
         * @param actionText The desired text of the action
         * @param actionCallback The {@link IHangingSnackbarCallback.OnActionClickedListener} to be called when the action is clicked. This can be null
         * @param callbackObject The object that should be returned when the {@link IHangingSnackbarCallback.OnActionClickedListener} is activated
         * @return The builder to add more attributes or build the snackbar
         */
        public <T> Builder setAction(String actionText, OnActionClickedListener actionCallback, T callbackObject) {
            params.actionText = actionText;
            params.actionCallback = actionCallback;
            params.callbackObject = callbackObject;
            return this;
        }

        /**
         * Sets the action of the {@link HangingSnackbar}
         * @param actionTextId The resource ID of the desired text of the action
         * @param actionCallback The {@link IHangingSnackbarCallback.OnActionClickedListener} to be called when the action is clicked. This can be null
         * @param callbackObject The object that should be returned when the {@link IHangingSnackbarCallback.OnActionClickedListener} is activated
         * @return The builder to add more attributes or build the snackbar
         */
        public <T> Builder setAction(int actionTextId, OnActionClickedListener actionCallback, T callbackObject) {
            params.actionText = params.context.getString(actionTextId);
            params.actionCallback = actionCallback;
            params.callbackObject = callbackObject;
            return this;
        }

        /**
         * Sets the action of the {@link HangingSnackbar}
         * @param actionText The desired text of the action
         * @param actionCallback The {@link IHangingSnackbarCallback.OnActionClickedListener} to be called when the action is clicked. This can be null
         * @param callbackObject The object that should be returned when the {@link IHangingSnackbarCallback.OnActionClickedListener} is activated
         * @param colorId The resource ID of the desired action text color
         * @return The builder to add more attributes or build the snackbar
         */
        public <T> Builder setAction(String actionText, OnActionClickedListener actionCallback, T callbackObject, int colorId) {
            params.actionColor = colorId;
            params.actionText = actionText;
            params.actionCallback = actionCallback;
            params.callbackObject = callbackObject;
            return this;
        }

        /**
         * Sets the action of the {@link HangingSnackbar}
         * @param actionTextId The resource ID of the desired text of the action
         * @param actionCallback The {@link IHangingSnackbarCallback.OnActionClickedListener} to be called when the action is clicked. This can be null
         * @param callbackObject The object that should be returned when the {@link IHangingSnackbarCallback.OnActionClickedListener} is activated
         * @param colorId The resource ID of the desired action text color
         * @return The builder to add more attributes or build the snackbar
         */
        public <T> Builder setAction(int actionTextId, OnActionClickedListener actionCallback, T callbackObject, int colorId) {
            params.actionColor = colorId;
            params.actionText = params.context.getString(actionTextId);
            params.actionCallback = actionCallback;
            params.callbackObject = callbackObject;
            return this;
        }

        /**
         * Sets the desired typface of the {@link HangingSnackbar} action text
         * @param typeface The typeface the action text of the {@link HangingSnackbar} should use
         * @return The builder to add more attributes or build the snackbar
         */
        public Builder setActionTypeface(Typeface typeface) {
            params.actionTypeface = typeface;
            return this;
        }

        /**
         * Sets the listener to be called when the {@link HangingSnackbar} is dismissed
         * @param dismissedCallback The {@link IHangingSnackbarCallback.OnDismissedListener} to be called when the snackbar is dismissed
         * @return The builder to add more attributes or build the snackbar
         */
        public Builder setOnDismissedListener(OnDismissedListener dismissedCallback) {
            params.dismissedCallback = dismissedCallback;
            return this;
        }

        /**
         * Sets the background color of the {@link HangingSnackbar}
         * @param colorId The resource ID of the desired color of the background
         * @return The builder to add more attributes or build the snackbar
         */
        public Builder setBackgroundColor(int colorId) {
            params.backgroundColor = colorId;
            return this;
        }

        /**
         * Creates and returns a new {@link HangingSnackbar} object using the collected {@link SnackbarParams}
         * @return The created {@link HangingSnackbar} object
         */
        public HangingSnackbar build() {
            return new HangingSnackbar(params);
        }

    }

    /**
     * Holds the attributes to be used by the {@link HangingSnackbar}
     */
    private static class SnackbarParams<T1 extends ViewGroup, T2> {
        /**
         * The generated ID of the snackbar
         */
        int id;
        /**
         * The context to be used to obtain resources and build the {@link HangingSnackbar}
         */
        Context context;
        /**
         * The view the {@link HangingSnackbar} will appear in. Should be of type {@link RelativeLayout}, {@link FrameLayout}, or {@link CoordinatorLayout}
         */
        T1 parentView;
        /**
         * The length of time the {@link HangingSnackbar} will appear on screen
         */
        long length;
        /**
         * The view of the {@link HangingSnackbar} itself that is added to the parent
         */
        RelativeLayout snackView;
        /**
         * The text to be displayed on the {@link HangingSnackbar}
         */
        String text;
        /**
         * The action text to be displayed on the {@link HangingSnackbar}
         */
        String actionText;
        /**
         * The calculated height of the {@link HangingSnackbar} used to offset the snackbar position off screen
         */
        int offset;
        /**
         * The listener used when the {@link HangingSnackbar} action has been clicked
         */
        OnActionClickedListener actionCallback;
        /**
         * The listener used when the {@link HangingSnackbar} has been dismissed
         */
        OnDismissedListener dismissedCallback;
        /**
         * The resource ID of the desired background color of {@link HangingSnackbar}
         */
        int backgroundColor;
        /**
         * The resource ID of the desired text color of {@link HangingSnackbar}
         */
        int textColor;
        /**
         * The resource ID of the desired action text color of {@link HangingSnackbar}
         */
        int actionColor;
        /**
         * The object returned when the {@link OnActionClickedListener} is activated
         */
        T2 callbackObject;
        /**
         * The desired typeface of the {@link HangingSnackbar} text
         */
        Typeface textTypeface;
        /**
         * The desired typeface of the {@link HangingSnackbar} action text
         */
        Typeface actionTypeface;

        /**
         * Initialises the object
         * @param context Used to maintain context within the {@link HangingSnackbar}
         * @param parentView The view the snackbar should appear inside
         */
        SnackbarParams(Context context, T1 parentView) {
            this.context = context;
            this.parentView = parentView;
        }

    }

    /**
     * Allows easier manipulation of the {@link HangingSnackbar}'s view object
     */
    private class ViewHolder {

        /**
         * The {@link TextView} used to display the {@link HangingSnackbar} text
         */
        TextView textTextView;
        /**
         * The {@link TextView} used to display the {@link HangingSnackbar} action text
         */
        TextView actionTextView;
        /**
         * The {@link RelativeLayout} used to change the background color of the {@link HangingSnackbar}
         */
        RelativeLayout backgroundRelativeLayout;

        /**
         * Finds the inner views of the {@link HangingSnackbar} view and stores them in their respective members
         * @param snackView The {@link RelativeLayout} of the {@link HangingSnackbar} to be manipulated
         */
        ViewHolder(RelativeLayout snackView) {
            textTextView = (TextView) snackView.findViewById(R.id.text_view);
            actionTextView = (TextView) snackView.findViewById(R.id.action_text_view);
            backgroundRelativeLayout = (RelativeLayout) snackView.findViewById(R.id.background_relative_layout);
        }
    }
}