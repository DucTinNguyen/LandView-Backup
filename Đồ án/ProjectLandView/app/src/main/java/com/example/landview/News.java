package com.example.landview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class News extends AppCompatActivity {
    private ListView lv;
    private List<String>titles;
    private List<String>links;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_news);
//        ánh xạ view
        lv = findViewById(R.id.lvNews);
        titles = new ArrayList<>();
        links = new ArrayList<>();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//                startActivity(intent);
                Intent intent = new Intent(News.this,NewsWeb.class);
                intent.putExtra("link",links.get(i));
                startActivity(intent);
            }
        });
        new ReadRSS().execute();
    }
    public InputStream GetInputStream(URL url)
    {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public  class ReadRSS extends AsyncTask<String, Void, Exception> {
        Exception exception = null;
        @Override
        protected Exception doInBackground(String... strings) {
            try {
                URL url = new URL("https://vnexpress.net/rss/du-lich.rss");
                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(GetInputStream(url),null);


                int event =xmlPullParser.getEventType();
                boolean insideItem = false;
                while(event != XmlPullParser.END_DOCUMENT)
                {
                    if(event==XmlPullParser.START_TAG)
                    {
                        if(xmlPullParser.getName().equalsIgnoreCase("item"))
                        {
                            insideItem = true;
                        }
                        else if(xmlPullParser.getName().equalsIgnoreCase("title"))
                        {
                            if(insideItem)
                            {
                                titles.add(xmlPullParser.nextText());
                            }
                        }
                        else if(xmlPullParser.getName().equalsIgnoreCase("link"))
                        {
                            if(insideItem)
                            {
                                links.add(xmlPullParser.nextText());
                            }
                        }
                    }
                    else if(event == XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item"))
                    {
                        insideItem = false;
                    }

                    event = xmlPullParser.next();
                }
            } catch (MalformedURLException e) {
                exception = e;
            } catch (XmlPullParserException e) {
                exception = e;
            } catch (IOException e) {
                exception = e;
            }

            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);

            ArrayAdapter<String>arrayAdapter = new ArrayAdapter<String>(News.this, android.R.layout.simple_list_item_1,titles);
            lv.setAdapter(arrayAdapter);
        }
    }
}
