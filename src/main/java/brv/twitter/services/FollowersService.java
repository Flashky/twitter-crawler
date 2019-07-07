package brv.twitter.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import brv.twitter.graphs.TwitterUserGraph;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@Service
public class FollowersService {

	private static final Logger LOGGER = LogManager.getLogger(FollowersService.class.getName());
	
	@Autowired
	private Twitter twitter;
	
	@Autowired
	private TwitterUserGraph twitterUserGraph;
	
	public void getFollowers(String startingUser) {
	
		String screenName = startingUser;
		try {
			
			if(StringUtils.isEmpty(screenName)) {
					screenName = twitter.getScreenName();
			}
		
			LOGGER.info("Start to obtain followers - Starting screenName: " + screenName);
	
			twitterUserGraph.traverse(screenName);
	
			LOGGER.info("No more followers to obtain");
			
		} catch (IllegalArgumentException e) {
			LOGGER.error("Error (FollowersService): "+e.getMessage());
		} catch (TwitterException e) {
			LOGGER.error("Error (FollowersService): "+e.getErrorMessage());
		}
		

	}

}
