package tw.ntu.edu.alon21034.moneycard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private TextView mTextView;
    public final static String FILTER = "tw.ntu.edu.alon21034.MainReciever";
    public final static String KEY_BROADCAST_MODE = "MODE";
    private final static String PREFS_NAME = "MONEY";
    public final static String KEY_MONEY = "KEY_MONEY";
    public final static int ADD = 100;
    public final static int SUB = 50;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra(KEY_BROADCAST_MODE, 0)) {
                case 0:
                    showMoney(money_num);
                    break;
                case 1:
                    money_num += ADD;
                    showMoney(money_num);
                    break;
                case 2:
                    money_num -= SUB;
                    showMoney(money_num);
                    break;
                default:
                    showMoney(money_num);
                    break;
            }
        }
    };

    public static int money_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.main_value_textview);

        registerReceiver(receiver, new IntentFilter(FILTER));

        money_num = loadFromPrefs(KEY_MONEY);

        showMoney(money_num);

        Log.d("!!", "!!! on create");
    }

    public void showMoney(int n) {
        mTextView.setText(n + "$");
    }

    public void updateMoney(int n) {
        showMoney(n);
        saveToPrefs(n, KEY_MONEY);
    }

    private int loadFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int n = prefs.getInt(key, 1000);
        return n;
    }

    private void saveToPrefs(int n, String key) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, n);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
