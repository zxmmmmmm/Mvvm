package com.bmapleaf.mvvm.help;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ZhangMing on 2017/08/01.
 */

@IntDef({MessageType.MSG_UNKNOWN, MessageType.MSG_LOADING0,
        MessageType.MSG_LOADING1, MessageType.MSG_LOADING2,
        MessageType.MSG_LOADING3, MessageType.MSG_MSG1,
        MessageType.MSG_ERROR1, MessageType.MSG_ERROR2,
        MessageType.MSG_EXTRA1, MessageType.MSG_EXTRAS})
@Retention(RetentionPolicy.SOURCE)
public @interface MessageType {
    /**
     * default message
     */
    int MSG_UNKNOWN = 0;
    /**
     * just loading
     */
    int MSG_LOADING0 = 1;
    /*
    * loading with message
    * */
    int MSG_LOADING1 = 2;
    /*
    * loading with message and progress
    * */
    int MSG_LOADING2 = 3;
    /**
     * hide loading
     */
    int MSG_LOADING3 = 4;
    /**
     * message
     */
    int MSG_MSG1 = 5;
    /**
     * error with message
     */
    int MSG_ERROR1 = 6;
    /**
     * error with message and content
     */
    int MSG_ERROR2 = 7;
    /**
     * extra action
     */
    int MSG_EXTRA1 = 8;
    /**
     * extra action
     */
    int MSG_EXTRAS = 9;
}
