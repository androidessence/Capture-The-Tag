package adammcneilly.capturethetag;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import adammcneilly.capturethetag.Utilities.FlagUtility;


public class FlagWriteActivity extends AppCompatActivity {

    boolean mWriteMode = false;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    EditText flagName;
    private TextView mTapTag;

    private String mGameName;
    private String mTeamName;

    public static final String ARG_GAME = "game";
    public static final String ARG_TEAM = "team";

    String mimeType = "application/adammcneilly.capturethetag";

    private String tagSerial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_write);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        flagName = (EditText) findViewById(R.id.flag_name);
        mTapTag = (TextView) findViewById(R.id.tap_tag);

        flagName.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mNfcAdapter = NfcAdapter.getDefaultAdapter(FlagWriteActivity.this);
                mNfcPendingIntent = PendingIntent.getActivity(FlagWriteActivity.this, 0,
                        new Intent(FlagWriteActivity.this, FlagWriteActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

                enableTagWriteMode();

                mTapTag.setText(getResources().getString(R.string.approach_tag_label));

                //                new AlertDialog.Builder(FlagWriteActivity.this).setTitle("Touch tag to write")
//                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                            @Override
//                            public void onCancel(DialogInterface dialog) {
//                                disableTagWriteMode();
//                            }
//
//                        }).create().show();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void afterTextChanged(Editable s){}
        });

        mGameName = getIntent().getStringExtra(ARG_GAME);
        mTeamName = getIntent().getStringExtra(ARG_TEAM);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] mWriteTagFilters = new IntentFilter[] { tagDetected };
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }

    private void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Tag writing mode
        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefRecord record = NdefRecord.createMime(mimeType, getFlagString().getBytes());
            NdefMessage message = new NdefMessage(new NdefRecord[] { record });

            if (writeTag(message, detectedTag)) {

                this.tagSerial = Global.ByteArrToSerial(detectedTag.getId());
                new FlagUtility().AddFlag(
                        mGameName,
                        mTeamName,
                        this.tagSerial,
                        flagName.getText().toString());

                mTapTag.setText("Successfully wrote tag " + flagName.getText().toString() + "!");
            }
        }
    }

    /*
    * Writes an NdefMessage to a NFC tag
    */
    public boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag not writable",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag too small",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                ndef.writeNdefMessage(message);
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    public String getFlagString(){
        return mGameName + "," + mTeamName + "," + flagName.getText().toString() + "," + this.tagSerial;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
