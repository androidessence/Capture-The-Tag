package adammcneilly.capturethetag;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import adammcneilly.capturethetag.Utilities.FlagUtility;

public class ReadFlagActivity extends AppCompatActivity {
    private boolean mReadMode;
    private NfcAdapter mNFCAdapter;
    private PendingIntent  mNFCPendingIntent;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_flag);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        readTag();
        enableReadMode();
    }

    private void readTag(){
        mNFCAdapter = NfcAdapter.getDefaultAdapter(this);

        if(mNFCAdapter == null){
            //TODO: NFC Not supported
            return;
        }

        Intent nfcIntent = new Intent(ReadFlagActivity.this, ReadFlagActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mNFCPendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
    }

    private void enableReadMode(){
        mReadMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] mReadTagFilters = new IntentFilter[] { tagDetected };
        mNFCAdapter.enableForegroundDispatch(this, mNFCPendingIntent, mReadTagFilters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Tag writing mode
        if (mReadMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            String action = intent.getAction();
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

                String type = intent.getType();
                if (MIME_TEXT_PLAIN.equals(type)) {

                    Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                    new NdefReaderTask().execute(tag);

                } else {
                    //TODO:
                    //Log.d(TAG, "Wrong mime type: " + type);
                }
            } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

                // In case we would still use the Tech Discovered Intent
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                String[] techList = tag.getTechList();
                String searchedTech = Ndef.class.getName();

                for (String tech : techList) {
                    if (searchedTech.equals(tech)) {
                        new NdefReaderTask().execute(tag);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     *
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        //TODO:
                        //Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String utf16 = "UTF-16";
            String utf8 = "UTF-8";
            String textEncoding = ((payload[0] & 128) == 0) ? utf8 : utf16;

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                mTextView.setText("Read content: " + result);
            }
        }
    }
}
