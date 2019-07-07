package brv.twitter.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import brv.twitter.graphs.TwitterUserGraph;
import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

@Service
public class FollowersService {

	private static final Logger LOGGER = LogManager.getLogger(FollowersService.class.getName());
	
	@Autowired
	private TwitterUserGraph followersTraversal;
	
	public void getFollowers() {
	
		LOGGER.info("Start to obtain followers");

		// Cosas útiles:
		// http://twitter4j.org/javadoc/twitter4j/TwitterBase.html
		// --> addRateLimitStatusListener
		// http://twitter4j.org/javadoc/twitter4j/RateLimitStatusListener.html
		// --> RateLimitStatusListener
		// Estos métodos me permitirían controlar cuando llego al rate limit.
		
		followersTraversal.traverse("packtfreebook");

		LOGGER.info("No more followers to obtain");
	}

}
