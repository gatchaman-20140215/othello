package jp.sasyou.game.othello.rule;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Boardクラスのテスト
 *
 * @author sashou
 *
 */
public class BoardTest {

	@Test
	public void testToString() {
		Board board = new Board();
		System.out.println(board.toString(Piece.BLACK));
	}

	@Test
	public void testReadKifuText() {
		Board board = new Board();
		board.readKifuText("data/kifu/kifu1.txt");
		System.out.println(board.toString(Piece.WHITE));
		System.out.printf("black:%016x\n", board.getBanmen()[Piece.BLACK]);
		System.out.printf("white:%016x\n", board.getBanmen()[Piece.WHITE]);
	}
}
