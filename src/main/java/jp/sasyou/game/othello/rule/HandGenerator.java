package jp.sasyou.game.othello.rule;

/**
 * 手を生成するクラス
 *
 * @author sasyou
 *
 */
public final class HandGenerator {

	/**
	 * コンストラクタの使用禁止
	 */
	private HandGenerator() {

	}

	/**
	 * 合法手のリストを取得する。
	 *
	 * @param board 盤
	 * @param teban 手番
	 * @return 合法手のリスト
	 */
	public static long generate(Board board, int teban) {
		return board.generateLegals(teban);
	}

	/**
	 * 合法手の数を数える。
	 *
	 * @param board 盤面
	 * @param teban 手番
	 * @return 合法手の数
	 */
	public static int countHands(Board board, int teban) {
		return Long.bitCount(board.generateLegals(teban));
	}
}
