package com.picklemixel.mister.hangingsnackbar;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Masterfully pieced together by the Al-Mighty Paul on 19/09/16.
 */
public class HangingSnackbar {

    public static final long LENGTH_SHORT = 3000;
    public static final long LENGTH_LONG = 5000;
    public static final long LENGTH_INDEFINITE = -1;
    private HangingSnackbarController controller;
    private SnackbarParams snackbarParams;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (snackbarParams.actionCallback != null) {
                snackbarParams.actionCallback.actionClicked(snackbarParams.callbackObject);
            }
        }
    };

    private HangingSnackbar(SnackbarParams params) {
        snackbarParams = params;
        controller = HangingSnackbarController.getInstance();

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
            }
            holder.actionTextView.setOnClickListener(onClickListener);
        }

        if (snackbarParams.backgroundColour != 0) {
            holder.backgroundRelativeLayout.setBackgroundColor(ContextCompat.getColor(snackbarParams.context, snackbarParams.backgroundColour));
        }
    }

    protected void animateIn() {
        snackbarParams.parentView.addView(snackbarParams.snackView);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, -snackbarParams.offset, 0, 0);
        snackbarParams.snackView.setLayoutParams(params);
        snackbarParams.snackView.animate().translationY(snackbarParams.offset);
    }

    protected long getLength() {
        return snackbarParams.length;
    }

    protected void animateOut() {
        snackbarParams.snackView.animate().translationY(0).setListener(controller);
    }

    protected void removeViewViewFromParent() {
        snackbarParams.parentView.removeView(snackbarParams.snackView);
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

    public boolean isInView() {
        return controller.isSnackbarInView(this);
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

        public <T> Builder setActionText(String actionText, IHangingSnackbarCallback actionCallback, T callbackObject) {
            params.actionText = actionText;
            params.actionCallback = actionCallback;
            params.callbackObject = callbackObject;
            return this;
        }

        public <T> Builder setActionText(int actionTextId, IHangingSnackbarCallback actionCallback, T callbackObject) {
            params.actionText = params.context.getString(actionTextId);
            params.actionCallback = actionCallback;
            params.callbackObject = callbackObject;
            return this;
        }

        public <T> Builder setActionText(String actionText, IHangingSnackbarCallback actionCallback, T callbackObject, int colorId) {
            params.actionColour = colorId;
            params.actionText = actionText;
            params.actionCallback = actionCallback;
            params.callbackObject = callbackObject;
            return this;
        }

        public <T> Builder setActionText(int actionTextId, IHangingSnackbarCallback actionCallback, T callbackObject, int colorId) {
            params.actionColour = colorId;
            params.actionText = params.context.getString(actionTextId);
            params.actionCallback = actionCallback;
            params.callbackObject = callbackObject;
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

    protected static class SnackbarParams<T> {
        public Context context;
        public ViewGroup parentView;
        public long length;
        public RelativeLayout snackView;
        public String text;
        public String actionText;
        public int offset;
        public IHangingSnackbarCallback actionCallback;
        public int backgroundColour;
        public int textColour;
        public int actionColour;
        public T callbackObject;
        public Typeface textTypeface;
        public Typeface actionTypeface;

        protected SnackbarParams(Context context, ViewGroup parentView) {
            this.context = context;
            this.parentView = parentView;
        }

    }

    private class ViewHolder {

        public TextView textTextView;
        public TextView actionTextView;
        public RelativeLayout backgroundRelativeLayout;

        public ViewHolder(RelativeLayout snackView) {
            textTextView = (TextView) snackView.findViewById(R.id.text_view);
            actionTextView = (TextView) snackView.findViewById(R.id.action_text_view);
            backgroundRelativeLayout = (RelativeLayout) snackView.findViewById(R.id.background_relative_layout);
        }
    }
}