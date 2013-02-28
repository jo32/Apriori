package us.bandj.jo32.Apriori;

import java.util.LinkedList;
import java.util.List;

import us.bandj.jo32.Apriori.Exceptions.InvalidRuleException;

/***
 * an object stands for an rule
 */
public class Rule implements Comparable<Rule> {

	private ItemSet wholeItemSet = null;
	private ItemSet leftItemSet = null;

	protected Rule(ItemSet wholeItemSet, ItemSet leftItemSet)
			throws InvalidRuleException {
		if (!wholeItemSet.contains(leftItemSet)) {
			throw new InvalidRuleException("invalid rule");
		}
		this.setWholeItemSet(wholeItemSet);
		this.setLeftItemSet(leftItemSet);
	}

	@Override
	public String toString() {
		byte[] whole = this.getWholeItemSet().getItems();
		byte[] left = this.getLeftItemSet().getItems();
		String leftString = "";
		String rightString = "";
		int i;
		int j = 0;
		for (i = 0; i < whole.length; i++) {
			if (whole[i] == left[j]) {
				j++;
				leftString += whole[i] + " ";
			} else {
				rightString += whole[i] + " ";
			}
			if (j == left.length) {
				break;
			}
		}
		for (i = i + 1; i < whole.length; i++) {
			rightString += whole[i] + " ";
		}
		return leftString + "--> " + rightString;
	}

	@Override
	public int compareTo(Rule o) {
		return this.getLeftItemSet().compareTo(o.getLeftItemSet());
	}

	/***
	 * 
	 * generate an new ItemSet based on another rule.
	 * if given [1, 2] --> [3] and [1, 3] --> [2]
	 * returns: [1]
	 * 
	 * 
	 * @param o
	 * @return
	 */
	public ItemSet cross(Rule o) {
		if (this.getWholeItemSet() != o.getWholeItemSet()) {
			return null;
		}
		ItemSet a = this.getLeftItemSet();
		ItemSet b = o.getLeftItemSet();
		if (a.getSize() != b.getSize()) {
			return null;
		}

		// another LCS problem of two strings: simplest solution: O(mn) because
		// the size of the two object is too small.
		int size = 0;
		List<Byte> bytes = new LinkedList<Byte>();
		for (int i = 0; i < a.getSize(); i++) {
			for (int j = 0; j < b.getSize(); j++) {
				if (a.getItems()[i] == b.getItems()[j]) {
					size++;
					bytes.add(a.getItems()[i]);
				}
			}
		}

		if (size == a.getSize() - 1) {
			byte[] items = new byte[bytes.size()];
			for (int i = 0; i < bytes.size(); i++) {
				items[i] = bytes.get(i);
			}
			ItemSet itemSet = new ItemSet(items);
			return itemSet;
		} else {
			return null;
		}

	}

	public ItemSet getWholeItemSet() {
		return wholeItemSet;
	}

	public ItemSet getLeftItemSet() {
		return leftItemSet;
	}

	private void setWholeItemSet(ItemSet wholeItemSet) {
		this.wholeItemSet = wholeItemSet;
	}

	private void setLeftItemSet(ItemSet leftItemSet) {
		this.leftItemSet = leftItemSet;
	}

}
