package com.uic.demo.fetchdatafromweb.fetchdatafromweb;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button button_data;
    TextView textView_data;

    private webcrawl wc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_data = (Button) findViewById(R.id.button_data);
        textView_data = (TextView) findViewById(R.id.textView_data);

        button_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "################## D E B U G G I N G ###################");
                wc = new webcrawl();
                wc.execute();
            }
        });
    }

    private class webcrawl extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            textView_data.setText("reading...");
            //progress dialog here...
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Document doc = null;
            final StringBuilder builder = new StringBuilder();
            try{
                doc = Jsoup.connect("http://www.phivolcs.dost.gov.ph/html/update_SOEPD/EQLatest.html").get();
                String title = doc.title();
                textView_data.setText("Title: " + title);
            }catch(IOException e){
                textView_data.setText("\n" + "Error: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Loading Finished",Toast.LENGTH_SHORT);
        }

    }


}
