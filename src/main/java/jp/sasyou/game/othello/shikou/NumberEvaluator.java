package jp.sasyou.game.othello.shikou;

import jp.sasyou.game.othello.rule.Board;
import jp.sasyou.game.othello.rule.Piece;

/**
 * 石の数による局面評価エンジン
 *
 * @author sasyou
 *
 */
public final class NumberEvaluator implements Evaluator {

	@Override
	public int evaluate(Board board, int teban) {
		if (teban == Piece.BLACK) {
			return board.getNumberOfBlack();
		} else if (teban == Piece.WHITE) {
			return board.getNumberOfWhite();
		} else {
			return 0;
		}
	}

}
