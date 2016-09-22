# Hanging Snackbar

A (mostly) material Snackbar equivalent that drops instead of rises.

<img src="http://i.imgur.com/vyZgbph.gif" alt="Regular Snackbar" width="320"/>
<img src="http://i.imgur.com/rVmIgBa.gif" alt="Regular Snackbar with action" width="320"/>
<img src="http://i.imgur.com/qT2BttX.gif" alt="Indefinite Snackbar" width="320"/>
<img src="http://i.imgur.com/kK6PvQB.gif" alt="Customised Snackbar with object" width="320"/>

## Code

```
private IHangingSnackbarCallback<YourObject> snackbarCallback = new IHangingSnackbarCallback<YourObject>() {
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
parentLayoutView must be a RelativeLayout or FrameLayout, other layouts will throw an exception

### Customisation methods
```
void setText(String text, int colorId)
void setActionText(String actionText, IHangingSnackbarCallback actionCallback, T callbackObject, int colorId)
void setTextTypeface(Typeface typeface)
void setBackgroundColor(int colorId)
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
    compile 'com.picklemixel.mister:hangingsnackbar:0.1.0'
}
```
