package jp.sasyou.game.othello.shikou;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.sasyou.game.othello.rule.Board;
import jp.sasyou.game.othello.rule.Hand;

/**
 * モンテカルロ法による思考エンジン
 *
 * @author sasyou
 *
 */
public final class ShikouMonteCarlo extends ShikouBasic {
	/** 評価エンジン */
	private Evaluator evaluator;

	/** ロガー */
	private Logger log = LogManager.getLogger(this.getClass());

	/**
	 * コンストラクタ
	 *
	 * @param board 盤
	 * @param teban 手番
	 * @param matchCount 対戦回数
	 */
	public ShikouMonteCarlo(Board board, int teban, int matchCount) {
		this.board = board;
		this.teban = teban;
		this.evaluator = new MonteCarloEvaluator(matchCount);
	}

	@Override
	public Hand getNextHand() {
		StopWatch sw = new StopWatch();
		sw.start();

		int eval = 0, evalMax = -1;
		long best = -1;
		long move;
		long legals = board.generateLegals(teban);
		while (legals != 0) {
			move = legals & (-legals);
			board.putPiece(move, teban);
			eval = evaluator.evaluate(board, teban);
			board.undo();

			if (eval > evalMax) {
				best = move;
				evalMax = eval;
			}

			legals ^= move;
		}

		sw.stop();
		log.info(String.format("move:%s, winningRate:%5d, time:%dms", new Hand(best, teban), evalMax, sw.getTime()));
		return new Hand(best, teban);
	}

	@Override
	public void run() {
		Hand hand = getNextHand();
		md.notifyNextHand(hand);
	}
}
