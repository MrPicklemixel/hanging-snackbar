package com.picklemixel.mister.hangingsnackbarexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.picklemixel.mister.hangingsnackbar.HangingSnackbar;
import com.picklemixel.mister.hangingsnackbar.IHangingSnackbarCallback;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout parentLayout;
    HangingSnackbar button3Snackbar;

    private IHangingSnackbarCallback<Void> button2Callback = new IHangingSnackbarCallback<Void>() {
        @Override
        public void actionClicked(Void obj) {
            Toast.makeText(getBaseContext(), "Button 2 Action pressed.", Toast.LENGTH_SHORT).show();
        }
    };

    private IHangingSnackbarCallback<Void> button3Callback = new IHangingSnackbarCallback<Void>() {
        @Override
        public void actionClicked(Void obj) {
            button3Snackbar.dismiss();
        }
    };

    private IHangingSnackbarCallback<RandomObject> button4Callback = new IHangingSnackbarCallback<RandomObject>() {
        @Override
        public void actionClicked(RandomObject obj) {
            Toast.makeText(getBaseContext(), "Random number from snackbar: " + obj.getRandomNumber(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_1).setOnClickListener(this);
        findViewById(R.id.button_2).setOnClickListener(this);
        findViewById(R.id.button_3).setOnClickListener(this);
        findViewById(R.id.button_4).setOnClickListener(this);
        parentLayout = (RelativeLayout) findViewById(R.id.parent_relative_layout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //Regular snackbar
            case R.id.button_1:
                new HangingSnackbar.Builder(getBaseContext(), parentLayout, HangingSnackbar.LENGTH_SHORT)
                        .setText("Hi there! :)")
                        .build()
                        .show();
                break;
            //Regular snackbar with action
            case R.id.button_2:
                new HangingSnackbar.Builder(getBaseContext(), parentLayout, HangingSnackbar.LENGTH_SHORT)
                        .setText("I have an action")
                        .setActionText("Undo", button2Callback, null)
                        .build()
                        .show();
                break;
            //Indefinite snackbar
            case R.id.button_3:
                button3Snackbar = new HangingSnackbar.Builder(getBaseContext(), parentLayout, HangingSnackbar.LENGTH_INDEFINITE)
                        .setText("I need to be dismissed, also I have more chars so I'm bigger")
                        .setActionText("Dismiss", button3Callback, null)
                        .build()
                        .show();
                break;
            //Customised snackbar
            case R.id.button_4:
                RandomObject randomObject = new RandomObject();
                new HangingSnackbar.Builder(getBaseContext(), parentLayout, HangingSnackbar.LENGTH_LONG)
                        .setText("I hold an object of any type, I will return it on the action press")
                        .setActionText("Got it", button4Callback, randomObject)
                        .build()
                        .show();
                break;
        }
    }

    class RandomObject {
        private int randomNumber;

        public RandomObject () {
            Random r = new Random();
            randomNumber = r.nextInt();
        }

        public int getRandomNumber() {
            return randomNumber;
        }
    }
}
