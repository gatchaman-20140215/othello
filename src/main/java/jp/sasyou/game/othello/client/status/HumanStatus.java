package jp.sasyou.game.othello.client.status;

import java.awt.Color;
import java.awt.Point;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.sasyou.game.othello.rule.OthelloRule;
import jp.sasyou.game.othello.rule.Piece;

/**
 * 「自分の番」の状態を表すクラス
 *
 * @author sasyou
 *
 */
public final class HumanStatus extends Status {
	/** ロガー */
	private Logger log = LogManager.getLogger(this.getClass());

	/**
	 * 初期化する。先手番である。
	 */
	public void doFirst() {
		md.showMessageA("あなたが先手です。", md.getSelfColor());
	}

	/**
	 * 終了状態かどうかを返す。
	 *
	 * @return 終了状態の場合 true、そうでない場合 false
	 */
	public boolean isEnd() {
		return false;
	}

	// 以下、アプレットのアクションによって生成する入力の処理
	@Override
	public Status kosan() {
		md.showMessageA(md.getPartnerHandle() + "の勝ちです。", Color.RED);
		md.showMessageB("降参！！　               ");
		return getNext("selfS:end");
	}

	/**
	 * 引き分けの場合
	 *
	 * @param s 文字列
	 * @param at 場所
	 * @return ステータス
	 */
	public Status nogame(String s, Point at) {
		md.showMessageA("引き分け", Color.RED);
		md.showMessageB(s);
		return getNext("selfS:end");
	}

	@Override
	public Status mouse(Point at) {
		// クリックした位置の正当性をチェックする。
		if (!othelloRule.canPutPiece(at, getTeban())) {
			// すでに石がある場合、クリックは無効
			return this;
		}

		othelloRule.putPiece(at, getTeban());
		log.debug(othelloRule.getBoard().toString(getTeban()));
		md.drawBoard(
				othelloRule.getBoard().getPointList(Piece.BLACK), othelloRule.getBoard().getPointList(Piece.WHITE));

		switch (othelloRule.checkWin(getTeban())) {
		case OthelloRule.GAMING:
			// 表示の更新
			log.debug("Human color:%d, check:GAMING\n", getTeban());
			md.showMessageA(md.getPartnerHandle() + "の番です。", md.getPartnerColor());
			md.showMessageB("相手の手を待っています。");
			return getNext("selfS:next");
		case OthelloRule.WIN_SENTE:
			log.debug("Human color:%d, check:WIN_SENTE\n", getTeban());
			md.showMessageA("先手の勝ちです。", Color.RED);
			md.showMessageB(othelloRule.getReason());
			md.appletEnd();
			return getNext("selfS:end");
		case OthelloRule.WIN_GOTE:
			log.debug("Human color:%d, check:WIN_GOTE\n", getTeban());
			md.showMessageA("後手の勝ちです。", Color.RED);
			md.showMessageB(othelloRule.getReason());
			md.appletEnd();
			return getNext("selfS:end");
		case OthelloRule.NOGAME:
			log.debug("Human color:%d, check:NOGAME\n", getTeban());
			md.showMessageA("引き分けです。", Color.RED);
			md.showMessageB(othelloRule.getReason());
			md.appletEnd();
			return getNext("selfS:end");
		case OthelloRule.PASS:
			log.debug("Human color:%d, check:PASS\n", getTeban());
			md.showMessageA("パスです。", Color.RED);
			return getNext("partnerS:next");
		default:
			return getNext("selfS:end");
		}
	}

	/**
	 * 手番を取得する。
	 *
	 * @return 手番
	 */
	private int getTeban() {
		return (md.getSelfColor() == Color.BLACK) ? OthelloRule.BLACK : OthelloRule.WHITE;
	}

	@Override
	public Status pass(Point p) {
		switch (othelloRule.checkWin(getTeban())) {
		case OthelloRule.GAMING:
			log.debug("Human PASS color:%d, check:GAMING\n", getTeban());
			// 表示の更新
			md.showMessageA(md.getPartnerHandle() + "の番です。", md.getPartnerColor());
			md.showMessageB("相手の手を待っています。");
			return getNext("selfS:next");
		case OthelloRule.WIN_SENTE:
			log.debug("Human PASS color:%d, check:WIN_SENTE\n", getTeban());
			md.showMessageA("先手の勝ちです。", Color.RED);
			md.showMessageB(othelloRule.getReason());
			md.appletEnd();
			return getNext("selfS:end");
		case OthelloRule.WIN_GOTE:
			log.debug("Human PASS color:%d, check:WIN_GOTE\n", getTeban());
			md.showMessageA("後手の勝ちです。", Color.RED);
			md.showMessageB(othelloRule.getReason());
			md.appletEnd();
			return getNext("selfS:end");
		case OthelloRule.NOGAME:
			log.debug("Human PASS color:%d, check:NOGAME\n", getTeban());
			md.showMessageA("引き分けです。", Color.RED);
			md.showMessageB(othelloRule.getReason());
			md.appletEnd();
			return getNext("selfS:end");
		case OthelloRule.PASS:
			log.debug("Human PASS color:%d, check:PASS\n", getTeban());
			md.showMessageA("パスです。", Color.RED);
			return getNext("partnerS:next");
		default:
			return getNext("selfS:end");
		}
	}
}
