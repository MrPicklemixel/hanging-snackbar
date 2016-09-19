package com.picklemixel.mister.hangingsnackbar;

import android.animation.Animator;
import android.os.CountDownTimer;

import java.util.ArrayList;

/**
 * Masterfully pieced together by the Al-Mighty Paul on 19/09/16.
 */
public class HangingSnackbarController implements Animator.AnimatorListener {

    private static HangingSnackbarController instance;
    private static ArrayList<HangingSnackbar> queuedSnackbars;
    private static boolean snackbarInView;

    private HangingSnackbarController () {}

    public static HangingSnackbarController getInstance() {
        if (instance == null) {
            instance = new HangingSnackbarController();
        }
        return instance;
    }

    protected void show(HangingSnackbar snackbar) {
        if (!snackbarInView) {
            if (queuedSnackbars == null) {
                queuedSnackbars = new ArrayList<>();
            }
            if (queuedSnackbars.isEmpty()) {
                queuedSnackbars.add(snackbar);
            }
            snackbar.animateIn();
            snackbarInView = true;

            if (snackbar.getLength() != HangingSnackbar.LENGTH_INDEFINITE) {
                Timer timer = new Timer(snackbar.getLength());
                timer.start();
            }
        } else {
            queuedSnackbars.add(snackbar);
        }
    }

    protected void hide() {
        queuedSnackbars.get(0).animateOut();
    }

    protected boolean isSnackbarInView (HangingSnackbar snackbar) {
        if (queuedSnackbars != null && !queuedSnackbars.isEmpty()) {
            return (snackbarInView && snackbar == queuedSnackbars.get(0));
        }
        return false;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        //Not needed
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        HangingSnackbar snackbar = queuedSnackbars.get(0);
        snackbarInView = false;
        snackbar.removeViewViewFromParent();
        queuedSnackbars.remove(0);
        if (!queuedSnackbars.isEmpty()) {
            show(queuedSnackbars.get(0));
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        //Not needed
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        //Not needed
    }

    private class Timer extends CountDownTimer {

        public Timer(long length) {
            super(length, 1000);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            hide();
        }
    }
}
