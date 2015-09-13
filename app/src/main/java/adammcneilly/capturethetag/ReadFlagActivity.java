package adammcneilly.capturethetag;

import android.app.Activity;
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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import adammcneilly.capturethetag.Utilities.FlagUtility;
import adammcneilly.capturethetag.Utilities.GameUtility;
import adammcneilly.capturethetag.Utilities.PlayerUtility;

public class ReadFlagActivity extends AppCompatActivity {
    private static final String MIME_TEXT_PLAIN = "application/adammcneilly.capturethetag";
    private TextView mTextView;
    private NfcAdapter mNFCAdapter;
    private static final String TAG = ReadFlagActivity.class.getSimpleName();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_flag);
        mTextView = (TextView) findViewById(R.id.response);

        mNFCAdapter = NfcAdapter.getDefaultAdapter(this);

        if(mNFCAdapter == null){
            //TODO: Handle null adapter.
            finish();
        }

        handleIntent(getIntent());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, mNFCAdapter);
    }

    @Override
    protected void onPause() {
        setupForegroundDispatch(this, mNFCAdapter);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private static void setupForegroundDispatch(Activity activity, NfcAdapter NFCAdapter){
        Intent intent = new Intent(activity, activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);

        IntentFilter[] filter = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filter[0] = new IntentFilter();
        filter[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filter[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        NFCAdapter.enableForegroundDispatch(activity, pendingIntent, filter, null);
    }

    private static void stopForegroundDispatch(final Activity activity, NfcAdapter NFCAdapter) {
        NFCAdapter.disableForegroundDispatch(activity);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
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

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        private String TagSerial = "";

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            this.TagSerial = Global.ByteArrToSerial(tag.getId()).toString();

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                Log.v(TAG, "NDEF not supported");
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_MIME_MEDIA) { //&& Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            Log.v(TAG, "Missed if.");
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
            String utf8 = "UTF-8";
            String utf16 = "UTF-16";
            String textEncoding = ((payload[0] & 128) == 0) ? utf8 : utf16;

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            // Test langcode
            Log.v(TAG, textEncoding);
            //return new String(payload, 1, languageCodeLength, "US-ASCII");
            return new String(payload, 0, payload.length, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v(TAG, "in onPostExecute: " + result);
            //gameName, teamName, flagName

            String[] data = result.split(",");
            String gameName = data[0];
            String teamName = data[1];
            String flagName = data[2];

            // Make sure we are in the right game
            if (gameName.equals(Global.currentGame))
            {
                // If flag is not mine
                if (!teamName.equals(Global.currentPlayer.getTeamName()))
                {
                    // If flag is not captured
                    if (new FlagUtility().GetFlagStatus(gameName, teamName, this.TagSerial) == Global.FlagStatus.Not_Captured)
                    {
                        // Update status of the flag
                        new FlagUtility().SetFlagCapturedStatus(gameName, teamName, this.TagSerial, Global.FlagStatus.In_Progress);
                        // Give the currentUser the flag
                        new PlayerUtility().setCapturedFlag(gameName, teamName, Global.currentPlayer.getName(), result);
                    }
                }
                // It is mine
                else
                {
                    // If I have a flag
                    if (!(new PlayerUtility().GetPlayersCapturedFlagInfo(gameName, teamName, Global.currentPlayer.getName()))
                        .equals(""))
                    {
                        String[] capturedFlagInfo = new PlayerUtility().GetPlayersCapturedFlagInfo(gameName, teamName, Global.currentPlayer.getName()).split(",");
                        // Set that flag as captured
                        new FlagUtility().SetFlagCapturedStatus(gameName, capturedFlagInfo[1], capturedFlagInfo[3], Global.FlagStatus.Captured);
                        // Remove flag from currentUser
                        new PlayerUtility().RemoveCapturedFlagFromUser(gameName, teamName, Global.currentPlayer.getName());
                        // Increment team score
                        //new GameUtility().IncrementScore(gameName, teamName);
                    }

                }
            }

            if (result != null) {
                mTextView.setText("Good work soldier, you've scanned a flag. I've added some details about the flag below, bring it to home base and we'll take care of the enemy. \n\nFlag Name:" + flagName + "\n\nTeam Creator:" + teamName);

                //new FlagUtility().SetFlagCapturedStatus(gameName, teamName, flagName, Global.FlagStatus.In_Progress);
                //new PlayerUtility().SetCapturedFlag(gameName, teamName, Global.currentPlayer.getName(), result);
            }

        }

        public void RunGameConditionals()
        {

        }
    }
}