package jp.sasyou.game.othello.shikou.tejyun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jp.sasyou.game.othello.rule.Hand;
import jp.sasyou.game.othello.rule.Piece;

/**
 * 手順のノード
 *
 * @author sasyou
 *
 */
public final class TejyunNode extends Tejyun {

	/** リストのサイズ */
	private static final int LIST_SIZE = 100;

	/** このノードから派生する手のリスト */
	private List<Tejyun> tejyunList = new ArrayList<Tejyun>(LIST_SIZE);

	/**
	 * コンストラクタ
	 *
	 * @param move 手
	 * @param piece 石の色
	 * @param depth 深さ
	 */
	public TejyunNode(long move, int piece, int depth) {
		this.move = move;
		this.piece = piece;
		this.depth = depth;
	}

	/**
	 * 手順を追加する。
	 *
	 * @param tejyun 手順
	 */
	public void addTejyun(Tejyun tejyun) {
		tejyunList.add(tejyun);
	}

	@Override
	public String viewAllTejyun() {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format(StringUtils.repeat("\t", depth)
				+ ReflectionToStringBuilder.toStringExclude(this, new String[]{"tejyunList"})));

		Hand hand = new Hand(move, piece);
		if (hand.getPiece() == Piece.BLACK) {
			Collections.sort(tejyunList, new TejyunAscComparator());
		} else if (hand.getPiece() == Piece.WHITE) {
			Collections.sort(tejyunList, new TejyunDescComparator());
		}

		for (Tejyun tejyun : tejyunList) {
			sb.append(tejyun.viewAllTejyun());
		}

		return sb.toString();
	}

	@Override
	public String viewSaizenTejyun() {
		StringBuilder sb = new StringBuilder();

		Hand hand = new Hand(move, piece);
		if (depth > 0) {
			sb.append(hand + ",");
		}

		if (tejyunList.size() > 0) {
			if (hand.getPiece() == Piece.BLACK) {
				Collections.sort(tejyunList, new TejyunAscComparator());
			} else if (hand.getPiece() == Piece.WHITE) {
				Collections.sort(tejyunList, new TejyunDescComparator());
			}

			sb.append(tejyunList.get(0).viewSaizenTejyun());
		}

		return sb.toString();
	}

	/**
	 * 評価値を設定する。
	 *
	 * @param value 評価値
	 */
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
