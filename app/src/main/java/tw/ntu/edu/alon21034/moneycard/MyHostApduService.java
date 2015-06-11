package tw.ntu.edu.alon21034.moneycard;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by alon21034 on 2015/6/11.
 */
public class MyHostApduService extends HostApduService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("!!", "!! on create");
    }

    private byte[] getWelcomeMessage() {
        return "Hello".getBytes();
    }

    private byte[] getNextMessage(int n) {
        Log.d("!!", "!! return " + n + "  " + (byte)MainActivity.money_num);
        return (n+"").getBytes();
    }

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        if (selectAidApdu(commandApdu)) {
            Log.d("!!", "!! Application selected");
            return getWelcomeMessage();
        } else {
            Intent intent = new Intent();
            intent.setAction(MainActivity.FILTER);
            int n = MainActivity.money_num;
            if (commandApdu[0] == (byte)0xaa) {
                intent.putExtra(MainActivity.KEY_BROADCAST_MODE, 0);
                sendBroadcast(intent);
                Log.d("!!","!! query");
            } else if (commandApdu[0] == (byte)0xbb) {
                intent.putExtra(MainActivity.KEY_BROADCAST_MODE, 1);
                sendBroadcast(intent);
                n += MainActivity.ADD;
                Log.d("!!","!! add");
            } else if (commandApdu[0] == (byte)0xcc) {
                intent.putExtra(MainActivity.KEY_BROADCAST_MODE, 2);
                sendBroadcast(intent);
                n -= MainActivity.SUB;
                Log.d("!!", "!! sub");
            } else {

            }
            sendResponseApdu(getNextMessage(n));
            return getNextMessage(n);

//            sendResponseApdu(new byte[]{(byte) MainActivity.money_num});
//            return new byte[]{(byte)MainActivity.money_num};
        }
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d("!!", "!! on deactivated " + reason);

    }

    private boolean selectAidApdu(byte[] apdu) {
        return apdu.length >= 2 && apdu[0] == (byte)0 && apdu[1] == (byte)0xa4;
    }
}
