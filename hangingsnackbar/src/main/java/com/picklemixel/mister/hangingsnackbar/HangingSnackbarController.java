package com.picklemixel.mister.hangingsnackbar;

import android.animation.Animator;
import android.os.CountDownTimer;

import java.util.ArrayList;

/**
 * Singleton controller used to manage the {@link HangingSnackbar} queue and display snackbars, implements {@link Animator.AnimatorListener} to remove snackbars when once it is out of view
 * @author Paul Jeffries Jones (Mr.Picklemixel)
 */
class HangingSnackbarController implements Animator.AnimatorListener {

    /**
     * A singleton instance
     */
    private static HangingSnackbarController instance;
    /**
     * A list of {@link HangingSnackbar} objects to be displayed
     */
    private ArrayList<HangingSnackbar> queuedSnackbars;
    /**
     * Determines whether a {@link HangingSnackbar} is currently in view
     */
    private boolean snackbarInView;
    /**
     * Incremented count used to give {@link HangingSnackbar} objects a unique ID
     */
    private int count;

    /**
     * Initialises the count
     */
    private HangingSnackbarController () {
        count = 1;
    }

    /**
     * Creates and/or returns the singleton instance
     * @return A singleton instance of this class
     */
    static HangingSnackbarController getInstance() {
        if (instance == null) {
            instance = new HangingSnackbarController();
        }
        return instance;
    }

    /**
     * Displays a {@link HangingSnackbar} or adds it to the queue if a snackbar is already in view
     * @param snackbar The desired {@link HangingSnackbar} to be shown
     */
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

    /**
     * Takes the currently displayed {@link HangingSnackbar} out of view
     */
    void hide() {
        queuedSnackbars.get(0).animateOut();
    }

    /**
     * Returns true if the {@link HangingSnackbar} in question is currently being displayed
     * @param id The unique ID of the {@link HangingSnackbar} in question
     * @return True if the {@link HangingSnackbar} in question is currently being displayed
     */
    boolean isSnackbarInView(int id) {
        return queuedSnackbars != null && !queuedSnackbars.isEmpty() && (snackbarInView && id == queuedSnackbars.get(0).getId());
    }

    /**
     * Returns true if the {@link HangingSnackbar} in question is currently being displayed or is in the queue to be displayed
     * @param id The unique ID of the {@link HangingSnackbar} in question
     * @return True if the {@link HangingSnackbar} in question is currently being displayed or is in the queue to be displayed
     */
    public boolean isSnackbarInQueue(int id) {
        for (HangingSnackbar snackbar : queuedSnackbars) {
            if (snackbar.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Empty method
     */
    @Override
    public void onAnimationStart(Animator animation) {
        //Not needed
    }

    /**
     *  Removes the {@link HangingSnackbar} from its parent view and the queue once it has finished animating out of view, then shows the next in the queue
     */
    @Override
    public void onAnimationEnd(Animator animation) {
        HangingSnackbar snackbar = queuedSnackbars.get(0);
        snackbarInView = false;
        snackbar.removeSnackViewFromParent();
        queuedSnackbars.remove(0);
        if (!queuedSnackbars.isEmpty()) {
            show(queuedSnackbars.get(0));
        }
    }

    /**
     * Empty method
     */
    @Override
    public void onAnimationCancel(Animator animation) {
        //Not needed
    }

    /**
     * Empty method
     */
    @Override
    public void onAnimationRepeat(Animator animation) {
        //Not needed
    }

    /**
     * Returns and increments the count for a unique ID
     * @return The count for a unique ID
     */
    int createId() {
        return count++;
    }

    /**
     * Counts the amount of time the {@link HangingSnackbar} has been in view
     */
    private class Timer extends CountDownTimer {

        /**
         * Initialises the {@link CountDownTimer} using the time length specified
         * @param length The amount of time the timer should run for
         */
        Timer(long length) {
            super(length, 1000);
        }

        /**
         * Empty method
         */
        @Override
        public void onTick(long millisUntilFinished) {
        }

        /**
         * Hides the {@link HangingSnackbar} when the countdown is finished
         */
        @Override
        public void onFinish() {
            hide();
        }
    }
}
