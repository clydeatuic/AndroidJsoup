package com.uic.demo.fetchdatafromweb.fetchdatafromweb;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private String _data;
    private int total_rows;
    private String[][] trtd;
    private TextView textView_data;
    private Button button_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_data = (TextView) findViewById(R.id.textView_data);
        textView_data.setMovementMethod(new ScrollingMovementMethod());
        _data = "";
        new FetchDataFromWeb().execute();

        button_refresh = (Button) findViewById(R.id.button_refresh);
        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _data = "";
                textView_data.setText(_data);
                new FetchDataFromWeb().execute();
            }
        });
    }

    private class FetchDataFromWeb extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Android Basic JSoup API");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("http://www.phivolcs.dost.gov.ph/html/update_SOEPD/EQLatest.html").get();
                Elements tables = doc.select("table.MsoNormalTable");
                Elements tr = tables.select("tbody").get(2).getElementsByTag("tr");
                _data += "total rows: " + tr.size();
                total_rows = 20;
                _data += "\nfetched the first " + total_rows + " rows";
                trtd = new String[total_rows][];
                for(int row=0;row<total_rows;row++){
                    Elements td = tr.select("tr").get(row).getElementsByTag("td");
                    trtd[row] = new String[td.size()];
                    for(int col=0;col<td.size();col++){
                        trtd[row][col] = td.get(col).text();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(MainActivity.this, "Extraction finished!", Toast.LENGTH_SHORT).show();
            for(int i=2;i<trtd.length;i++){
                _data += "\n---- "+ i +" ----\n";
                for(int j=0;j<trtd[i].length;j++){
                    _data += trtd[i][j] + "\n";
                }
            }
            textView_data.setText(_data);
            mProgressDialog.dismiss();
        }
    }
}
