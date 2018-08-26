package jp.sasyou.game.othello.shikou;

import jp.sasyou.game.othello.rule.Board;

/**
 * 評価エンジンインタフェース
 *
 * @author sasyou
 *
 */
public interface Evaluator {

	/**
	 * 盤面を評価する。
	 *
	 * @param board 盤面
	 * @param teban 手番
	 * @return 評価値
	 */
	int evaluate(Board board, int teban);
}
