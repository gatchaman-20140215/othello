package jp.sasyou.game.othello.shikou.tejyun;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 手順を格納するためのクラスの基底クラス
 *
 * @author sasyou
 *
 */
public abstract class Tejyun {

	/** 手 */
	protected long move;
	/** 石の色 */
	protected int piece;
	/** 評価値 */
	protected int value;
	/** 深さ */
	protected int depth;

	/**
	 * コンストラクタ
	 */
	public Tejyun() {
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * 全ての手順を文字列化する。
	 *
	 * @return 文字列化した全手順
	 */
	public abstract String viewAllTejyun();

	/**
	 * 最善手順を文字列化する。
	 *
	 * @return 文字列化した最善手順
	 */
	public abstract String viewSaizenTejyun();

	/**
	 * 評価値を取得する。
	 * @return 評価値
	 */
	public final int getValue() {
	    return value;
	}

	/**
	 * 深さを取得する。
	 * @return 深さ
	 */
	public final int getDepth() {
	    return depth;
	}
}
