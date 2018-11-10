package com.app.diceroid.nerede.ResourceFiles;

import android.os.Handler;



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
