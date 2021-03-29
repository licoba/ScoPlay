package com.licoba.scoplay.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;

public class BluetoothUtil {

    private String TAG = "BluetoothUtil";

    private static BluetoothUtil mBluetoothUtil;

    private static final int SCO_CONNECT_TIME = 5;
    private int mConnectIndex = 0;

    private AudioManager mAudioManager = null;
    static Context mContext;

    private BluetoothUtil() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        }
    }

    public static BluetoothUtil getInstance(Context context) {
        mContext = context;
        if (mBluetoothUtil == null) {
            mBluetoothUtil = new BluetoothUtil();
        }
        return mBluetoothUtil;
    }

    public void openSco( final IBluetoothConnectListener listener) {
        if (!mAudioManager.isBluetoothScoAvailableOffCall()) {
            Log.e(TAG, "系统不支持蓝牙录音");
            listener.onError("Your device no support bluetooth record!");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAudioManager.stopBluetoothSco();
                mAudioManager.startBluetoothSco();
                mConnectIndex = 0;
                mContext.registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        int state = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1);
                        boolean bluetoothScoOn = mAudioManager.isBluetoothScoOn();
                        if (AudioManager.SCO_AUDIO_STATE_CONNECTED == state) { // 判断值是否是：1
                            mAudioManager.setBluetoothScoOn(true);  //打开SCO
                            listener.onSuccess();
                            mContext.unregisterReceiver(this);  //取消广播，别遗漏
                        } else {//等待一秒后再尝试启动SCO
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (mConnectIndex < SCO_CONNECT_TIME) {
                                mAudioManager.startBluetoothSco();//再次尝试连接
                            } else {
                                listener.onError("open sco failed!");
                                mContext.unregisterReceiver(this);  //取消广播，别遗漏
                            }
                            mConnectIndex++;
                        }
                    }
                }, new IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED));
            }
        }).start();

    }


    public void closeSco() {
        boolean bluetoothScoOn = mAudioManager.isBluetoothScoOn();
        if (bluetoothScoOn) {
            mAudioManager.setBluetoothScoOn(false);
            mAudioManager.stopBluetoothSco();
        }
        mBluetoothConnectListener = null;
    }

    public interface IBluetoothConnectListener {
        void onError(String error);

        void onSuccess();
    }

    IBluetoothConnectListener mBluetoothConnectListener;
}