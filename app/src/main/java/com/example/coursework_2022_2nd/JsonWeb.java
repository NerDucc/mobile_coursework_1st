package com.example.coursework_2022_2nd;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.coursework_2022_2nd.databinding.FragmentJsonWebBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class JsonWeb extends Fragment {

    FragmentJsonWebBinding binding;
    WebView webView;
    String serviceLink = "https://cwservice1786.herokuapp.com/sendPayLoad";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJsonWebBinding.inflate(inflater, container, false);
        webView = binding.jsonWeb;
        String jsonString = getArguments().getString("json");
        try {

            URL link = new URL(serviceLink);
            HttpURLConnection con = (HttpURLConnection) link.openConnection();
            JsonThread myTask = new JsonThread((AppCompatActivity) getActivity(), con, jsonString);
            Thread t1 = new Thread(myTask, "JSON Thread");
            t1.start();
        }  catch (IOException e) {
            e.printStackTrace();
        }

        return  binding.getRoot();
    }
    class JsonThread implements Runnable {
        private AppCompatActivity activity;
        private HttpURLConnection con;
        private String jsonPayLoad;

        public JsonThread(AppCompatActivity activity, HttpURLConnection con, String jsonPayLoad) {
            this.activity = activity;
            this.con = con;
            this.jsonPayLoad = jsonPayLoad;
        }

        @Override
        public void run() {
            String response = "";
            if(prepareConnection()){
                response = postJson();
            }
            else {
                response = "Error preparing the connection";
            }
            showResult(response);
        }

        private boolean prepareConnection() {
            try {
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                return true;
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            return false;
        }
        private String postJson() {
            String response = "";
            try {
                String postParameters = "jsonpayload=" + URLEncoder.encode(jsonPayLoad, "UTF-8");;
                con.setFixedLengthStreamingMode(postParameters.getBytes().length);
                PrintWriter out = new PrintWriter(con.getOutputStream());
                out.print(postParameters);
                out.close();
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    response = readStream(con.getInputStream());
                } else {
                    response = "Error contacting server: " + responseCode;
                }
            } catch (Exception e) {
                response = "Error executing code";
            }
            return response;
        }
        private String readStream(InputStream in) {
            StringBuilder sb = new StringBuilder();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String nextLine = "";
                while ((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
        private void showResult(String response) {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    String page = generatePage(response);
                    webView.loadData(page, "text/html", "UTF-8");
                }
            });
        }

        private String generatePage(String response) {
            return "<html><body><p>" + response + "</p></body></html>";
        }


    }
}