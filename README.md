# Hanging Snackbar

A (mostly) material Snackbar equivalent that drops instead of rises.

<img src="http://i.imgur.com/vyZgbph.gif" alt="Regular Snackbar" width="320"/>
<img src="http://i.imgur.com/rVmIgBa.gif" alt="Regular Snackbar with action" width="320"/>
<img src="http://i.imgur.com/qT2BttX.gif" alt="Indefinite Snackbar" width="320"/>
<img src="http://i.imgur.com/kK6PvQB.gif" alt="Customised Snackbar with object" width="320"/>

## Code

```
private IHangingSnackbarCallback.OnActionClickedListener<YourObject> snackbarCallback = new IHangingSnackbarCallback.OnActionClickedListener<YourObject>() {
    @Override
    public void actionClicked(YourObject obj) {
        Log.d("ACT_CLICKED", obj.toString());
    }
};

HangingSnackbar snackbar = new HangingSnackbar.Builder(getBaseContext(), parentLayoutView, HangingSnackbar.LENGTH_SHORT)
      .setText("Deleted item.")
      .setActionText("Undo", snackbarCallback, yourObject)
      .build();
      
snackbar.show();
```
Callback and object can be null.

parentLayoutView must be a RelativeLayout, FrameLayout, or CoordinatorLayout. Other layouts will throw an exception

### Builder customisation methods
```
void setText(String text, int colorId)
void setActionText(String actionText, IHangingSnackbarCallback.OnActionClickedListener actionCallback, T callbackObject, int colorId)
void setTextTypeface(Typeface typeface)
void setBackgroundColor(int colorId)
void setOnDismissedListener(IHangingSnackbarCallback.OnDismissedListener dismissCallback)
```
colorId refers to the resource ID.
setText and setActionText also allow resource ID's in place of the String

### Snackbar methods
```
void show()
void dismiss()
boolean isInView()
boolean isInQueue()
```

## Installation
Project build.gradle:
```
repositories {
    jcenter()
}
```

App build.gradle:
```
dependencies {
    compile 'com.picklemixel.mister:hangingsnackbar:0.2.1'
}
```
