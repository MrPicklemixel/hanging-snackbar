# Hanging Snackbar

A (mostly) material Snackbar equivilant that drops instead of rises.

<img src="http://i.imgur.com/vyZgbph.gif" alt="Regular Snackbar" style="width: 320px; display: inline-block;"/>
<img src="http://i.imgur.com/rVmIgBa.gif" alt="Regular Snackbar with action" style="width: 320px; display: inline-block;"/>
<img src="http://i.imgur.com/qT2BttX.gif" alt="Indefinite Snackbar" style="width: 320px; display: inline-block;"/>
<img src="http://i.imgur.com/kK6PvQB.gif" alt="Customised Snackbar with object" style="width: 320px; display: inline-block;"/>

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
