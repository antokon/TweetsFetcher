package com.example.antonio.twitter;

/**
 * Created by Antonio on 2/27/2018.
 */
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Antonio on 2/27/2018.
 */

public class TwitterI extends AsyncTask<List, List, List<String>>
{
    public static String consumer_key = "D2DZmGXPUXC2iGrTcf89oNNOb";
    public static String consumer_secret_key = "mGshWmTUjUpejbzY2SOPPsxxyw7oZXHJeHOxFKs4MXv9ySeajb";
    public static String access_token = "968193895724208128-DUiJ6CBc7khxwQsHpo1I3nVzdtcw40e";
    public static String access_token_secret = "HSjDC3U47T2jT27Vp3GpTxi8O8H9o95L0UJoAfo3NCfKT";



    public interface AsyncTaskResponse {
        void processResponse(List<String> response);
    }

    AsyncTaskResponse delegateResp = null;
    private String username;

    TwitterI(String username) {
        this.username = username;
    }

    @Override
    protected List<String> doInBackground(List[] lists) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(consumer_key)
                .setOAuthConsumerSecret(consumer_secret_key)
                .setOAuthAccessToken(access_token)
                .setOAuthAccessTokenSecret(access_token_secret);

        TwitterFactory twitterFactory=new TwitterFactory(configurationBuilder.build());
        Twitter twitter= twitterFactory.getInstance();
        List<String> listStrings = new ArrayList<>();
        try {
            List<twitter4j.Status> status = twitter.getUserTimeline(this.username);
            for(twitter4j.Status status1 : status) {
                listStrings.add(status1.getText());
            }

        } catch (Exception e) {
            if(e.toString().contains("statusCode=400")) {
                listStrings.add(0, "statusCode=400");
                Log.d("Timeline: ", "Authentication error!");
            }
            e.printStackTrace();
        }

        return listStrings;
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        delegateResp.processResponse(strings);
    }


}
