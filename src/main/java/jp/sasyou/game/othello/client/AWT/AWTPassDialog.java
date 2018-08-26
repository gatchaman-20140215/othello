package jp.sasyou.game.othello.client.AWT;

import java.awt.Point;

import jp.sasyou.game.othello.client.OthelloMain;

/**
 * 強制パスダイアログ
 *
 * @author sasyou
 *
 */
public class AWTPassDialog extends AWTSimpleDialog {
	/** 親クラス */
	private OthelloMain parent;

	/**
	 * コンストラクタ
	 *
	 * @param ap
	 * @param message1
	 * @param message2
	 */
	public AWTPassDialog(OthelloMain ap, String message1, String message2) {
		super("注目！", message1);
		parent = ap;

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
}
