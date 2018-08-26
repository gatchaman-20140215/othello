package jp.sasyou.game.othello.shikou;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.sasyou.game.othello.rule.Board;
import jp.sasyou.game.othello.rule.Hand;
import jp.sasyou.game.othello.rule.HandGenerator;
import jp.sasyou.game.othello.rule.Piece;
import jp.sasyou.game.othello.shikou.tejyun.Tejyun;
import jp.sasyou.game.othello.shikou.tejyun.TejyunLeaf;
import jp.sasyou.game.othello.shikou.tejyun.TejyunNode;

/**
 * αβ法による思考エンジン
 *
 * @author sasyou
 *
 */
public final class ShikouAlphaBeta extends ShikouBasic {
	/** 最大値を表すための定数 */
	private static final int INFINITE = 99999999;
	/** 読みの深さ */
	private int depthMax;

	/** リーフ */
	private int leaf = 0;
	/** ノード */
	private int node = 0;

	/** 局面評価エンジン */
	private Evaluator evaluator;

	/** ロガー */
	private Logger log = LogManager.getFormatterLogger(this.getClass());

	/**
	 * コンストラクタ
	 *
	 * @param evaluator 局面評価エンジン
	 * @param depthMax 読みの深さ
	 * @param board 盤
	 * @param teban 手番
	 */
	public ShikouAlphaBeta(Evaluator evaluator, int depthMax, Board board, int teban) {
		this.evaluator = evaluator;
		this.depthMax = depthMax;
		this.board = board;
		this.teban = teban;
	}

	@Override
	public void run() {
		Hand hand = getNextHand();
		md.notifyNextHand(hand);
	}

	@Override
	public Hand getNextHand() {
		leaf = 0; node = 0;
		int eval = 0;

		Hand hand = new Hand();
		Tejyun tejyun = new TejyunNode(0, 0, 0);
		long giveMove = 0;
		long time = System.currentTimeMillis();
		if (teban == Piece.BLACK) {
			// 評価値最大の手を得る。
			eval = getMax(hand, board, teban, -INFINITE, INFINITE, 0, giveMove, tejyun);
		} else if (teban == Piece.WHITE) {
			// 評価値最小の手を得る。
			eval = getMin(hand, board, teban, -INFINITE, INFINITE, 0, giveMove, tejyun);
		}

		log.info("先読みの評価値:%d, teban:%d, hand:%s,", eval, teban, hand);
	    log.info(tejyun.viewSaizenTejyun());
		time = System.currentTimeMillis() - time;
		log.info("leaf=%d, node=%d, time=%dms", leaf, node, time);

		if (hand.equals(new Hand())) {
			return null;
		} else {
			return hand;
		}
	}

	/**
	 * 評価値が最大になる手を取得する。
	 *
	 * @param hand 手
	 * @param board 盤
	 * @param piece 石
	 * @param alpha α値
	 * @param beta β値
	 * @param depth 深さ
	 * @param givenMove 手
	 * @param tejyun 手順
	 * @return 評価値
	 */
	private int getMax(Hand hand, Board board, int piece, int alpha, int beta, int depth,
			long givenMove, Tejyun tejyun) {
		// 思考が最大深さに達していたら、そこでの評価値を返して終了
		if (depth >= depthMax) {
			leaf++;
			int value = evaluator.evaluate(board, piece);
			((TejyunNode) tejyun).addTejyun(new TejyunLeaf(givenMove, piece, value, depth));
			return value;
		}

		// 現在の局面での合法手を生成する。
		long legals = HandGenerator.generate(board, piece);
		if (legals == 0) {
			leaf++;
			int value = evaluator.evaluate(board, piece);
			((TejyunNode) tejyun).addTejyun(new TejyunLeaf(givenMove, piece, value, depth));
			return value;
		}

		node++;
		// 手順を格納するインスタンスを生成する。
		TejyunNode tejyunNode = new TejyunNode(givenMove, piece, depth);
		((TejyunNode) tejyun).addTejyun(tejyunNode);
		// 現在の指しての候補手の評価値を入れる。
		int value = -INFINITE;
		// 合法手の中から一手進めてみて、一番よかった指し手を選択
		long move;
		while (legals != 0) {
			move = legals & (-legals);
			// その手で一手進めた局面を作ってみる
			board.putPiece(move, piece);

			Hand tmpHand = new Hand();
			int eval = getMin(tmpHand, board, piece ^ 1, alpha, beta, depth + 1, move, tejyunNode);
			board.undo();

			// 先手番の場合･･･
			// 指し手で進めた局面が、今までよりもっと大きな値を返すか？
			if (eval > value) {
				// 返す手を更新
				value = eval;
				// α値も更新
				if (eval > alpha) {
					alpha = eval;
				}

				hand.setHand(move);
				// βカットの条件を満たしていたら、ループ終了
				if (eval >= beta) {
					break;
				}
			}

			legals ^= move;
		}

		tejyunNode.setValue(value);
		return value;
	}

	/**
	 * 評価値が最小になる手を取得する。
	 *
	 * @param hand 手
	 * @param board 盤
	 * @param piece 石
	 * @param alpha α値
	 * @param beta β値
	 * @param depth 深さ
	 * @param givenMove 次の手
	 * @param tejyun 手順
	 * @return 評価値
	 */
	private int getMin(Hand hand, Board board, int piece, int alpha, int beta, int depth,
			long givenMove, Tejyun tejyun) {
		// 深さが最大深さに達していたら、そこでの評価値を返して終了
		if (depth >= depthMax) {
			leaf++;
			int value = evaluator.evaluate(board, piece);
			((TejyunNode) tejyun).addTejyun(new TejyunLeaf(givenMove, piece, value, depth));
			return value;
		}

		// 現在の局面での合法手を生成する。
		long legals = HandGenerator.generate(board, piece);
		// 指し手がなかったら、そこでの評価値を返して終了
		if (legals == 0) {
			leaf++;
			int value = evaluator.evaluate(board, piece);
			((TejyunNode) tejyun).addTejyun(new TejyunLeaf(givenMove, piece, value, depth));
			return value;
		}

		node++;
		// 手順を格納するインスタンスを生成する。
		TejyunNode tejyunNode = new TejyunNode(givenMove, piece, depth);
		((TejyunNode) tejyun).addTejyun(tejyunNode);
		// 現在の指し手の候補手の評価値を入れる。
		int value = INFINITE;
		// 合法手の中から、一手指してみて、一番よかった指し手を選択
		long move;
		while (legals != 0) {
			move = legals & (-legals);
			// その手で一手進めた局面を作ってみる。
			board.putPiece(move, piece);

			// その局面の評価値をさらに先読みして得る。
			Hand tmpHand = new Hand();
			int eval = getMax(tmpHand, board, piece ^ 1, alpha, beta, depth + 1, move, tejyunNode);
			board.undo();

			// 後手番の場合･･･
			// 指し手で進めた局面が、今までよりもっと小さな値を返すか？
			if (eval < value) {
				// 返す手を更新
				value = eval;
				// βも更新
				if (eval < beta) {
					beta = eval;
				}

				hand.setHand(move);
				// αの条件を満たしていたら、ループ終了
				if (eval <= alpha) {
					break;
				}
			}

			legals ^= move;
		}

		tejyunNode.setValue(value);
		return value;
	}
}
