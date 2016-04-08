package com.ideabus.ideabus_structure.handler;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Ting on 16/4/5.
 */
public class MyHandler extends Handler {

    public static final int CODE_DOWNLOAD = 1;

    @Override
    public void handleMessage(Message msg){
        switch (msg.what){
            case CODE_DOWNLOAD:
                break;
        }
    }
}
