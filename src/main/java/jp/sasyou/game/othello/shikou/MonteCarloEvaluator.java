package jp.sasyou.game.othello.shikou;

import jp.sasyou.game.othello.rule.Board;

/**
 * モンテカルロ法による評価エンジン
 *
 * @author sashou
 *
 */
public final class MonteCarloEvaluator implements Evaluator {
	/** 係数 */
	private static final int FACTOR = 10000;
	/** 対戦回数 */
	private int matchCount;

	/**
	 * コンストラクタ
	 *
	 * @param matchCount 対戦回数
	 */
	public MonteCarloEvaluator(int matchCount) {
		this.matchCount = matchCount;
	}

	@Override
	public int evaluate(Board board, int teban) {
		int result;
		int win = 0, lose = 0, draw = 0;
		for (int i = 0; i < matchCount; i++) {
			result = play((Board) board.clone(), teban);
			if (result > 0) {
				win++;
			} else if (result < 0) {
				lose++;
			} else {
				draw++;
			}
		}

		return (int) ((win / (double) (win + lose + draw)) * FACTOR);
	}

	/**
	 * 対戦する。
	 *
	 * @param board 盤面
	 * @param teban 手番
	 * @return 石数の差
	 */
	private int play(Board board, int teban) {
		int turn = teban ^ 1;

		long move;
		long legals;
		do {
			legals = board.generateLegals(turn);
			if (legals != 0) {
				move = getNextMove(legals);
				board.putPiece(move, turn);
			}
			turn = turn ^ 1;

		} while (!board.isGameOver());

		long[] banmen = board.getBanmen();
		return Long.bitCount(banmen[teban]) - Long.bitCount(banmen[teban ^ 1]);
	}

	/**
	 * 合法手から一手取り出す。
	 *
	 * @param legals 合法手のリスト
	 * @return 抽出された一手
	 */
	private long getNextMove(long legals) {
		long[] list = new long[Long.bitCount(legals)];
		long mask = 1L;
		long hand;
		int idx = 0;
		for (int i = 0; i < Board.BOARD_SIZE * Board.BOARD_SIZE; i++) {
			hand = legals & mask;
			if (hand != 0) {
				list[idx] = hand;
				idx++;
			}
			mask = mask << 1;
		}

		return list[(int) Math.floor(Math.random() * list.length)];
	}
}
