package jp.sasyou.game.othello.shikou.tejyun;

import java.util.Comparator;

/**
 * 手順を降順にソートするためのクラス
 *
 * @author sasyou
 *
 */
public final class TejyunDescComparator implements Comparator<Tejyun> {

	@Override
	public int compare(Tejyun tejyun1, Tejyun tejyun2) {
		int value1 = tejyun1.getValue();
		int value2 = tejyun2.getValue();

		if (value1 < value2) {
			return 1;
		} else if (value1 > value2) {
			return -1;
		} else {
			return 0;
		}
	}

}
