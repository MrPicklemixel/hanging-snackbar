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
 * Masterfully pieced together by the Al-Mighty Paul on 19/09/16.
 */
public class HangingSnackbar {

    private static final String TAG = "hanging-snackbar";
    public static final long LENGTH_SHORT = 3000;
    public static final long LENGTH_LONG = 5000;
    public static final long LENGTH_INDEFINITE = -1;
    private HangingSnackbarController controller;
    private SnackbarParams snackbarParams;
    private boolean actionClicked;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (snackbarParams.actionCallback != null && !actionClicked) {
                actionClicked = true;
                snackbarParams.actionCallback.actionClicked(snackbarParams.callbackObject);
            }
        }
    };

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
            if (snackbarParams.textColour != 0) {
                holder.textTextView.setTextColor(ContextCompat.getColor(snackbarParams.context, snackbarParams.textColour));
            }
            if (snackbarParams.textTypeface != null) {
                holder.textTextView.setTypeface(snackbarParams.textTypeface);
            }
        }

        if (snackbarParams.actionText != null) {
            holder.actionTextView.setText(snackbarParams.actionText);
            if (snackbarParams.actionColour != 0) {
                holder.actionTextView.setTextColor(ContextCompat.getColor(snackbarParams.context, snackbarParams.actionColour));
            }
            if (snackbarParams.actionTypeface != null) {
                holder.actionTextView.setTypeface(snackbarParams.actionTypeface);
            } else {
                holder.actionTextView.setTypeface(Typeface.DEFAULT_BOLD);
            }
            holder.actionTextView.setOnClickListener(onClickListener);
        }

        if (snackbarParams.backgroundColour != 0) {
            holder.backgroundRelativeLayout.setBackgroundColor(ContextCompat.getColor(snackbarParams.context, snackbarParams.backgroundColour));
        }
    }

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

    long getLength() {
        return snackbarParams.length;
    }

    void animateOut() {
        snackbarParams.snackView.animate().translationY(0).setListener(controller);
    }

    void removeViewViewFromParent() {
        snackbarParams.parentView.removeView(snackbarParams.snackView);
        actionClicked = false;
        if (snackbarParams.dismissedCallback != null) {
            snackbarParams.dismissedCallback.onDismissed();
        }
    }

    public HangingSnackbar show() {
        controller.show(this);
        return this;
    }

    public void dismiss() {
        if (isInView()) {
            controller.hide();
        }
    }

    public boolean isInQueue() {
        return controller.isSnackbarInQueue(snackbarParams.id);
    }

    public boolean isInView() {
        return controller.isSnackbarInView(snackbarParams.id);
    }

    public int getId() {
        return snackbarParams.id;
    }

    public static class Builder {

        private SnackbarParams params;

        public Builder(Context context, ViewGroup parentView, long length) {
            params = new SnackbarParams(context, parentView);
            params.length = length;
        }

        public Builder setText(String text) {
            params.text = text;
            return this;
        }

        public Builder setText(int textId) {
            params.text = params.context.getString(textId);
            return this;
        }

        public Builder setText(String text, int colorId) {
            params.textColour = colorId;
            params.text = text;
            return this;
        }

        public Builder setText(int textId, int colorId) {
            params.textColour = colorId;
            params.text = params.context.getString(textId);
            return this;
        }

        public Builder setTextTypeface(Typeface typeface) {
            params.textTypeface = typeface;
            return this;
        }

        public <T> Builder setActionText(String actionText, OnActionClickedListener actionCallback, T callbackObject) {
            params.actionText = actionText;
            params.actionCallback = actionCallback;
            params.callbackObject = callbackObject;
            return this;
        }

        public <T> Builder setActionText(int actionTextId, OnActionClickedListener actionCallback, T callbackObject) {
            params.actionText = params.context.getString(actionTextId);
            params.actionCallback = actionCallback;
            params.callbackObject = callbackObject;
            return this;
        }

        public <T> Builder setActionText(String actionText, OnActionClickedListener actionCallback, T callbackObject, int colorId) {
            params.actionColour = colorId;
            params.actionText = actionText;
            params.actionCallback = actionCallback;
            params.callbackObject = callbackObject;
            return this;
        }

        public <T> Builder setActionText(int actionTextId, OnActionClickedListener actionCallback, T callbackObject, int colorId) {
            params.actionColour = colorId;
            params.actionText = params.context.getString(actionTextId);
            params.actionCallback = actionCallback;
            params.callbackObject = callbackObject;
            return this;
        }

        public Builder setOnDismissedListener(OnDismissedListener dismissedCallback) {
            params.dismissedCallback = dismissedCallback;
            return this;
        }

        public Builder setActionTypeface(Typeface typeface) {
            params.actionTypeface = typeface;
            return this;
        }

        public Builder setBackgroundColor(int color) {
            params.backgroundColour = color;
            return this;
        }

        public HangingSnackbar build() {
            HangingSnackbar snackbar = new HangingSnackbar(params);
            return snackbar;
        }

    }

    private static class SnackbarParams<T1 extends ViewGroup, T2> {
        int id;
        Context context;
        T1 parentView;
        long length;
        RelativeLayout snackView;
        String text;
        String actionText;
        int offset;
        OnActionClickedListener actionCallback;
        OnDismissedListener dismissedCallback;
        int backgroundColour;
        int textColour;
        int actionColour;
        T2 callbackObject;
        Typeface textTypeface;
        Typeface actionTypeface;

        SnackbarParams(Context context, T1 parentView) {
            this.context = context;
            this.parentView = parentView;
        }

    }

    private class ViewHolder {

        TextView textTextView;
        TextView actionTextView;
        RelativeLayout backgroundRelativeLayout;

        ViewHolder(RelativeLayout snackView) {
            textTextView = (TextView) snackView.findViewById(R.id.text_view);
            actionTextView = (TextView) snackView.findViewById(R.id.action_text_view);
            backgroundRelativeLayout = (RelativeLayout) snackView.findViewById(R.id.background_relative_layout);
        }
    }
}