package ntu.edu.tw.moneycardreader;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;


public class MainActivity extends Activity implements View.OnClickListener {

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;

    private final static String PREFS_NAME = "MONEY";
    public final static String KEY_MONEY = "KEY_MONEY";

    private Button addButton;
    private Button subButton;
    private Button queryButton;
    private Button clearButton;

    private final int QUERY = 0;
    private final int ADD = 1;
    private final int SUB = 2;

    private int state;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.activity_main);

        queryButton = (Button) findViewById(R.id.main_query_button);
        addButton = (Button) findViewById(R.id.main_add_button);
        subButton = (Button) findViewById(R.id.main_sub_button);
        clearButton = (Button) findViewById(R.id.main_clear_button);

        queryButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        subButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        state = QUERY;

        Log.d("!!", "!! Reader");

        mTextView = (TextView)findViewById(R.id.main_textview);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter != null) {
            //mTextView.setText("NFC Reader");
        } else {
            mTextView.setText("This phone is not NFC enabled.");
        }

        // create an intent with tag data and deliver to this activity
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { ndefIntent };
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_add_button:
                state = ADD;
                break;
            case R.id.main_query_button:
                state = QUERY;
                break;
            case R.id.main_sub_button:
                state = SUB;
                break;
            case R.id.main_clear_button:
                mTextView.setText(" ");
                break;
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

        Log.d("!!", "!! onNewIntent");

        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = action + "\n\n" + tag.toString();

        // parse through all NDEF messages and their records and pick text type only
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        String result = "";
        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    Log.d("!!", "!! " + data);
                    NdefRecord[] recs = ((NdefMessage)data[i]).getRecords();
                    Log.d("!!", "!!" + recs.length);
                    for (int j = 0; j < recs.length; j++) {
                        Log.d("!!", "!!" + recs[j].getTnf());
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            result = new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1, textEncoding);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }
        }

        if (result != "") {
            int m = loadFromPrefs(result);
            switch (state) {
                case QUERY: break;
                case ADD: m+=100; break;
                case SUB: m-=80; break;
            }

            saveToPrefs(m, result);
            mTextView.setText(m + "");
            state = QUERY;
        } else {
            Toast.makeText(this, "Oops!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {

        Log.d("!!", "!! onResume");

        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
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
}
