package ali.naseem.newscop;

import android.app.Application;

import ali.naseem.newscop.utils.Utils;

public class CopApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.initialize(getApplicationContext());
    }
}
