package bhj.cancybabe.gotoplayer.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Cancybabe on 2016/11/9.
 * 此广播接收者用于接收系统受到信息的广播
 */
public class SmsContentReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object[] objects = (Object[]) bundle.get("pdus");
        for (int i = 0; i < objects.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])objects[i]);
            String body = smsMessage.getMessageBody();
            if (body.contains("GoToPlay")){
                String code = body.substring(16,22);
                Log.i("myTag",code);
                notify(code);
            }
        }

    }


    public  static  OnGetSmsNotifySetSmsListener onGetSmsNotifySetSmsListener;


    public interface OnGetSmsNotifySetSmsListener{
        void onSetText(String str);
    }

    public static void  setOnGetSmsNotifySetSmsListener(OnGetSmsNotifySetSmsListener onGetSmsNotifySetSmsListener){
        SmsContentReceiver.onGetSmsNotifySetSmsListener = onGetSmsNotifySetSmsListener;
    }

    public void notify(String str){
        if (onGetSmsNotifySetSmsListener != null){
            onGetSmsNotifySetSmsListener.onSetText(str);
        }
    }
}
