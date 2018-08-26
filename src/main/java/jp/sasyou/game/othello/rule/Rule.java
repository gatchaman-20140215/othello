package jp.sasyou.game.othello.rule;

import java.awt.Point;
import java.util.List;

/**
 * オセロのルール
 *
 * @author sasyou
 *
 */
public interface Rule {

	/**
	 * その手が正当であるかどうか、判断する。
	 *
	 * @param p 場所
	 * @param teban 手番
	 * @return 正当な手の場合 true、そうでない場合 false
	 */
	boolean canPutPiece(Point p, int teban);

	/**
	 * 盤面に石を置く。
	 *
	 * @param p 場所
	 * @param teban 手番
	 * @return 裏返すことのできる場所のリスト
	 */
	List<Point> putPiece(Point p, int teban);

	/**
	 * パスすべき局面かどうか判断する。
	 *
	 * @param teban 手番
	 * @return パスすべき局面の場合 true、そうでない場合 false
	 */
	boolean forcePass(int teban);

	/**
	 * ゲームが終了したかどうか、また勝敗をチェックする。
	 *
	 * @param teban 手番
	 * @return 勝敗チェックの結果
	 */
	int checkWin(int teban);

	/**
	 * 勝敗理由の文字列表現を取得する。
	 *
	 * @return 勝敗の理由
	 */
	String getReason();
}
