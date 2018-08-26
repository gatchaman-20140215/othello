package jp.sasyou.game.othello.shikou;

import jp.sasyou.game.othello.rule.Board;
import jp.sasyou.game.othello.rule.Piece;

/**
 * 最終局面を評価するための評価エンジン
 *
 * @author sashou
 *
 */
public class PerfectEvaluator implements Evaluator {

	@Override
	public final int evaluate(Board board, int teban) {
		return (board.getNumberOfBlack() - board.getNumberOfWhite())
				* (teban == Piece.BLACK ? 1 : -1);
	}

}
