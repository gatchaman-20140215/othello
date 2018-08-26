package jp.sasyou.game.othello.shikou;

import static org.junit.Assert.*;

import org.junit.Test;

import jp.sasyou.game.othello.rule.Board;
import jp.sasyou.game.othello.rule.Piece;

/**
 * MidEvaluatorクラスのテスト
 *
 * @author sasyou
 *
 */
public class MidEvaluatorTest {

	@Test
	public final void testEvaluate() {
		Board board = new Board();
		board.readKifuText("data/kifu/kifu3.txt");
		System.out.println(board.toString(Piece.BLACK));

		Evaluator eval = new MidEvaluator();
		int value = eval.evaluate(board, Piece.BLACK);
		System.out.printf("value:%d\n", value);
		//eval.evaluate(board, Piece.WHITE);
	}

}
