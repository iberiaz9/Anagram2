package com.example.anagram

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Random

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    internal var mTextView: TextView? = null
    internal var mEditText: EditText? = null
    internal var mPrevAns = ""
    internal var mTheWord: String? = null
    internal var random = Random()
    internal var lines: Array<String>? = null
    internal var max: Int = 0
    internal var mTOval: String? = null
    var mAtask: myAtask? = null

    inner class myAtask : AsyncTask<Int, Void, Int?>() {
        override fun doInBackground(vararg params: Int?): Int? {
            try {
                Thread.sleep((1000 * params[0]!!).toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            return 0
        }

        override fun onPostExecute(result: Int?) {
            Toast.makeText(applicationContext, "Timeout!", Toast.LENGTH_LONG).show()
            if (mTOval != null)
                mTextView!!.text = "Your time of $mTOval secs is up :-("
            else
                mTextView!!.text = "Your time is up :0"
            mEditText!!.setText(mPrevAns)
            mAtask = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTextView = findViewById<View>(R.id.textView1) as TextView
        mEditText = findViewById<View>(R.id.editText1) as EditText
        val line = readRawTextFile(R.raw.anag)
        lines = line?.split("\n".toRegex())?.dropLastWhile({ it.isEmpty() })?.toTypedArray()
        max = lines!!.size

        mTextView!!.text = "Anagram puzzle (Kotlin) ...!"

        val button = findViewById<View>(R.id.button1) as Button
        val button2 = findViewById<View>(R.id.button2) as Button

        // Link UI elements to actions in code
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                try {
                    val idx = (random.nextFloat() * max).toInt()
                    val words = lines!![idx].split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    val wmax = words.size
                    val widx = (random.nextFloat() * wmax).toInt()
                    mTheWord = words[widx]
                    mTextView!!.setText((wmax - 1).toString() + " anagram(s) of " + mTheWord)
                    mEditText!!.setText("")
                    mPrevAns = lines!![idx]
                    if (mAtask != null) mAtask!!.cancel(true)
                    val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                    val mTOval = prefs.getString(getString(R.string.pref_location_key),
                            getString(R.string.pref_location_default))
                    if (mTOval != null) {
                        val value: Int = mTOval.toInt()
                        if (value != 0) {
                            mAtask = myAtask()
                            mAtask!!.execute(value)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                }

            }
        })

        button2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                try {
                    var ans = mEditText!!.text.toString().toLowerCase()
                    if (!ans.isEmpty())
                        ans = mTheWord + " " + ans
                    val words = mPrevAns.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    var j = 0
                    if (!ans.isEmpty()) {
                        for (i in words.indices)
                            if (ans.contains(words[i]))
                                j++
                        if (j == words.size)
                            mTextView!!.text = "Correct! :-)"
                        else
                            mTextView!!.text = "Sorry :-("
                    } else
                        mTextView!!.text = "Sorry :-("

                    mEditText!!.setText(mPrevAns)
                    if (mAtask != null) mAtask!!.cancel(true)
                    mAtask = null
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                }

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun readRawTextFile(resId: Int): String? {
        val inputStream = this.resources.openRawResource(resId)

        val inputreader = InputStreamReader(inputStream)
        val buffreader = BufferedReader(inputreader)
        var line: String?
        val text = StringBuilder()

        try {
            line = buffreader.readLine()
            while (line != null) {
                text.append(line)
                text.append('\n')
                line = buffreader.readLine()
            }
        } catch (e: IOException) {
            return null
        }

        return text.toString()
    }

    companion object {
        private val TAG = "MainActivity"
    }
}