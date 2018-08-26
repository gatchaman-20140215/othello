package jp.sasyou.game.othello.rule;

import java.awt.Point;

/**
 * 手を表すクラス
 *
 * @author sasyou
 *
 */
public final class Hand implements Cloneable {
	/** 置く場所 */
	private Point p;
	/** 石の色 */
	private int piece;

	/**
	 * コンストラクタ
	 */
	public Hand() {
		this.p = new Point(-1, -1);
		this.piece = Piece.EMPTY;
	}

	/**
	 * コンストラクタ
	 *
	 * @param p 置く場所
	 * @param piece 石の色
	 */
	public Hand(Point p, int piece) {
		this.p = p;
		this.piece = piece;
	}

	/**
	 * コンストラクタ
	 *
	 * @param move 手
	 * @param piece 石の色
	 */
	public Hand(long move, int piece) {
		this.p = convert(move);
		this.piece = piece;
	}

	/**
	 * 手が同一かどうか、判定する。
	 *
	 * @param o 比較対象の手
	 * @return 同一の場合 true、そうでない場合 false
	 */
	public boolean equals(Object o) {
		if (o instanceof Hand) {
			return equals((Hand) o);
		} else {
			return false;
		}
	}

	/**
	 * 手が同一かどうか、判定する。
	 *
	 * @param hand 比較対象の手
	 * @return 同一の場合 true、そうでない場合 false
	 */
	public boolean equals(Hand hand) {
		return hand.p.x == p.x && hand.p.y == p.y && hand.piece == piece;
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		result = prime * result + piece;
		return result;
	}

	/**
	 * 置く場所
	 * @return 置く場所
	 */
	public Point getP() {
	    return p;
	}

	/**
	 * 手を文字列化する。
	 *
	 * @return 文字列化された手
	 */
	public String toString() {
		if (p.x == -1) {
			return "　:　";
		} else {
			String pieceStr = "";
			if (piece == Piece.BLACK) {
				pieceStr = "黒";
			} else if (piece == Piece.WHITE) {
				pieceStr = "白";
			} else {
				pieceStr = "空";
			}
			return String.format("%s:%s%s",
					pieceStr, Board.BOARD_STR[Piece.BLACK][p.x], Board.BOARD_STR[Piece.WHITE][p.y]);
		}
	}

	/**
	 * 手を設定する。
	 *
	 * @param hand 手
	 */
	public void setHand(Hand hand) {
		this.p = hand.p;
		this.piece = hand.piece;
	}

	/**
	 * 手を設定する。
	 *
	 * @param move 手
	 */
	public void setHand(long move) {
		this.p = convert(move);
	}

	/**
	 * 石の色を取得する。
	 * @return 石の色
	 */
	public int getPiece() {
	    return piece;
	}

	/**
	 * 石の色を設定する。
	 * @param piece 石の色
	 */
	public void setPiece(int piece) {
	    this.piece = piece;
	}

	/**
	 * 変換する。
	 *
	 * @param move 手
	 * @return 場所
	 */
	private Point convert(long move) {
		if (move == 0) {
			return new Point(-1, -1);
		}

		final long mask = 0x00000000FFFFFFFFL;
		long b1 = (long) (move >>> Integer.SIZE);
		long b2 = (long) (move & mask);
		int b = (b1 != 0) ? (int) (Math.log(b1) / Math.log(2)) + Integer.SIZE : (int) (Math.log(b2) / Math.log(2));

		return new Point(
				Board.BOARD_SIZE - 1 - (b % Board.BOARD_SIZE), Board.BOARD_SIZE - 1 - (b / Board.BOARD_SIZE));
	}
}
