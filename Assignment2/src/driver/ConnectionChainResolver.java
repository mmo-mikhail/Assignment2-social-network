/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package driver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author s3419069 (Mykhailo Muzyka)
 * Represents the helper class to find connection chains
 */
public class ConnectionChainResolver {

	/**
	 * initial person's name
	 */
	private String name1;
	
	/**
	 * destination person's name
	 */
	private String name2;
	
	/**
	 * all given connections
	 */
	private List<String[]> connections;

	public ConnectionChainResolver(String name1, String name2, List<String[]> connections) {
		this.name1 = name1;
		this.name2 = name2;
		this.connections = connections;
	}
	
	/**
	 * get connection chain
	 * @return connection chain
	 */
	public List<String> getChain() {
		if (!hasConnection(name1)
			|| !hasConnection(name2)) {
			//if person1 or person2 has no friends/connection at all
			return null;
		}
		
		List<String> chain = new ArrayList<>();
		chain.add(name1);
		
		List<List<String>> allChains = new ArrayList<>();
		findAllPossibleChains(name1, chain, allChains);
		
		if (allChains.size() == 0) return null; //no connection found
		List<String> shortestChain = findShortest(allChains);
		return shortestChain;
	}

	/**
	 * get shortest connection chain
	 * @return shortest chain
	 */
	private List<String> findShortest(List<List<String>> allChains) {
		List<String> shortest = allChains.get(0);
		for (List<String> chain : allChains) {
			if (chain.size() < shortest.size()) {
				//if we found smaller chain, good
				shortest = chain;
			}
		}
		return shortest;
	}

	/**
	 * finds all possible connections
	 */
	private void findAllPossibleChains(
			String personName,
			List<String> currentPath,
			List<List<String>> allChains) {
		//walk though all friends and their friends
		List<String> friends = getAllFriendsOfPerson(personName);
		//exclude elements we visited already
		friends.removeAll(currentPath);
		for (String friend : friends) {
			//remember current path
			currentPath.add(friend);
			if (friend.equals(name2)) {
				//cool, we reach the end, we found connection chain!
				//and copy it to be unmodifiable later
				allChains.add(new ArrayList<String>(currentPath));
				return;
			}
			findAllPossibleChains(friend, currentPath, allChains);
		}
	}
	
	/**
	 * determine if person has connection
	 * @return true if person has connection
	 */
	private boolean hasConnection(String name) {
		return connections.stream()
				.filter(c -> c[0].equals(name) || c[1].equals(name))
				.findFirst().isPresent();
	}
	
	/**
	 * helper
	 * get all friends of person
	 * @return connection element
	 */
	private List<String> getAllFriendsOfPerson(String name) {
		List<String> allFriends = new ArrayList<>();

		connections.stream()
			.filter(c -> c[0].equals(name) || c[1].equals(name)).forEach(e -> {
				allFriends.add(e[0].equals(name) ? e[1] : e[0]);
		});
		return allFriends;
	}
}
