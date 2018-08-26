package jp.sasyou.game.othello.shikou;

import jp.sasyou.game.othello.client.Mediator;
import jp.sasyou.game.othello.rule.Board;

/**
 * 思考エンジンの基底クラス
 *
 * @author sasyou
 *
 */
public abstract class ShikouBasic implements Shikou {

	/** 盤面 */
	protected Board board;
	/** 手番 */
	protected int teban;
	/** メディエータ */
	protected Mediator md;

	/**
	 * デフォルト・コンストラクタ
	 */
	public ShikouBasic() {

	}

	/**
	 * コンストラクタ
	 *
	 * @param board 盤面
	 * @param teban 手番
	 */
	public ShikouBasic(Board board, int teban) {
		this.board = board;
		this.teban = teban;
	}

	/**
	 * メディエータを設定する。
	 *
	 * @param md メディエータ
	 */
	public final void attach(Mediator md) {
		this.md = md;
	}

	/**
	 * 思考を行う。
	 */
	public abstract void run();

	/**
	 * 盤面
	 *
	 * @param board 盤面
	 */
	public final void setBoard(Board board) {
	    this.board = board;
	}

	/**
	 * 手番を取得する。
	 *
	 * @return 手番
	 */
	public final int getTeban() {
		return teban;
	}

	/**
	 * 手番
	 *
	 * @param teban 手番
	 */
	public final void setTeban(int teban) {
	    this.teban = teban;
	}
}
