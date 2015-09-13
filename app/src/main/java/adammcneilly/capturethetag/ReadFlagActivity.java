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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import adammcneilly.capturethetag.Utilities.FlagUtility;
import adammcneilly.capturethetag.Utilities.GameUtility;
import adammcneilly.capturethetag.Utilities.PlayerUtility;

public class ReadFlagActivity extends AppCompatActivity {
    private static final String MIME_TEXT_PLAIN = "application/adammcneilly.capturethetag";
    private TextView mFlagMessage;
    private TextView mScan;
    private NfcAdapter mNFCAdapter;
    private static final String TAG = ReadFlagActivity.class.getSimpleName();
    private List<Flag> mFlags = new ArrayList<>();
    private HashMap<String, List<Flag>> mTeamFlags = new HashMap<>();
    private Flag myCarriedFlag;
    private TextView mFlagsRemaining;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_flag);
        mFlagMessage = (TextView) findViewById(R.id.flag_message);

        mNFCAdapter = NfcAdapter.getDefaultAdapter(this);

        if(mNFCAdapter == null){
            //TODO: Handle null adapter.
            finish();
        }

        handleIntent(getIntent());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        for(Team team : Global.currentTeams){
            initTeamFlagListener(team.getName());
        }

        mFlagsRemaining = (TextView) findViewById(R.id.flags_remaining);
        mScan = (TextView) findViewById(R.id.scan_ready);
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

    private void initTeamFlagListener(final String teamName){
        Firebase flagsRef = new Firebase(Global.FirebaseURl).child(Global.currentGame).child(teamName).child(Global.FLAGS);
        flagsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String serial = dataSnapshot.getKey();
                String flagName = dataSnapshot.child("flagName").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                Flag newFlag = new Flag(flagName, serial, Global.FlagStatus.valueOf(status), teamName);
                mFlags.add(newFlag);
                if (mTeamFlags.containsKey(teamName)) {
                    mTeamFlags.get(teamName).add(newFlag);
                } else {
                    List<Flag> flagList = new ArrayList<Flag>();
                    flagList.add(newFlag);
                    mTeamFlags.put(teamName, flagList);
                }

                getRemainingFlags();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // Only update our flag
                String serial = dataSnapshot.getKey();
                // my day if off but it's fair for those flying:
                for (Flag f : mFlags) {
                    if (f.getSerialNumber().equals(serial)) {
                        f.setName(dataSnapshot.child("flagName").getValue().toString());
                        f.setStatus(Global.FlagStatus.valueOf(dataSnapshot.child("status").getValue().toString()));
                    }
                }

                getRemainingFlags();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void getRemainingFlags(){
        // Any flags that don't belong to the current user's team that are not completed are available
        int flagCount = 0;
        for(Flag flag : mFlags){
            if(!flag.getTeamName().equals(Global.currentPlayer.getTeamName()) && flag.getStatus() != Global.FlagStatus.Captured){
                flagCount++;
            }
        }

        mFlagsRemaining.setText(flagCount + " flags remaining.");
        if(flagCount == 0){
            mFlagMessage.setText("");
            mScan.setText("Congratulations! You won!");
        }
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
                    // Find our flag
                    for(Flag flag : mFlags){
                        if(flag.getSerialNumber().equals(TagSerial)){
                            if(flag.getStatus() == Global.FlagStatus.Not_Captured){
                                // Update status of the flag
                                new FlagUtility().SetFlagCapturedStatus(gameName, teamName, this.TagSerial, Global.FlagStatus.In_Progress);
                                // Give the currentUser the flag
                                new PlayerUtility().setCapturedFlag(gameName, Global.currentPlayer.getTeamName(), Global.currentPlayer.getName(), result);
                                flag.setTeamName(teamName);
                                myCarriedFlag = flag;
                                mFlagMessage.setText("Enemy flag captured! Hurry home!");
                            }
                        }
                    }
                }
                // It is mine
                else
                {
                    // If I have a flag
                    if(myCarriedFlag != null) {
                        // Set that flag as captured
                        new FlagUtility().SetFlagCapturedStatus(gameName, myCarriedFlag.getTeamName(), myCarriedFlag.getSerialNumber(), Global.FlagStatus.Captured);
                        // Remove from user
                        new PlayerUtility().RemoveCapturedFlagFromUser(gameName, Global.currentPlayer.getTeamName(), Global.currentPlayer.getName());
                        myCarriedFlag = null;
                        mFlagMessage.setText("Enemy flag returned. Great work!");
                    }

                }
            }

        }

        public void RunGameConditionals()
        {

        }
    }
}