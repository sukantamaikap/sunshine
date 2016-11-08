package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A placeholder fragment containing weather update view.
 */
public class ForecastFragment extends Fragment {

    private static final String TAG = ForecastFragment.class.getSimpleName();

    private ArrayAdapter<String> mForecastArrayAdapter;
    private ListView mListView;

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.mForecastArrayAdapter = new ArrayAdapter<String>(this.getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textView,
                new ArrayList<String>());

        this.mListView = (ListView) rootView.findViewById(R.id.listview_forcast);
        this.mListView.setAdapter(this.mForecastArrayAdapter);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String forecast = ForecastFragment.this.mForecastArrayAdapter.getItem(position);
                final Intent forecastIntent = new Intent(ForecastFragment.this.getActivity(), DetailActivity.class);
                forecastIntent.putExtra(Intent.EXTRA_TEXT, forecast);
                ForecastFragment.this.getActivity().startActivity(forecastIntent);
            }
        });
        this.setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.fetchWeather();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            fetchWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchWeather() {
        final SharedPreferences defaultLocationPreference = PreferenceManager.getDefaultSharedPreferences(ForecastFragment.this.getActivity());
        final String location = defaultLocationPreference.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        new FetchWeatherTask(this.getActivity(), this.mForecastArrayAdapter).execute(location);
    }
}
