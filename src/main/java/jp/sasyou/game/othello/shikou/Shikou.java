package jp.sasyou.game.othello.shikou;

import jp.sasyou.game.othello.client.Mediator;
import jp.sasyou.game.othello.rule.Hand;

/**
 * 思考エンジンインタフェース
 *
 * @author sasyou
 *
 */
public interface Shikou extends Runnable {

	/**
	 * 次の一手を取得する。
	 *
	 * @return 手
	 */
	Hand getNextHand();

	/**
	 * メディエータをセットする。
	 *
	 * @param md メディエータ
	 */
	void attach(Mediator md);

	/**
	 * 手番を取得する。
	 *
	 * @return 手番
	 */
	int getTeban();

	/**
	 * 手番を設定する。
	 *
	 * @param teban 手番
	 */
	void setTeban(int teban);
}
