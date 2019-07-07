package brv.twitter.graphs;

import java.util.function.Supplier;

public enum TwitterUserGraphFactory {
	
	BFS(BfsTwitterUserGraph::new),
	DFS(DfsTwitterUserGraph::new);
	
	private Supplier<TwitterUserGraph> instantiator;
	
	private TwitterUserGraphFactory(Supplier<TwitterUserGraph> instantiator) {
		this.instantiator = instantiator;
	}
	
	public TwitterUserGraph getInstance() {
		return instantiator.get();
	}

}

