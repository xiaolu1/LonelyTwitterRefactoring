package ca.ualberta.cs.lonelytwitter;

import java.util.List;

import ca.ualberta.cs.lonelytweet.ImportantLonelyTweet;
import ca.ualberta.cs.lonelytweet.LonelyTweet;
import ca.ualberta.cs.lonelytweet.NormalLonelyTweet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class LonelyTwitterActivity extends Activity {

	private EditText bodyText;
	private ListView oldTweetsList;

	private List<LonelyTweet> tweets;
	private ArrayAdapter<LonelyTweet> adapter;
	private TweetsFileManager tweetsProvider;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		bodyText = (EditText) findViewById(R.id.body);
		oldTweetsList = (ListView) findViewById(R.id.oldTweetsList);
	}

	@Override
	protected void onStart() {
		super.onStart();

		tweetsProvider = new TweetsFileManager(this);
		setTweets(tweetsProvider.loadTweets());
		adapter = new ArrayAdapter<LonelyTweet>(this, R.layout.list_item,
				getTweets());
		oldTweetsList.setAdapter(adapter);
	}

	public void save(View v) {

		LonelyTweet tweet = determineNormalOrImportant();
	
		if (tweet.isValid()) {
			getTweets().add(tweet);
			adapter.notifyDataSetChanged();

			bodyText.setText("");
			tweetsProvider.saveTweets(getTweets());
		} else {
			Toast.makeText(this, "Invalid tweet", Toast.LENGTH_SHORT).show();
		}
	}

	private LonelyTweet determineNormalOrImportant()
	{

		String text = bodyText.getText().toString();
		LonelyTweet tweet;

		if (text.contains("*")) {
			tweet = new ImportantLonelyTweet(text);
		} else {
			tweet = new NormalLonelyTweet(text);
		}
		return tweet;
	}

	public void clear(View v) {
		getTweets().clear();
		adapter.notifyDataSetChanged();
		tweetsProvider.saveTweets(getTweets());
	}

	private List<LonelyTweet> getTweets()
	{

		return tweets;
	}

	private void setTweets(List<LonelyTweet> tweets)
	{

		this.tweets = tweets;
	}

}