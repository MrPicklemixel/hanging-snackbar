# Hanging Snackbar

A (mostly) material Snackbar equivilant that drops instead of rises.

![alt text](http://i.imgur.com/vyZgbph.gif =480x852 "Regular Snackbar")
![alt text](http://i.imgur.com/rVmIgBa.gif =480x852 "Regular Snackbar with action")
![alt text](http://i.imgur.com/qT2BttX.gif =480x852 "Indefinite Snackbar")
![alt text](http://i.imgur.com/kK6PvQB.gif =480x852 "Customised Snackbar with object")

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
