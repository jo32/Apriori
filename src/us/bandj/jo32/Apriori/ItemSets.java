package us.bandj.jo32.Apriori;

import java.util.Collections;
import java.util.List;

/***
 * an object stands for item sets
 */
public class ItemSets {
	List<ItemSet> itemSets = null;
	private int rank;

	protected ItemSets(List<ItemSet> itemSets) {
		Collections.sort(itemSets);
		this.setItemSets(itemSets);
		this.setRank(itemSets.get(0).getSize());
	}

	public List<ItemSet> getItemSets() {
		return itemSets;
	}

	private void setItemSets(List<ItemSet> itemSets) {
		this.itemSets = itemSets;
	}

	public int getSize() {
		return this.getItemSets().size();
	}

	public ItemSet get(int i) {
		return this.getItemSets().get(i);
	}

	public int getRank() {
		return rank;
	}

	private void setRank(int rank) {
		this.rank = rank;
	}

}
