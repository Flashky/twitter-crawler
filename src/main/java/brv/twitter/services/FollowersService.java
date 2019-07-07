package brv.twitter.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import brv.twitter.graphs.TwitterUserGraph;

@Service
public class FollowersService {

	private static final Logger LOGGER = LogManager.getLogger(FollowersService.class.getName());
	
	@Autowired
	private TwitterUserGraph twitterUserGraph;
	
	public void getFollowers() {
	
		LOGGER.info("Start to obtain followers");

		twitterUserGraph.traverse("packtfreebook");

		LOGGER.info("No more followers to obtain");
	}

}
