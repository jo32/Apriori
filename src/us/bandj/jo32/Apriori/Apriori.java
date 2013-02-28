package us.bandj.jo32.Apriori;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import us.bandj.jo32.Apriori.Exceptions.InvalidRuleException;
import us.bandj.jo32.Apriori.Exceptions.NoRequiredItemSetException;
import us.bandj.jo32.Apriori.Exceptions.OutOfRangeException;

public class Apriori {

	private String filePath = null;
	private TransactionList transactionList = null;
	private Set<Integer> universeSet = null;

	private double minimunSupport;
	private double minimumConfidence;

	private List<ItemSets> generations = null;
	private List<Rule> rules = new ArrayList<Rule>();

	/***
	 * 
	 * @param filePath
	 *            the file path of the csv file
	 * @param minimumSupport
	 *            the minimum support
	 * @param minimumConfidence
	 *            the minimum confidence
	 * 
	 */
	public Apriori(String filePath, double minimumSupport,
			double minimumConfidence) {
		this.setFilePath(filePath);
		this.setMinimumConfidence(minimumConfidence);
		this.setMinimunSupport(minimumSupport);
	}

	/***
	 * 
	 * the whole flow of this algorithm.<br />
	 * ATTENTION:<br />
	 * this app implemented based on the CSV file given, too much transactions
	 * or too much transaction lenght will lead to exceptions.
	 * 
	 * @throws IOException
	 * @throws OutOfRangeException
	 * @throws InvalidRuleException
	 * @throws NoRequiredItemSetException
	 */
	@SuppressWarnings("resource")
	public void load() throws IOException, OutOfRangeException,
			InvalidRuleException, NoRequiredItemSetException {
		InputStream fis = new FileInputStream(this.getFilePath());
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		List<Transaction> transactions = new ArrayList<Transaction>();
		String line;
		Set<Integer> universeSet = new HashSet<Integer>();
		Map<Integer, List<Integer>> appearence_stat = new HashMap<Integer, List<Integer>>();
		while ((line = br.readLine()) != null) {
			line = line.replace(",", " ");
			Scanner scanner = new Scanner(line);
			int id = scanner.nextInt();
			if (id < 0 || id > 1000) {
				throw new OutOfRangeException("The id is out of Range!");
			}
			List<Integer> _transaction = new ArrayList<Integer>();
			int item;
			while (scanner.hasNextInt()) {
				item = scanner.nextInt();
				if (!universeSet.contains(item)) {
					List<Integer> appearence = new ArrayList<Integer>();
					appearence_stat.put(item, appearence);
				}
				appearence_stat.get(item).add(id);
				if (item < 0 || item > 50) {
					throw new OutOfRangeException("The item is out of Range!");
				}
				_transaction.add(item);
				universeSet.add(item);
			}
			transactions.add(new Transaction(id, _transaction));
		}
		TransactionList transactionList = new TransactionList(transactions);
		this.setTransactionList(transactionList);
		this.setUniverseSet(universeSet);
		this.setGenerations(new ArrayList<ItemSets>());
		ItemSets temp = this.getLengthOneItemSets(universeSet, appearence_stat);
		this.getGenerations().add(temp);
		while (temp.getSize() > 1) {
			temp = this.growItemSets(temp);
			this.getGenerations().add(temp);
		}
		this.computeRules();
	}

	/***
	 * 
	 * get lenght one item sets, for example: transactions: [1, 2] [1] returns:
	 * items sets: [1] [2]
	 * 
	 * @param universeSet
	 *            the universe set
	 * @param appearence_stat
	 *            the appearence statistics of size one item set.
	 * @return lenght one item sets
	 * @throws NoRequiredItemSetException
	 *             throw if no item set meet minimum support
	 */
	private ItemSets getLengthOneItemSets(Set<Integer> universeSet,
			Map<Integer, List<Integer>> appearence_stat)
			throws NoRequiredItemSetException {
		List<ItemSet> itemSets = new ArrayList<ItemSet>();
		for (int item : this.getUniverseSet()) {
			List<Integer> appearences = appearence_stat.get(item);
			byte[] items = { new Integer(item).byteValue() };
			ItemSet itemSet = new ItemSet(items);
			Integer[] _appearences = new Integer[appearences.size()];
			appearences.toArray(_appearences);
			itemSet.setAppearences(_appearences);
			double support = ((double) _appearences.length)
					/ this.getTransactionList().getSize();
			itemSet.setSupport(support);
			if (support > this.getMinimumSupport()) {
				itemSets.add(itemSet);
			}
		}
		if (itemSets.size() < 1) {
			throw new NoRequiredItemSetException(
					"No required itemset satisfying the minimum support.");
		}
		return new ItemSets(itemSets);
	}

	/***
	 * 
	 * getting the item set list based on two item sets whose first order - 1
	 * items are the same.
	 * 
	 * although the worse performace of this function is O(n^2). however, we
	 * think the expectation is O(kn) due to the item sets are sorted.
	 * 
	 * the support of item set is caculated based on largest common substring
	 * implemented in dynamic programming O(nlogn)
	 ***/

	/***
	 * 
	 * @param itemSets
	 *            the item sets needed to be grow, for example:<br />
	 *            if given: [1], [2]; returns:<br />
	 *            [1, 2]
	 * @return
	 */
	private ItemSets growItemSets(ItemSets itemSets) {
		List<ItemSet> _itemSets = new ArrayList<ItemSet>();
		int i = 0;
		int k = 1;
		while (k < itemSets.getSize()) {
			int result = itemSets.get(i).compareTo(itemSets.get(k));
			while (Math.abs(result) == itemSets.getRank()) {
				ItemSet temp = itemSets.get(i).cross(itemSets.get(k));
				double support;
				// the condition is always false now:
				if (temp.getSize() <= 1) {
					support = this.getTransactionList().computeSupport(temp);
				} else {
					support = this.getTransactionList().computeSupport(temp,
							itemSets.get(i), itemSets.get(k));
				}
				if (support >= this.getMinimumSupport()) {
					_itemSets.add(temp);
				}
				k++;
				if (k == itemSets.getSize()) {
					break;
				}
				result = itemSets.get(i).compareTo(itemSets.get(k));
			}
			for (int l = i + 1; l < k; l++) {
				for (int m = l + 1; m < k; m++) {
					ItemSet temp = itemSets.get(l).cross(itemSets.get(m));
					double support;
					// the condition is always false now:
					if (temp.getSize() <= 1) {
						support = this.getTransactionList()
								.computeSupport(temp);
					} else {
						support = this.getTransactionList().computeSupport(
								temp, itemSets.get(l), itemSets.get(m));
					}
					if (support >= this.getMinimumSupport()) {
						_itemSets.add(temp);
					}
				}
			}
			i = k;
			k = i + 1;
		}
		if (itemSets.getSize() > 0) {
			return new ItemSets(_itemSets);
		} else {
			return null;
		}
	}

	/***
	 * as our item sets are sorted, we can get a item set based on binary
	 * search.
	 */
	private ItemSet getItemSetMeetsSupport(ItemSet itemSet) {
		int rank = itemSet.getSize();
		if (rank > this.getGenerations().size()) {
			return null;
		}
		List<ItemSet> sets = this.getGenerations().get(rank - 1).getItemSets();
		int result = Collections.binarySearch(sets, itemSet);
		if (result > -1) {
			return sets.get(result);
		} else {
			return null;
		}
	}

	private double getSupport(ItemSet itemSet) {
		ItemSet temp = this.getItemSetMeetsSupport(itemSet);
		if (temp != null) {
			return temp.getSupport();
		} else {
			return 0;
		}
	}

	/***
	 * get the rules based on frequent item sets
	 * 
	 * @throws InvalidRuleException
	 */
	private void computeRules() throws InvalidRuleException {
		for (int i = 1; i < this.getGenerations().size(); i++) {
			List<ItemSet> sets = this.getGenerations().get(i).getItemSets();
			for (ItemSet itemSet : sets) {
				this.searchRules(itemSet);
			}
		}
	}

	/***
	 * search rules based on an frequent item sets
	 * 
	 * @param itemSet
	 * @throws InvalidRuleException
	 */
	private void searchRules(ItemSet itemSet) throws InvalidRuleException {
		List<Rule> rules = this.getLevelOneRules(itemSet);
		while (rules.size() >= 1) {
			this.getRules().addAll(rules);
			rules = this.growLevelByRules(rules);
		}
	}

	/***
	 * get the rules level by level.
	 * 
	 * @param rules
	 * @return
	 * @throws InvalidRuleException
	 */
	private List<Rule> growLevelByRules(List<Rule> rules)
			throws InvalidRuleException {
		List<Rule> newRules = new LinkedList<Rule>();
		if (rules.get(0).getLeftItemSet().getSize() == 1) {
			return newRules;
		}
		for (int i = 0; i < rules.size(); i++) {
			for (int j = i + 1; j < rules.size(); j++) {
				ItemSet crossResult = rules.get(i).cross(rules.get(j));
				if (crossResult != null
						&& (crossResult = this
								.getItemSetMeetsSupport(crossResult)) != null) {
					if (rules.get(i).getWholeItemSet().getSupport()
							/ crossResult.getSupport() >= this
								.getMinimumConfidence()) {
						Rule _rule = new Rule(rules.get(i).getWholeItemSet(),
								crossResult);
						newRules.add(_rule);
					}
				}
			}
		}
		return newRules;
	}

	/*** get the level one rule
	 * 
	 * @param itemSet
	 * @return
	 * @throws InvalidRuleException
	 */
	private List<Rule> getLevelOneRules(ItemSet itemSet)
			throws InvalidRuleException {
		List<Rule> levelOneRules = new ArrayList<Rule>();
		for (int i = 0; i < itemSet.getSize(); i++) {
			byte[] subItems = new byte[itemSet.getSize() - 1];
			System.arraycopy(itemSet.getItems(), 0, subItems, 0, i);
			System.arraycopy(itemSet.getItems(), i + 1, subItems, i,
					itemSet.getSize() - i - 1);
			ItemSet temp = new ItemSet(subItems);
			temp = this.getItemSetMeetsSupport(temp);
			if (temp != null
					&& this.getSupport(itemSet) / this.getSupport(temp) >= this
							.getMinimumConfidence()) {
				levelOneRules.add(new Rule(itemSet, temp));
			}
		}
		return levelOneRules;
	}

	public TransactionList getTransactionList() {
		return transactionList;
	}

	private void setTransactionList(TransactionList transactionList) {
		this.transactionList = transactionList;
	}

	public Set<Integer> getUniverseSet() {
		return universeSet;
	}

	private void setUniverseSet(Set<Integer> universeSet) {
		this.universeSet = universeSet;
	}

	public String getFilePath() {
		return filePath;
	}

	private void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public double getMinimumSupport() {
		return minimunSupport;
	}

	private void setMinimunSupport(double minimunSupport) {
		this.minimunSupport = minimunSupport;
	}

	public double getMinimumConfidence() {
		return minimumConfidence;
	}

	private void setMinimumConfidence(double minimumConfidence) {
		this.minimumConfidence = minimumConfidence;
	}

	public List<ItemSets> getGenerations() {
		return generations;
	}

	private void setGenerations(List<ItemSets> generations) {
		this.generations = generations;
	}

	public List<Rule> getRules() {
		return rules;
	}

}
