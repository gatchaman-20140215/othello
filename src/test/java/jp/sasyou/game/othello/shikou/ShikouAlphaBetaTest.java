package jp.sasyou.game.othello.shikou;

import static org.junit.Assert.*;

import org.junit.Test;

import jp.sasyou.game.othello.rule.Board;
import jp.sasyou.game.othello.rule.Hand;
import jp.sasyou.game.othello.rule.Piece;

/**
 * ShikouAlphaBetaクラスのテスト
 *
 * @author sashou
 *
 */
public class ShikouAlphaBetaTest {

	@Test
	public void testGetNextHand() {
		Board board = new Board();
		board.readKifuText("data/kifu/kifu1.txt");
		System.out.println(board.toString(Piece.WHITE));

		Shikou shikou = new ShikouAlphaBeta(new PositionEvaluator(), 3, board, Piece.WHITE);
		Hand hand = shikou.getNextHand();
		System.out.printf("hand:%s\n", hand);


		long[] banmen = new long[2];
		long[] originalBanmen = board.getBanmen();
		long empty = ~(originalBanmen[Piece.BLACK] | originalBanmen[Piece.WHITE]);
		banmen[Piece.BLACK] = ~(originalBanmen[Piece.BLACK] | empty);
		banmen[Piece.WHITE] = ~(originalBanmen[Piece.WHITE] | empty);
		Board board2 = new Board(banmen);
		System.out.println(board2.toString(Piece.BLACK));

		Shikou shikou2 = new ShikouAlphaBeta(new PositionEvaluator(), 3, board2, Piece.BLACK);
		Hand hand2 = shikou.getNextHand();
		System.out.printf("hand2:%s\n", hand2);
	}

}
