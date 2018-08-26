package jp.sasyou.game.othello.shikou;

import jp.sasyou.game.othello.rule.Board;

import static jp.sasyou.game.othello.rule.OthelloRule.*;

/**
 * 盤面の位置で評価を与える評価エンジン
 *
 * @author sasyou
 *
 */
public final class PositionEvaluator implements Evaluator {
	/** マスごとの価値 */
	private static final int[] VALUE_TABLE = {
		120, -20,  20,   5,   5,  20, -20, 120,
		-20, -40,  -5,  -5,  -5,  -5, -40, -20,
		 20,  -5,  15,   3,   3,  15,  -5,  20,
		  5,  -5,   3,   3,   3,   3,  -5,   5,
		  5,  -5,   3,   3,   3,   3,  -5,   5,
		 20,  -5,  15,   3,   3,  15,  -5,  20,
		-20, -40,  -5,  -5,  -5,  -5, -40, -20,
		120, -20,  20,   5,   5,  20, -20, 120
	};

	@Override
	public int evaluate(Board board, int teban) {
		int eval = 0;
		long[] banmen = board.getBanmen();
		long mask = 1;
		for (int i = 0; i < Board.BOARD_SIZE * Board.BOARD_SIZE; i++) {
			if ((banmen[teban] & mask) != 0) {
				eval += VALUE_TABLE[i];
			} else if ((banmen[teban ^ 1] & mask) != 0) {
				eval -= VALUE_TABLE[i];
			}
			mask = mask << 1;
		}
		return eval;
	}

}
