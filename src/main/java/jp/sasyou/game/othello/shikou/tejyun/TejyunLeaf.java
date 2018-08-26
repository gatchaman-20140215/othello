package jp.sasyou.game.othello.shikou.tejyun;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jp.sasyou.game.othello.rule.Hand;

/**
 * 手順のリーフ
 *
 * @author sasyou
 *
 */
public final class TejyunLeaf extends Tejyun {

	/**
	 * コンストラクタ
	 *
	 * @param move 手
	 * @param piece 石の色
	 * @param value 評価値
	 * @param depth 深さ
	 */
	public TejyunLeaf(long move, int piece, int value, int depth) {
		this.move = move;
		this.piece = piece;
		this.value = value;
		this.depth = depth;
	}

	@Override
	public String viewAllTejyun() {
		return String.format(StringUtils.repeat("\t", depth))
				+ ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public String viewSaizenTejyun() {
		return String.format("%s,評価値=%d", new Hand(move, piece), value);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
