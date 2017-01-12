package com.google.developer.udacityalumni.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.developer.udacityalumni.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewFragment extends Fragment {
    public static final String ARG_URL = "param_url";

    @BindView(R.id.webview)
    WebView mWebView;

    private String mParamUrl;

    public WebViewFragment() { }

    public static WebViewFragment newInstance(String paramUrl) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, paramUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamUrl = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
        ButterKnife.bind(this, rootView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                hideWebPageToolbar();
            }
        });
        mWebView.loadUrl(mParamUrl);

        return rootView;
    }

    private void hideWebPageToolbar() {
        mWebView.loadUrl("javascript:(function() { " +
            "document.getElementsByClassName('header__navbar')[1].style.display='none'; })()");
    }
}
