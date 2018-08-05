package com.app.diceroid.nerede.ResourceFiles;

import android.os.Handler;

/**
 * Created by Deniz TERZI on 3/24/2018.
 */

public class CoutdownValidate {
    public static void countdown(final ICallback callback, final int time)
    {
        final Handler handler = new Handler();
        handler.postDelayed( new Runnable(){
            @Override
            public void run() {
                callback.onEndTime();
                handler.postDelayed(this, time);
            }
        }, time);
    }

    public interface ICallback{
        void onEndTime();
    }
}
