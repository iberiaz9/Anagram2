package com.example.anagram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import android.view.View;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	TextView mTextView;
	EditText mEditText;
	String mPrevAns = "";
	String mTheWord;
	Random random = new Random();
	String [] lines;
	int max;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTextView = (TextView) findViewById(R.id.textView1);
		mEditText = (EditText) findViewById(R.id.editText1);
		String line = readRawTextFile(R.raw.anag);
		lines = line.split("\n");
		max = lines.length;
		
		mTextView.setText("Press Start for anagram puzzle!!");
		
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
	
	public String readRawTextFile(int resId)
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