package us.bandj.jo32.Apriori;

/***
 * an object stands for an item set.
 */
public class ItemSet implements Comparable<ItemSet> {
	private double support;
	private byte[] items;

	private Integer[] appearences;

	protected ItemSet(byte[] items) {
		this.setItems(items);
	}

	public double getSupport() {
		return support;
	}

	protected void setSupport(double support) {
		this.support = support;
	}

	public byte[] getItems() {
		return items;
	}

	private void setItems(byte[] items) {
		this.items = items;
	}

	public int getSize() {
		return this.getItems().length;
	}

	public Integer[] getAppearences() {
		return appearences;
	}

	public void setAppearences(Integer[] appearences) {
		this.appearences = appearences;
	}

	/***
	 * 
	 * if given: [1, 2], [1, 3]: return [1, 2, 3] <br />
	 * if given: [1 ,2], [2, 3]: return null
	 * 
	 * @param o
	 * @return
	 */
	public ItemSet cross(ItemSet o) {
		byte[] a;
		byte[] b;
		int result = this.compareTo(o);
		if (this.getSize() == o.getSize() && Math.abs(result) == this.getSize()) {
			if (result <= 0) {
				a = this.getItems();
				b = o.getItems();
			} else {
				a = o.getItems();
				b = this.getItems();
			}
			byte[] items = new byte[this.getSize() + 1];
			for (int i = 0; i < this.getSize(); i++) {
				items[i] = a[i];
			}
			items[this.getSize()] = b[this.getSize() - 1];
			return new ItemSet(items);
		} else {
			return null;
		}
	}

	/***
	 * 
	 * test if the item set contains another item set.
	 * 
	 * @param o
	 * @return
	 */
	public boolean contains(ItemSet o) {
		int i;
		int j = 0;
		boolean flag = false;
		for (i = 0; i < this.getSize(); i++) {
			if (this.getItems()[i] == o.getItems()[j]) {
				j++;
			}
			if (j == o.getSize()) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/***
	 * test if two item set is equal, the absolute value of the return is the
	 * difference index (needed in other functions).
	 */
	@Override
	public int compareTo(ItemSet o) {
		int a = this.getSize();
		int b = o.getSize();
		int length;
		if (a <= b) {
			length = a;
		} else {
			length = b;
		}
		int i;
		for (i = 0; i < length; i++) {
			if (this.getItems()[i] != o.getItems()[i]) {
				break;
			}
		}
		if (i >= length) {
			if (a < b) {
				return -(i + 1);
			} else if (a > b) {
				return (i + 1);
			} else {
				return 0;
			}
		} else {
			if (this.getItems()[i] < o.getItems()[i]) {
				return -(i + 1);
			} else if (this.getItems()[i] == o.getItems()[i]) {
				return 0;
			} else {
				return (i + 1);
			}
		}
	}

	@Override
	public String toString() {
		String s = "[";
		for (int i = 0; i < this.getItems().length; i++) {
			s += new Byte(this.getItems()[i]).intValue() + ", ";
		}
		s += "]";
		return s;
	}

}
