package us.bandj.jo32.Apriori;

import java.util.Collections;
import java.util.List;

public class Transaction {
	private short id;
	private byte length;
	private byte[] transaction;

	protected Transaction(int id, List<Integer> transaction) {
		Collections.sort(transaction);
		this.setTransaction(new byte[transaction.size()]);
		this.setLength(new Integer(transaction.size()).byteValue());
		this.setId((short) id);
		for (int i = 0; i < transaction.size(); i++) {
			this.getTransaction()[i] = transaction.get(i).byteValue();
		}
	}

	/***
	 * 
	 * test if a transaction contains an itemset, no use now.
	 * 
	 * @param itemSet
	 * @return
	 */
	public boolean contains(ItemSet itemSet) {
		int i;
		int j = 0;
		boolean flag = false;
		for (i = 0; i < this.getTransaction().length; i++) {
			if (this.getTransaction()[i] == itemSet.getItems()[j]) {
				j++;
			}
			if (j == itemSet.getSize()) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public byte getLength() {
		return length;
	}

	private void setLength(byte length) {
		this.length = length;
	}

	public byte[] getTransaction() {
		return transaction;
	}

	private void setTransaction(byte[] transaction) {
		this.transaction = transaction;
	}

	public short getId() {
		return id;
	}

	private void setId(short id) {
		this.id = id;
	}

	public int getSize() {
		return this.getTransaction().length;
	}

}
