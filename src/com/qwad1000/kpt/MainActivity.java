package com.qwad1000.kpt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private String[] mTransportType;
    private DrawerLayout mDrawerLayout;
    private ListView mNavigationDrawerList;

    private ListView mainListView;
    private TransportListAdapter transportListAdapter;

    protected ProgressDialog progressDialog;

    private String currentTransportType;
    private boolean isWeekend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTransportType = getResources().getStringArray(R.array.transport_type);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerList = (ListView) findViewById(R.id.left_drawer);

        mNavigationDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mTransportType));
        mNavigationDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mainListView = (ListView) findViewById(R.id.content_list);

        currentTransportType = getResources().getString(R.string.bus);
        isWeekend = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_btn:

                String resource = getResources().getString(isWeekend ? R.string.weekend_day_url : R.string.working_day_url);
                progressDialog = ProgressDialog.show(MainActivity.this, "Working...", "request to server", true, false);
                new ParseSite().execute(resource);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //selectItem(position);
        }
    }

    private class ParseSite extends AsyncTask<String, String, List<TransportItem>> {

        protected List<TransportItem> doInBackground(String... arg) {
            List<TransportItem> output = new ArrayList<>();
            try {
                HtmlHelper hh = new HtmlHelper(new URL(arg[0]));
                String currentDayType = getResources().getString(isWeekend ? R.string.weekend_day : R.string.working_day);
                List<TagNode> links = hh.getLinks(getResources().getString(R.string.schedule_url),
                        currentTransportType, currentDayType);

                for (TagNode node : links) {
                    String url = node.getAttributeByName("href");
                    CharSequence ch = node.getElementListByName("strong", true).get(0).getText();


                    //String num = (String)node.getChildTagList().get(0).getChildTagList().get(0).getText();
                    //String num = (String)node.getChildTags()[0].getText();

                    //publishProgress(url);
                    TransportItem item = new TransportItem(0, ch.toString(), TransportType.Tram, new URL(url));
                    output.add(item);
                }
                /*URL url = new URL(arg[0]);
                InputStream stream = url.openStream();
                InputStreamReader reader = new InputStreamReader(stream);
                Scanner scanner = new Scanner(reader);
                String str = scanner.next();
                Log.d("asdf", str);
                publishProgress(str);
                Thread.sleep(1000);*/

            } catch (IOException e) {
                publishProgress("cant do it");
                Log.e("htmlErr", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return output;
        }

        protected void onProgressUpdate(String... progressString) {
            Toast.makeText(MainActivity.this, progressString[0], Toast.LENGTH_SHORT);
        }

        //Событие по окончанию парсинга
        protected void onPostExecute(List<TransportItem> output) {
            //Убираем диалог загрузки
            progressDialog.dismiss();
            //Находим ListView
            mainListView = (ListView) findViewById(R.id.content_list);
            //Загружаем в него результат работы doInBackground
            transportListAdapter = new TransportListAdapter(MainActivity.this, output);
            mainListView.setAdapter(transportListAdapter);
        }
    }
}
