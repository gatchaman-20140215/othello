package jp.sasyou.game.othello.shikou;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.sasyou.game.othello.rule.Board;
import jp.sasyou.game.othello.rule.Hand;
import jp.sasyou.game.othello.rule.HandGenerator;

/**
 * 乱数による思考エンジン
 *
 * @author sasyou
 *
 */
public final class ShikouRandom extends ShikouBasic {

	/** 乱数 */
	private Random random = new Random();

	/**
	 * コンストラクタ
	 */
	public ShikouRandom() {

	}

	/**
	 * コンストラクタ
	 *
	 * @param board 盤
	 * @param teban 手番
	 */
	public ShikouRandom(Board board, int teban) {
		super(board, teban);
	}

	@Override
	public Hand getNextHand() {
		long legals = HandGenerator.generate(board, teban);
		if (legals == 0) {
			return null;
		} else {
			List<Hand> list = new ArrayList<>();
			long mask = 0x8000000000000000L;
			for (int y = 0; y < Board.BOARD_SIZE; y++) {
				for (int x = 0; x < Board.BOARD_SIZE; x++) {
					if ((legals & mask) != 0) {
						list.add(new Hand(new Point(x, y), teban));
					}
					mask = mask >>> 1;
				}
			}
			return list.get(random.nextInt(list.size()));
		}
	}

	@Override
	public void run() {
		Hand hand = getNextHand();
		md.notifyNextHand(hand);
	}

}
