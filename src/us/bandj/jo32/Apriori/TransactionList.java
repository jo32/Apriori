package us.bandj.jo32.Apriori;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionList {
	List<Transaction> transactions = null;

	protected TransactionList(List<Transaction> transactions) {
		this.setTransactions(transactions);
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	private void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public int getSize() {
		return this.getTransactions().size();
	}

	/***
	 * 
	 * no use now.
	 * 
	 * @param itemSet
	 * @return
	 */
	public double computeSupport(ItemSet itemSet) {
		int support = 0;
		ArrayList<Integer> appearences = new ArrayList<Integer>();
		for (int i = 0; i < this.getSize(); i++) {
			Transaction transaction = this.getTransactions().get(i);
			if (transaction.contains(itemSet)) {
				support += 1;
				appearences.add(i);
			}
		}
		itemSet.setSupport((double) support / this.getSize());
		Integer[] _appearences = new Integer[appearences.size()];
		appearences.toArray(_appearences);
		itemSet.setAppearences(_appearences);
		return itemSet.getSupport();
	}

	/***
	 * 
	 * use lcs implemented in dynamic programming to compute support.
	 * 
	 * @param itemSet
	 * @param father
	 * @param mother
	 * @return
	 */

	public double computeSupport(ItemSet itemSet, ItemSet father, ItemSet mother) {
		Integer[] appearences = TransactionList.lcs(father.getAppearences(),
				mother.getAppearences());
		int count = appearences.length;
		double support = ((double) count) / this.getSize();
		itemSet.setSupport(support);
		itemSet.setAppearences(appearences);
		return support;
	}

	// public static Integer[] lcs(Integer[] a, Integer[] b) {
	// List<Integer> lcs = new ArrayList<Integer>();
	// for (int i = 0; i < a.length; i++) {
	// for (int j = 0; j < b.length; j++) {
	// if (a[i].intValue() == b[j].intValue()) {
	// lcs.add(a[i]);
	// }
	// }
	// }
	// Integer[] _out = new Integer[lcs.size()];
	// return lcs.toArray(_out);
	// }

	public static Integer[] lcs(Integer[] a, Integer[] b) {
		int[][] lengths = new int[a.length + 1][b.length + 1];

		// row 0 and column 0 are initialized to 0 already

		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < b.length; j++)
				if (a[i].intValue() == b[j].intValue())
					lengths[i + 1][j + 1] = lengths[i][j] + 1;
				else
					lengths[i + 1][j + 1] = Math.max(lengths[i + 1][j],
							lengths[i][j + 1]);

		// read the substring out from the matrix
		ArrayList<Integer> out = new ArrayList<Integer>();
		for (int x = a.length, y = b.length; x != 0 && y != 0;) {
			if (lengths[x][y] == lengths[x - 1][y])
				x--;
			else if (lengths[x][y] == lengths[x][y - 1])
				y--;
			else {
				assert a[x - 1].intValue() == b[y - 1].intValue();
				out.add(a[x - 1]);
				x--;
				y--;
			}
		}

		Collections.reverse(out);
		Integer[] _out = new Integer[out.size()];
		return out.toArray(_out);
	}

	// public static void main(String[] args) {
	// Integer[] a = {7, 10, 14, 15, 23, 27, 41, 44, 48, 58, 61, 62, 66, 69, 72,
	// 74, 101, 111, 120, 121, 122, 127, 135, 146, 149, 156, 161, 172, 174, 185,
	// 188, 196, 201, 223, 224, 227, 230, 233, 243, 252, 257, 260, 263, 277,
	// 291, 300, 302, 307, 315, 323, 335, 336, 344, 347, 356, 387, 389, 393,
	// 402, 406, 409, 426, 430, 438, 442, 447, 448, 462, 471, 476, 484, 486,
	// 496, 510, 511, 519, 521, 524, 527, 541, 550, 556, 559, 560, 567, 584,
	// 586, 588, 595, 598, 603, 606, 623, 625, 631, 636, 639, 653, 663, 670,
	// 671, 672, 673, 678, 688, 697, 705, 723, 726, 730, 739, 741, 752, 754,
	// 761, 764, 773, 780, 781, 795, 798, 799, 801, 809, 811, 816, 825, 841,
	// 844, 848, 850, 853, 855, 858, 867, 872, 875, 880, 910, 914, 917, 918,
	// 932, 938, 950, 952, 965, 969, 973, 977, 979, 986, 991, 994, 997, 999};
	// Integer[] b = {7, 10, 14, 15, 23, 27, 34, 44, 48, 58, 62, 66, 69, 74, 81,
	// 101, 111, 120, 122, 127, 135, 146, 149, 161, 172, 174, 183, 185, 188,
	// 196, 213, 218, 224, 227, 230, 233, 242, 243, 244, 245, 247, 252, 257,
	// 260, 277, 291, 294, 300, 302, 307, 323, 328, 336, 344, 345, 347, 356,
	// 368, 387, 389, 393, 402, 406, 408, 409, 425, 438, 447, 448, 451, 462,
	// 467, 469, 471, 496, 510, 511, 518, 519, 521, 524, 527, 541, 550, 556,
	// 559, 560, 567, 578, 581, 584, 586, 588, 595, 603, 606, 623, 625, 627,
	// 631, 636, 639, 653, 663, 670, 671, 673, 678, 688, 697, 705, 720, 723,
	// 725, 726, 730, 739, 744, 752, 753, 754, 764, 770, 771, 773, 774, 780,
	// 798, 799, 801, 803, 809, 811, 814, 816, 841, 844, 848, 850, 851, 855,
	// 858, 875, 880, 910, 914, 917, 922, 932, 950, 960, 965, 969, 970, 973,
	// 977, 979, 986, 991, 994, 997, 999};
	// Integer[] c = TransactionList.lcs(a, b);
	// System.out.println("OK");
	// }
}
