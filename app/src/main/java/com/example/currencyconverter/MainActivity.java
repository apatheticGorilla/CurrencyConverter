package com.example.currencyconverter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


/*
implementation 'com.android.volley.volley:1.2.0'
	
	<uses-permission android:name="android.permission.INTERNET" />
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	android:usesClearTextTraffic="true"
	
	//I found this API which is Completely free that lets me change the base currency and allows unlimited calls:
	api: "https://api.exchangeratesapi.io/latest?base=" + base + "&symbols=" + convertTo
 */

public class MainActivity extends AppCompatActivity {
	Spinner spinnerBase, spinnerTo;
	String base, convertTo;
	EditText amountView;
	TextView textView;
	//used for api calls in code
	ArrayList<String> codes = new ArrayList<>(Arrays.asList("USD", "GBP", "CNY", "EUR", "JPY", "MXN", "RUB", "AUD", "CAD", "CHF", "INR"));
	ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(
			"Please Select",
			"US Dollars",
			"British Pounds",
			"Chinese Yen",
			"Euros",
			"Japanese Yen",
			"Mexican pesos",
			"Russian Rubles",
			"Australian Dollars",
			"Canadian Dollars",
			"Swiss Franc",
			"Indian Rupees"));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		spinnerBase = findViewById(R.id.spinnerBase);
		spinnerTo = findViewById(R.id.spinnerTo);
		amountView = findViewById(R.id.amount);
		textView = findViewById(R.id.result);

		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
		spinnerBase.setAdapter(adapter);
		spinnerTo.setAdapter(adapter);
		spinnerBase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position != 0) base = codes.get(position - 1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position != 0) convertTo = codes.get(position - 1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	public void OnClick(View view) {
		String amountText = amountView.getText().toString();
		System.out.println(base != null);
		System.out.println(convertTo != null);
		System.out.println(amountText.isEmpty());
		if (base != null && convertTo != null && !amountText.isEmpty()) {
			double amount = Double.parseDouble(amountText);
			parseJSON(amount);
		}
	}

	public void parseJSON(double baseAmount) {
		RequestQueue queue = Volley.newRequestQueue(this);
		String url = "https://api.exchangeratesapi.io/latest?base=" + base + "&symbols=" + convertTo;
		@SuppressLint({"DefaultLocale", "SetTextI18n"}) JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
			try {
				JSONObject object = response.getJSONObject("rates");
				double rate = object.getDouble(convertTo);

				textView.setText(String.format("%.2f", rate * baseAmount));
			} catch (JSONException e) {
				e.printStackTrace();
				textView.setText("error");
			}
		}, error -> textView.setText("error")
		);
		queue.add(objectRequest);
	}
}