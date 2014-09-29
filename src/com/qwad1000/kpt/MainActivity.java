package com.qwad1000.kpt;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
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

    private ProgressBar progressBar;

    private String currentTransportTypeArg;
    private boolean isWeekend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTransportType = getResources().getStringArray(R.array.transport_type);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerList = (ListView) findViewById(R.id.left_drawer);

        //mNavigationDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mTransportType));
        List<TransportTypeEnum> transportTypeEnumList = new ArrayList<>();
        for (TransportTypeEnum i : TransportTypeEnum.values()) {
            transportTypeEnumList.add(i);
        }
        mNavigationDrawerList.setAdapter(new DrawableListAdapter(this, transportTypeEnumList));
        mNavigationDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        isWeekend = true;

        mainListView = (ListView) findViewById(R.id.content_list);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ScheduleViewActivity.class);
                TransportItem currentTransportItem = (TransportItem) parent.getItemAtPosition(position);
                intent.putExtra("transport_schedule_website", currentTransportItem.getUrl().toString());
                intent.putExtra("transport_number", currentTransportItem.getNumber());
                intent.putExtra("transport_type", currentTransportTypeArg);
                intent.putExtra("daytype", isWeekend);

                MainActivity.this.startActivity(intent);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.download_progressBar);
        selectNavigationDrawerItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.dayType_btn).setChecked(isWeekend);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_btn:
                downloadData();
                return true;
            case R.id.dayType_btn:
                isWeekend = !isWeekend;
                item.setChecked(isWeekend);
                downloadData();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectNavigationDrawerItem(position);
        }
    }

    public void selectNavigationDrawerItem(int position) {
        mNavigationDrawerList.setItemChecked(position, true);
        setTitle(mTransportType[position]);
        mDrawerLayout.closeDrawer(mNavigationDrawerList);

        //todo: implement normal getting of current.
        if (mTransportType[position].equals(getResources().getString(R.string.bus))) {
            currentTransportTypeArg = getResources().getString(R.string.bus_url);
        } else if (mTransportType[position].equals(getResources().getString(R.string.tram))) {
            currentTransportTypeArg = getResources().getString(R.string.tram_url);
        } else if (mTransportType[position].equals(getResources().getString(R.string.trolleybus))) {
            currentTransportTypeArg = getResources().getString(R.string.trolley_url);
        }
        Log.d("transportTYpe", currentTransportTypeArg);
        downloadData();
    }

    private void downloadData() {
        String resource = getResources().getString(isWeekend ? R.string.weekend_day_url : R.string.working_day_url);
        progressBar.setVisibility(View.VISIBLE);
        new ParseSite().execute(resource);
    }

    //todo: refactor this to da level
    private class ParseSite extends AsyncTask<String, String, List<TransportItem>> {

        protected List<TransportItem> doInBackground(String... arg) {
            List<TransportItem> output = new ArrayList<TransportItem>();
            try {
                HtmlHelper hh = new HtmlHelper(new URL(arg[0]));
                String currentDayType = getResources().getString(isWeekend ? R.string.weekend_day : R.string.working_day);
                List<TagNode> links = hh.getLinks(getResources().getString(R.string.schedule_url),
                        currentTransportTypeArg, currentDayType);

                for (TagNode node : links) {
                    String url = node.getAttributeByName("href");
                    CharSequence ch = node.getElementListByName("strong", true).get(0).getText();

                    TransportItem item = new TransportItem(0, ch.toString(), TransportTypeEnum.Tram, new URL(url));
                    //todo: add id initialize
                    output.add(item);
                }
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

        protected void onPostExecute(List<TransportItem> output) {
            progressBar.setVisibility(View.GONE);
            mainListView = (ListView) findViewById(R.id.content_list);
            transportListAdapter = new TransportListAdapter(MainActivity.this, output);
            mainListView.setAdapter(transportListAdapter);
        }
    }
}
