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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.qwad1000.kpt.da.webload.HtmlHelper;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mNavigationDrawerListView;

    private ListView mainListView;
    private TransportListAdapter transportListAdapter;
    private DrawableListAdapter drawableListAdapter;

    private ProgressBar progressBar;

    private TransportTypeEnum currentTransportType;
    private boolean isWeekend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerListView = (ListView) findViewById(R.id.left_drawer);

        List<TransportTypeEnum> transportTypeEnumList = new ArrayList<>();
        for (TransportTypeEnum i : TransportTypeEnum.values()) {
            transportTypeEnumList.add(i);
        }

        mNavigationDrawerListView.setAdapter(drawableListAdapter = new DrawableListAdapter(this, transportTypeEnumList));
        mNavigationDrawerListView.setOnItemClickListener(new DrawerItemClickListener());

        mainListView = (ListView) findViewById(R.id.content_list);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ScheduleViewActivity.class);
                TransportItem currentTransportItem = (TransportItem) parent.getItemAtPosition(position);
                intent.putExtra("transport_schedule_website", currentTransportItem.getUrl().toString());
                intent.putExtra("transport_number", currentTransportItem.getNumber());
                intent.putExtra("transport_type", currentTransportType.getName(MainActivity.this));
                intent.putExtra("daytype", isWeekend);

                MainActivity.this.startActivity(intent);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.download_progressBar);


        if (savedInstanceState != null) {
            currentTransportType = TransportTypeEnum.values()[savedInstanceState.getInt("last_transport_type")];
            isWeekend = savedInstanceState.getBoolean("last_day_type");
        } else {
            isWeekend = true; //todo:current day init
            currentTransportType = TransportTypeEnum.Bus;
        }

        selectNavigationDrawerItem(currentTransportType.toInt());

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("last_transport_type", currentTransportType.toInt());
        savedInstanceState.putBoolean("last_day_type", isWeekend);

        super.onSaveInstanceState(savedInstanceState);
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
        currentTransportType = (TransportTypeEnum) drawableListAdapter.getItem(position);
        mNavigationDrawerListView.setItemChecked(position, true);
        setTitle(currentTransportType.getName(this));
        mDrawerLayout.closeDrawer(mNavigationDrawerListView);

        Log.d("transportType", currentTransportType.getName(this));
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
                Log.d("htmlHelper: url", arg[0]);
                HtmlHelper hh = new HtmlHelper(new URL(arg[0]));
                String currentDayType = getResources().getString(isWeekend ? R.string.weekend_day : R.string.working_day);
                List<TagNode> links = hh.getLinks(getResources().getString(R.string.schedule_url),
                        currentTransportType.getUrlPart(MainActivity.this), currentDayType);

                for (TagNode node : links) {
                    String url = node.getAttributeByName("href");
                    CharSequence ch = node.getElementListByName("strong", true).get(0).getText();

                    TransportTypeEnum en = (TransportTypeEnum) mNavigationDrawerListView.getSelectedItem();
                    TransportItem item = new TransportItem(0, ch.toString(), en, new URL(url), isWeekend);

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
            Toast.makeText(MainActivity.this, progressString[0], Toast.LENGTH_SHORT).show();
        }

        protected void onPostExecute(List<TransportItem> output) {
            progressBar.setVisibility(View.GONE);
            mainListView = (ListView) findViewById(R.id.content_list);
            transportListAdapter = new TransportListAdapter(MainActivity.this, output);
            mainListView.setAdapter(transportListAdapter);
        }
    }
}
