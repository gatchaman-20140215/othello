package jp.sasyou.game.othello.client.AWT;

import java.awt.Point;
import java.awt.event.ActionEvent;

import jp.sasyou.game.othello.client.Jumper;
import jp.sasyou.game.othello.client.OthelloMain;

/**
 * 終了のダイアログを表示し、アプレットのページを切り替える。
 * 親クラスが Applet でもそうでなくても動作するようにする。
 *
 * @author sasyou
 *
 */
public class AWTJumpDialog extends AWTSimpleDialog {
	/** 親クラス */
	private OthelloMain parent;
	/** アプレットの場合、飛び先に飛ぶ */
	private Jumper jumper;

	/**
	 * コンストラクタ
	 *
	 * @param title
	 * @param message
	 */
	public AWTJumpDialog(OthelloMain ap, String title, String message) {
		super(title, message);
		parent = ap;

		jumper = new Jumper(parent);
		String message2 = jumper.getOrientation();
		setLabel(message2);
	}

	/**
	 * 表示する。
	 */
	public void show() {
		Point p = parent.getLocation();
		setLocation(p.x + 200, p.y + 100);
		super.show();
	}

	/**
	 * OK ボタンがクリックされたら
	 *
	 * @param ae
	 */
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		jumper.jump();
		parent.stop();
	}
}
