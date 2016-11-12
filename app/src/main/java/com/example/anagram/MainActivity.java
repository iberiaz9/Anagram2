package com.example.anagram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    TextView mTextView;
    EditText mEditText;
    String mPrevAns = "";
    String mTheWord;
    Random random = new Random();
    String [] lines;
    int max;
    String mTOval = null;
    AsyncTask<Integer, Void, Integer> mAtask = null;

    private class myAtask extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                Thread.sleep(params[0] * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Toast.makeText(getApplicationContext(), "Timeout!", Toast.LENGTH_LONG).show();
            if (mTOval != null)
                mTextView.setText("Your time of " + mTOval + " secs is up :-(");
            else
                mTextView.setText("Your time is up :-(");
            mEditText.setText(mPrevAns);
            mAtask = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textView1);
        mEditText = (EditText) findViewById(R.id.editText1);
        String line = readRawTextFile(R.raw.anag);
        lines = line.split("\n");
        max = lines.length;

        mTextView.setText("Press Start for anagram puzzle ...");

        final Button button = (Button) findViewById(R.id.button1);
        final Button button2 = (Button) findViewById(R.id.button2);

        // Link UI elements to actions in code
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int idx = (int) (random.nextFloat() * max);
                    String [] words = lines[idx].split(" ");
                    int wmax = words.length;
                    int widx = (int) (random.nextFloat() * wmax);
                    mTheWord = words[widx];
                    mTextView.setText(wmax - 1 + " anagram(s) of " + mTheWord);
                    mEditText.setText("");
                    mPrevAns = lines[idx];
                    if (mAtask != null) mAtask.cancel(true);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    mTOval = prefs.getString(getString(R.string.pref_location_key),
                            getString(R.string.pref_location_default));
                    if (mTOval != null) {
                        int val = new Integer(mTOval);
                        if (val != 0) {
                            mAtask = new myAtask();
                            mAtask.execute(val);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ans = mEditText.getText().toString().toLowerCase();
                    if ( !ans.isEmpty() )
                        ans = mTheWord + " " + ans;
                    String [] words = mPrevAns.split(" ");
                    int j = 0;
                    if ( !ans.isEmpty() ) {
                        for (int i = 0; i < words.length ; i++)
                            if (ans.contains(words[i]))
                                j++;
                        if ( j == words.length )
                            mTextView.setText("Correct! :-)");
                        else
                            mTextView.setText("Sorry :-(");
                    } else
                        mTextView.setText("Sorry :-(");

                    mEditText.setText(mPrevAns);
                    if (mAtask != null) mAtask.cancel(true);
                    mAtask = null;
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String readRawTextFile(int resId)
    {
        InputStream inputStream = this.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while (( line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }

        return text.toString();
    }
}