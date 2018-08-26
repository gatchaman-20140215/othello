package jp.sasyou.game.othello.rule;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * OthelloRuleクラスのテスト
 *
 * @author sashou
 *
 */
public class OthelloRuleTest {

	@Test
	public void testCheckWin() {
		Board board = new Board();
		board.readKifuText("data/kifu/kifu1.txt");
		System.out.println(board.toString(Piece.WHITE));
		OthelloRule rule = new OthelloRule(board);
		System.out.println(rule.checkWin(Piece.WHITE));
	}

	@Test
	public void testForcePass() {
		Board board = new Board();
		board.readKifuText("data/kifu/kifu2.txt");
		System.out.println(board.toString(Piece.BLACK));
		OthelloRule rule = new OthelloRule(board);
		System.out.println(rule.forcePass(Piece.BLACK));
		System.out.println(rule.forcePass(Piece.WHITE));
	}
}
