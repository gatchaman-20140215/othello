package jp.sasyou.game.othello.shikou;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.sasyou.game.othello.rule.Board;
import jp.sasyou.game.othello.rule.Hand;

/**
 * NegaMax法に基づくAIクラス
 *
 * @author sasyou
 *
 */
public final class ShikouNegaMax extends ShikouBasic {
	/** 評価値の最大 */
	private static final int MAX_SCORE = Integer.MAX_VALUE;
	/** 評価値の最小 */
	private static final int MIN_SCORE = Integer.MIN_VALUE;

	/** ノード数 */
	private int node;
	/** リーフ数 */
	private int leaf;
	/** 評価エンジン */
	private Evaluator evaluator;
	/** 読みの深さ */
	private int depthMax;

	/** 最善手順 */
	private long[][] best;

	/** ロガー */
	private Logger log = LogManager.getLogger(this.getClass());

	/**
	 * コンストラクタ
	 *
	 * @param evaluator 評価エンジン
	 * @param depthMax 読みの深さ
	 * @param board 盤
	 * @param teban 手番
	 */
	public ShikouNegaMax(Evaluator evaluator, int depthMax, Board board, int teban) {
		this.evaluator = evaluator;
		this.depthMax = depthMax;
		this.board = board;
		this.teban = teban;

		best = new long[depthMax][depthMax];
	}

	@Override
	public void run() {
		Hand hand = getNextHand();
		md.notifyNextHand(hand);
	}

	@Override
	public Hand getNextHand() {
		StopWatch sw = new StopWatch();
		sw.start();

		leaf = 0; node = 0;
		int eval = MIN_SCORE, evalMax = MIN_SCORE;
		long legals = board.generateLegals(teban);
		long move, bestMove = 0;
		while (legals != 0) {
			move = legals & (-legals);
			board.putPiece(move, teban);
			eval = -search(board, teban ^ 1, MIN_SCORE, MAX_SCORE, 1);
			board.undo();

			if (eval >= evalMax) {
				evalMax = eval;
				bestMove = move;

				best[0][0] = move;
				for (int i = 1; i < depthMax; i++) {
					best[0][i] = best[1][i];
				}
			}

			legals ^= move;
		}

		sw.stop();
		Hand hand = new Hand(bestMove, teban);
		log.info(String.format("move:%s, time:%3dms, eval:%+5d, node:%d, leaf:%d",
				hand, sw.getTime(), evalMax, node, leaf));
		if (log.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder();
			int turn = teban;
			for (long te : best[0]) {
				if (te == 0) {
					break;
				}
				if (te == 0) {
					sb.append("　:　,");
				} else {
					sb.append(String.format("%s,", new Hand(te, turn)));
				}
				turn ^= 1;
			}
			log.info(sb.toString());
		}

		return hand;
	}

	/**
	 * 最善手を探索する。
	 *
	 * @param board 盤面
	 * @param teban 手番
	 * @param alpha α値
	 * @param beta β値
	 * @param depth 読みの深さ
	 * @return 評価値
	 */
	private int search(Board board, int teban, int alpha, int beta, int depth) {
		node++;

		if (depth >= depthMax || board.isBoardFull()) {
			leaf++;
			return evaluator.evaluate(board, teban);
		}

		long legals = board.generateLegals(teban);
		if (legals == 0) {
			if (board.isGameOver()) {
				leaf++;
				return evaluator.evaluate(board, teban);
			} else {
				return -search(board, teban ^ 1,  -beta, -alpha, depth);
			}
		}

		long move;
		int score;
		while (legals != 0) {
			move = legals & (-legals);
			board.putPiece(move, teban);
			score = -search(board, teban ^ 1, -beta, -alpha, depth + 1);
			board.undo();

			best[depth][depth] = move;
			for (int i = depth + 1; i < depthMax; i++) {
				best[depth][i] = best[depth + 1][i];
			}

			if (score >= beta) {
				return score;
			}

			if (score > alpha) {
				alpha = score;
			}

			legals ^= move;
		}

		return alpha;
	}
}
