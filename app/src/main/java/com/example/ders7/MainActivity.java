package com.example.ders7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView tryText;
    TextView cadText;
    TextView usdText;
    TextView jpyText;
    TextView chfText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tryText = findViewById(R.id.textView);
        cadText = findViewById(R.id.textView2);
        usdText = findViewById(R.id.textView3);
        jpyText = findViewById(R.id.textView4);
        chfText = findViewById(R.id.textView5);
    }

    public void getRates(View view) {
        DownloadData downloadData = new DownloadData();

        try {
            String url = "https://api.exchangerate-api.com/v4/latest/USD?access_key=9dfd66dfb2fb3c193515bdcc057dd97b";
            downloadData.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1) {
                    char current = (char) data;
                    result.append(current);
                    data = inputStreamReader.read();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String base = jsonObject.getString("base");

                JSONObject ratesObject = jsonObject.getJSONObject("rates");

                String turkishlira = ratesObject.getString("TRY");
                tryText.setText("TRY: " + turkishlira);

                String usd = ratesObject.getString("USD");
                usdText.setText("USD: " + usd);

                String cad = ratesObject.getString("CAD");
                cadText.setText("CAD: " + cad);

                String chf = ratesObject.getString("CHF");
                chfText.setText("CHF: " + chf);

                String jpy = ratesObject.getString("JPY");
                jpyText.setText("JPY: " + jpy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
