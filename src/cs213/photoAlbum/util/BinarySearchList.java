/**
 * @author Victor Kaiser-Pendergrast
 * @author Rajyashri Vasudevamoorthy
 */

package cs213.photoAlbum.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;



public class BinarySearchList<E extends Comparable<E>> extends ArrayList<E> implements Serializable {

	static final long serialVersionUID = 2847843;

	/**
	 * Add an item to this BinarySearchList
	 * @return true if item was inserted, false if item already existed
	 */
	@Override
	public boolean add(E item) {
		int index = binarySearch(item);
		if(index >= 0) {
			return false;
		} 

		index = (index + 1) * -1;
		super.add(index, item);

		return true;
	}

	@Override
	public boolean contains(Object item) {
		if(item instanceof Comparable<?>) {
			return binarySearch((E) item) >= 0;
		}
		return false;
	}


	/**
	 * Do a binary search for the item
	 * @return index of item if exists, (- (index to insert) - 1) otherwise
	 */
	private int binarySearch(E item) {
		return Collections.binarySearch(this, item);
	}

}
