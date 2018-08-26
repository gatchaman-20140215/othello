package jp.sasyou.game.othello.shikou;

import jp.sasyou.game.othello.rule.Board;
import jp.sasyou.game.othello.rule.HandGenerator;
import jp.sasyou.game.othello.rule.Piece;

/**
 * 盤面の位置の評価に加え、終盤度による評価を加味した評価エンジン
 *
 * @author sasyou
 *
 */
public final class KyokumenEvaluator implements Evaluator {
	/** 中盤の始まる手数 */
	private static final int MIDDLE_NUM = 10;
	/** 終盤の始まる手数 */
	private static final int FINISH_NUM = 48;

	@Override
	public int evaluate(Board board, int teban) {
		int eval = 0;
		Evaluator evaluator = new PositionEvaluator();
		eval = evaluator.evaluate(board, teban);

		if (board.getNumber() <= MIDDLE_NUM) {
			eval += evaluateByDropDownNum(board, teban);
		} else if (board.getNumber() <= FINISH_NUM) {
			eval += evaluateByDropDownNum(board, teban);
		}
		return eval;
	}

	/**
	 * 打てる手がいくつあるか取得する。
	 *
	 * @param board 盤面
	 * @param piece 石
	 * @return 評価値
	 */
	private int evaluateByDropDownNum(Board board, int piece) {
		final int factor = 3;
		return HandGenerator.countHands(board, piece) * factor
				* (piece == Piece.BLACK ? 1 : -1);
	}

	/**
	 * 確定石を数え、評価する。
	 *
	 * @param board 盤面
	 * @param piece 石の色
	 * @return 評価値
	 */
	private int evaluateEdge(Board board, int piece) {
		int count = 0;
		byte rightEdge = (byte) ~board.getRightEdge(piece);
		count += ((rightEdge & (-rightEdge)) - 1);
		byte leftEdge = (byte) ~board.getLeftEdge(piece);
		count += ((leftEdge & (-leftEdge)) - 1);
		byte upperEdge = (byte) ~board.getUpperEdge(piece);
		count += ((upperEdge & (-upperEdge)) - 1);
		byte lowerEdge = (byte) ~board.getLowerEdge(piece);
		count += ((lowerEdge & (-lowerEdge)) - 1);

		long corners = board.getBanmen()[piece] & 0x8100000000000081L;
		count -= Long.bitCount(corners);

		return count;
	}
}
