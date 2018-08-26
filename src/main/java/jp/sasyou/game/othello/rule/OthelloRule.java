package jp.sasyou.game.othello.rule;

import java.awt.Point;
import java.util.List;

/**
 * オセロゲームのルール。
 *
 * @author sasyou
 *
 */
public final class OthelloRule implements Rule {
	/** 先手 */
	public static final int BLACK = 0;
	/** 後手 */
	public static final int WHITE = 1;

	/** ゲーム継続中 */
	public static final int GAMING = 0;
	/** 先手の勝ち */
	public static final int WIN_SENTE = 1;
	/** 後手の勝ち */
	public static final int WIN_GOTE = 2;
	/** 引き分け */
	public static final int NOGAME = 3;
	/** パス */
	public static final int PASS = 4;

	/** 盤面 */
	private Board board;

	/** 勝敗理由の文字列 */
	private String reason = "";

	/**
	 * コンストラクタ
	 */
	public OthelloRule() {
		board = new Board();
	}

	/**
	 * コンストラクタ
	 *
	 * @param board 盤面
	 */
	public OthelloRule(Board board) {
		this.board = board;
	}

	/**
	 * その手が正当であるかどうか、判断する。
	 *
	 * @param p 場所
	 * @param teban 手番
	 * @return 正当な手の場合 true、そうでない場合 false
	 */
	@Override
	public boolean canPutPiece(Point p, int teban) {
		return board.canPutPiece(p, teban);
	}

	/**
	 * 盤面に石を置く。
	 *
	 * @param p 場所
	 * @param teban 手番
	 * @return 裏返すことのできる場所のリスト
	 */
	@Override
	public List<Point> putPiece(Point p, int teban) {
		return board.putPiece(p, teban);
	}

	/**
	 * パスすべき局面かどうか判断する。
	 *
	 * @param teban 手番
	 * @return パスすべき局面の場合 true、そうでない場合 false
	 */
	@Override
	public boolean forcePass(int teban) {
		return !board.canMove(teban);
	}


	/**
	 * 勝敗をチェックする。
	 *
	 * @param teban 手番
	 * @return 勝敗チェックの結果
	 */
	@Override
	public int checkWin(int teban) {
		if (forcePass(teban ^ 1)) {
			if (forcePass(teban)) {
				int sum1, sum2;
				sum1 = board.getNumberOfBlack();
				sum2 = board.getNumberOfWhite();
				reason = sum1 + "対" + sum2;
				if (sum1 > sum2) {
					return WIN_SENTE;
				} else if (sum1 < sum2) {
					return WIN_GOTE;
				} else {
					return NOGAME;
				}
			} else {
				return PASS;
			}
		}
		return GAMING;
	}

	/**
	 * 盤面
	 * @return 盤面
	 */
	public Board getBoard() {
	    return board;
	}

	@Override
	public String getReason() {
		return reason;
	}

}
