# ToggleButton
通常使用的ToggleButton
### Compile
```
allprojects {  
        repositories {  
			...  
			maven { url "https://jitpack.io" }  
		}  
	}  
```
```
dependencies {  
	        compile 'com.github.Gcrj:ToggleButton:1.0.1'  
	}
```

### 使用方法
xml:  
```
 <com.gcrj.togglebuttonlibrary.ToggleButton
            android:id="@+id/tb"
            android:layout_width="200dp"
            android:layout_height="100dp"
            app:anim_duration="200"
            app:back_checked_color="#FF0000"
            app:back_unchecked_color="#0000FF"
            app:checked="true"
            app:circle_color="#000000"
            app:stroke_width="10dp" />
```

java:  
```
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.tb);
        toggleButton.setChecked(false, false);
//                toggleButton.toggle(false);
        toggleButton.setAnimDuration(200);
        toggleButton.setStrokeWidth(2);
        toggleButton.setCircleColor(Color.YELLOW);
        toggleButton.setBackCheckedColor(Color.rgb(200, 200, 0));
        toggleButton.setBackUncheckedColor(Color.GRAY);

        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ToggleButton toggleButton, boolean isChecked) {
                Toast.makeText(MainActivity.this, isChecked + "", Toast.LENGTH_SHORT).show();
            }
        });
 ```
