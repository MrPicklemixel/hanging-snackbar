package com.picklemixel.mister.hangingsnackbar;

import android.animation.Animator;
import android.os.CountDownTimer;

import java.util.ArrayList;

/**
 * Masterfully pieced together by the Al-Mighty Paul on 19/09/16.
 */
class HangingSnackbarController implements Animator.AnimatorListener {

    private static HangingSnackbarController instance;
    private ArrayList<HangingSnackbar> queuedSnackbars;
    private boolean snackbarInView;
    private int count;

    private HangingSnackbarController () {
        count = 1;
    }

    static HangingSnackbarController getInstance() {
        if (instance == null) {
            instance = new HangingSnackbarController();
        }
        return instance;
    }

    void show(HangingSnackbar snackbar) {
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

    void hide() {
        queuedSnackbars.get(0).animateOut();
    }

    boolean isSnackbarInView(int id) {
        return queuedSnackbars != null && !queuedSnackbars.isEmpty() && (snackbarInView && id == queuedSnackbars.get(0).getId());
    }

    public boolean isSnackbarInQueue(int id) {
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

    public int createId() {
        return count++;
    }

    private class Timer extends CountDownTimer {

        Timer(long length) {
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
