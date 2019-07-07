package brv.twitter.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import brv.twitter.graphs.DfsTwitterUserGraph;
import brv.twitter.graphs.TwitterUserGraph;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;

@Component
public class GetFollowersRateLimitStatusListener implements RateLimitStatusListener {

	private static final Logger LOGGER = LogManager.getLogger(GetFollowersRateLimitStatusListener.class.getName());
	
	@Override
	public void onRateLimitStatus(RateLimitStatusEvent event) {

	}

	@Override
	public void onRateLimitReached(RateLimitStatusEvent event) {

//		RateLimitStatus status = event.getRateLimitStatus();

//		long secondsUntilReset = status.getSecondsUntilReset();


	}
	

}
