package jp.sasyou.game.othello.shikou;

import jp.sasyou.game.othello.rule.Board;
import jp.sasyou.game.othello.rule.Piece;

/**
 * 中盤用評価エンジン
 *
 * @author sasyou
 *
 */
public final class MidEvaluator implements Evaluator {
	/** 着手可能手数の係数 */
	private static final int MOBILITY_W = 67;
	/** 開放度の係数 */
	private static final int LIBERTY_W = -13;
	/** 確定石数の係数 */
	private static final int FIXED_W = 101;
	/** ウイングの係数 */
	private static final int WING_W = -308;
	/** X打ちの係数 */
	private static final int X_MOVE_W = -449;
	/** C打ちの係数 */
	private static final int C_MOVE_W = -552;

	@Override
	public int evaluate(Board board, int teban) {
		// 着手可能手数を取得する。
		int movable = Long.bitCount(board.generateLegals(teban));
		// 開放度を取得する。
		int libertySelf = countLiberty(board, teban);
		int libertyEnemy = countLiberty(board, teban ^ 1);
		// ウイングの数を数える。
		int wingSelf = countWing(board, teban);
		int wingEnemy = countWing(board, teban ^ 1);
		// 確定石の個数を数える。
		int fixedPieceSelf = countFixedPiece(board, teban);
		int fixedPieceEnemy = countFixedPiece(board, teban ^ 1);
		// C打ちの個数を数える。
		int cMoveSelf = countCmove(board, teban);
		int cMoveEnemy = countCmove(board, teban ^ 1);
		// X打ちの個数を数える。
		int xMoveSelf = countXmove(board, teban);
		int xMoveEnemy = countXmove(board, teban ^ 1);

//		System.out.printf("movable:%d, libertySelf:%d, libertyEnemy:%d, wingSelf:%d, wingEnemy:%d, "
//				+ "fixedSelf:%d, fixedEnemy:%d, cMoveSelf:%d, cMoveEnemy:%d, xMoveSelf:%d, xMoveEnemy:%d\n",
//				movable, libertySelf, libertyEnemy, wingSelf, wingEnemy,
//				fixedPieceSelf, fixedPieceEnemy, cMoveSelf, cMoveEnemy, xMoveSelf, xMoveEnemy);

		return movable * MOBILITY_W
				+ (libertySelf - libertyEnemy) * LIBERTY_W
				+ (fixedPieceSelf - fixedPieceEnemy) * FIXED_W
				+ (wingSelf - wingEnemy) * WING_W
				+ (cMoveSelf - cMoveEnemy) * C_MOVE_W
				+ (xMoveSelf - xMoveEnemy) * X_MOVE_W;
	}

	/**
	 * ウイングの個数を数える。
	 *
	 * @param board 盤面
	 * @param teban 手番
	 * @return ウイングの個数
	 */
	private int countWing(Board board, int teban) {
		int count = 0;
		if (isWing((byte) board.getUpperEdge(teban), (byte) board.getUpperEdge(teban ^ 1))) {
			count++;
		}
		if (isWing((byte) board.getLowerEdge(teban), (byte) board.getLowerEdge(teban ^ 1))) {
			count++;
		}
		if (isWing((byte) board.getLeftEdge(teban), (byte) board.getLeftEdge(teban ^ 1))) {
			count++;
		}
		if (isWing((byte) board.getRightEdge(teban), (byte) board.getRightEdge(teban ^ 1))) {
			count++;
		}

		return count;
	}

	/**
	 * 辺がウイングかどうか、確認する。
	 *
	 * @param self 自分の石の並び
	 * @param enemy 敵の石の並び
	 * @return true：ウイング、false：ウイングではない
	 */
	private boolean isWing(byte self, byte enemy) {
		final int mask = 0x00000081;
		// 両端が空白か確認する。
		if (((self | enemy) & mask) != 0) {
			return false;
		}

		final byte wing1 = 0b01111100;
		final byte wing2 = 0b00111110;
		return self == wing1 || self == wing2;
	}

	/**
	 * 確定石の個数を数える。
	 *
	 * @param board 盤面
	 * @param teban 手番
	 * @return 確定石の個数
	 */
	private int countFixedPiece(Board board, int teban) {
		final int mask = 0xFF;
		int count = 0;
		byte rightEdge = (byte) ~board.getRightEdge(teban);
		count += Integer.bitCount(((rightEdge & (-rightEdge)) - 1) & mask);
		if ((rightEdge & 0x80) == 0) {
			rightEdge = (byte) (rightEdge | rightEdge >>> 1);
			rightEdge = (byte) (rightEdge | rightEdge >>> 2);
			rightEdge = (byte) (rightEdge | rightEdge >>> Short.BYTES);
			count += Integer.bitCount((~rightEdge) & mask);
		}

		byte leftEdge = (byte) ~board.getLeftEdge(teban);
		count += Integer.bitCount(((leftEdge & (-leftEdge)) - 1) & mask);
		if ((leftEdge & 0x80) == 0) {
			leftEdge = (byte) (leftEdge | leftEdge >>> 1);
			leftEdge = (byte) (leftEdge | leftEdge >>> 2);
			leftEdge = (byte) (leftEdge | leftEdge >>> Short.BYTES);
			count += Integer.bitCount((~leftEdge) & mask);
		}

		byte upperEdge = (byte) ~board.getUpperEdge(teban);
		count += Integer.bitCount(((upperEdge & (-upperEdge)) - 1) & mask);
		if ((upperEdge & 0x80) == 0) {
			upperEdge = (byte) (upperEdge | upperEdge >>> 1);
			upperEdge = (byte) (upperEdge | upperEdge >>> 2);
			upperEdge = (byte) (upperEdge | upperEdge >>> Short.BYTES);
			count += Integer.bitCount((~upperEdge) & mask);
		}

		byte lowerEdge = (byte) ~board.getLowerEdge(teban);
		count += Integer.bitCount(((lowerEdge & (-lowerEdge)) - 1) & mask);
		if ((lowerEdge & 0x80) == 0) {
			lowerEdge = (byte) (lowerEdge | lowerEdge >>> 1);
			lowerEdge = (byte) (lowerEdge | lowerEdge >>> 2);
			lowerEdge = (byte) (lowerEdge | lowerEdge >>> Short.BYTES);
			count += Integer.bitCount((~lowerEdge) & mask);
		}

		final long mask2 = 0x8100000000000081L;
		long corners = board.getBanmen()[teban] & mask2;
		count -= Long.bitCount(corners);

		return count;
	}

	/**
	 * C打ちを数える。
	 *
	 * @param board 盤面
	 * @param teban 手番
	 * @return C打ちの個数
	 */
	private int countCmove(Board board, int teban) {
		int count = 0;

		// 上辺
		int lineSelf = board.getUpperEdge(teban);
		int lineEnemy = board.getUpperEdge(teban ^ 1);
		int line = lineSelf | lineEnemy;
		if (((line & 0x80) == 0) && ((lineSelf & 0x40) != 0)) {
			count++;
		}
		if (((line & 0x01) == 0) && ((lineSelf & 0x02) != 0)) {
			count++;
		}

		// 下辺
		lineSelf = board.getLowerEdge(teban);
		lineEnemy = board.getLowerEdge(teban ^ 1);
		line = lineSelf | lineEnemy;
		if (((line & 0x80) == 0) && ((lineSelf & 0x40) != 0)) {
			count++;
		}
		if (((line & 0x01) == 0) && ((lineSelf & 0x02) != 0)) {
			count++;
		}

		// 左辺
		lineSelf = board.getLeftEdge(teban);
		lineEnemy = board.getLeftEdge(teban ^ 1);
		line = lineSelf | lineEnemy;
		if (((line & 0x80) == 0) && ((lineSelf & 0x40) != 0)) {
			count++;
		}
		if (((line & 0x01) == 0) && ((lineSelf & 0x02) != 0)) {
			count++;
		}

		// 右辺
		lineSelf = board.getRightEdge(teban);
		lineEnemy = board.getRightEdge(teban ^ 1);
		line = lineSelf | lineEnemy;
		if (((line & 0x80) == 0) && ((lineSelf & 0x40) != 0)) {
			count++;
		}
		if (((line & 0x01) == 0) && ((lineSelf & 0x02) != 0)) {
			count++;
		}

		return count;
	}

	/**
	 * X打ちを数える。
	 *
	 * @param board 盤面
	 * @param teban 手番
	 * @return X打ちの個数
	 */
	private int countXmove(Board board, int teban) {
		int count = 0;
		long corner = board.getCorner(teban) | board.getCorner(teban ^ 1);
		long boardSelf = board.getBanmen()[teban];

		// 左上
		if (((corner & 0x8000000000000000L) == 0) && ((boardSelf & 0x0040000000000000L) != 0)) {
			count++;
		}
		// 右上
		if (((corner & 0x0100000000000000L) == 0) && ((boardSelf & 0x0002000000000000L) != 0)) {
			count++;
		}
		// 左下
		if (((corner & 0x0000000000000080L) == 0) && ((boardSelf & 0x0000000000004000L) != 0)) {
			count++;
		}
		// 右上
		if (((corner & 0x0000000000000001L) == 0) && ((boardSelf & 0x0000000000000200L) != 0)) {
			count++;
		}


		return count;
	}

	/**
	 * 指定した手番の開放度を取得する。
	 *
	 * @param board 盤面
	 * @param teban 手番
	 * @return 開放度
	 */
	private int countLiberty(Board board, int teban) {
		long self = board.getBanmen()[teban];
		long empty = ~(board.getBanmen()[Piece.BLACK] | board.getBanmen()[Piece.WHITE]);
		long point;
		int liberty = 0;
		while (self != 0) {
			point = self & (-self);
			liberty += Long.bitCount(empty & getRound(point));

			self ^= point;
		}
		return liberty;
	}

	/**
	 * 指定した座標の周囲の座標を取得する。
	 *
	 * @param point 座標
	 * @return 周囲の座標
	 */
	private long getRound(long point) {
		long around;
		if (point == 0x8000000000000000L || point == 0x0080000000000000L
				|| point == 0x0000800000000000L || point == 0x0000008000000000L
				|| point == 0x0000000080000000L || point == 0x0000000000800000L
				|| point == 0x0000000000008000L || point == 0x0000000000000080L) {
			around = point |  (point >>> 1);
			around = around | (point << 8) | (point << 7);
			around = around | (point >>> 8) | (point >>> 9);
		} else if (point == 0x0100000000000000L || point == 0x0001000000000000L
				|| point == 0x0000010000000000L || point == 0x0000000100000000L
				|| point == 0x0000000001000000L || point == 0x0000000000010000L
				|| point == 0x0000000000000100L || point == 0x0000000000000001L) {
			around = point |  (point << 1);
			around = around | (point << 9) | (point << 8);
			around = around | (point >>> 7) | (point >>> 8);
		} else {
			around = point | (point << 1) | (point >>> 1);
			around = around | (point << 9) | (point << 8) | (point << 7);
			around = around | (point >>> 7) | (point >>> 8) | (point >>> 9);
		}

		return around;
	}
}
