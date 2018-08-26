package jp.sasyou.game.othello.client.status;

import java.awt.Color;
import java.awt.Point;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.sasyou.game.othello.rule.OthelloRule;
import jp.sasyou.game.othello.rule.Piece;

/**
 * 「相手の番」の状態を表すクラス
 *
 * @author sasyou
 *
 */
public final class CPUStatus extends Status {
	/** ロガー */
	private Logger log = LogManager.getLogger(this.getClass());

	/**
	 * 初期化する。先手番である。
	 */
	public void doFirst() {
		md.showMessageA(md.getPartnerHandle() + "が先手です。", md.getPartnerColor());
	}

	@Override
	public boolean isEnd() {
		return false;
	}

	@Override
	public Status kosan() {
		md.showMessageA(md.getPartnerHandle() + "の勝ちです。", Color.RED);
		md.showMessageB("ゲーム放棄               ");
		return getNext("partnerS:end");
	}

	@Override
	public Status hand(Point at) {
		othelloRule.putPiece(at, getTeban());
		log.debug(othelloRule.getBoard().toString(getTeban()));
		md.drawBoard(
				othelloRule.getBoard().getPointList(Piece.BLACK), othelloRule.getBoard().getPointList(Piece.WHITE));

		switch (othelloRule.checkWin(getTeban())) {
		case OthelloRule.GAMING:
			// 表示の更新
			log.debug("CPU color:%d, check:GAMING\n", getTeban());
			md.showMessageA("あなたの番です。", md.getSelfColor());
			return getNext("partnerS:next");
		case OthelloRule.WIN_SENTE:
			log.debug("CPU color:%d, check:WIN_SENTE\n", getTeban());
			md.showMessageA("先手の勝ちです。", Color.RED);
			md.showMessageB(othelloRule.getReason());
			md.appletEnd();
			return getNext("partnerS:end");
		case OthelloRule.WIN_GOTE:
			log.debug("CPU color:%d, check:WIN_GOTE\n", getTeban());
			md.showMessageA("後手の勝ちです。", Color.RED);
			md.showMessageB(othelloRule.getReason());
			md.appletEnd();
			return getNext("partnerS:end");
		case OthelloRule.NOGAME:
			log.debug("CPU color:%d, check:NOGAME\n", getTeban());
			md.showMessageA("引き分けです。", Color.RED);
			md.showMessageB(othelloRule.getReason());
			md.appletEnd();
			return getNext("partnerS:end");
		case OthelloRule.PASS:
			log.debug("CPU color:%d, check:PASS\n", getTeban());
			md.showMessageA("パスです。", Color.RED);
			md.setButtonPassEnable(true);
			return getNext("selfS:next");
		default:
			return getNext("partnerS:end");
		}
	}

	/**
	 * 手番を取得する。
	 *
	 * @return 手番
	 */
	private int getTeban() {
		return (md.getSelfColor() == Color.BLACK) ? OthelloRule.WHITE : OthelloRule.BLACK;
	}

	/**
	 * パスする。
	 *
	 * @return ステータス
	 */
	public Status pass() {
		md.showMessageA("あなたの番です。", md.getSelfColor());
		return getNext("partnerS:next");
	}
}
