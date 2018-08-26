package jp.sasyou.game.othello.client;

import java.awt.Container;
import java.awt.Dimension;

import org.apache.commons.configuration2.Configuration;

import jp.sasyou.game.othello.client.AWT.AWTConfrontControl;
import jp.sasyou.game.othello.client.AWT.AWTControl;
import jp.sasyou.game.othello.client.AWT.AWTBoardImage;
import jp.sasyou.game.othello.client.AWT.AWTJumpDialog;
import jp.sasyou.game.othello.client.AWT.AWTPassDialog;
import jp.sasyou.game.util.PropertyUtil;

/**
 * GUIFactory は個々の部品の具象クラスを生成して返す。
 * SWT の場合、機能は共通でも引数に渡されるクラスが若干異なるので、
 * これを interface や抽象基底クラスとせず、具象基底クラスとする。
 * @author sasyou
 *
 */
public final class GUIFactory {
	/** プロパティ */
	private Configuration prop = PropertyUtil.getInstance();

	/**
	 * 盤面を取得する。
	 *
	 * @param top コンテナ
	 * @param md メディエータ
	 * @return 盤面イメージ
	 */
	public BoardImage getBoardImage(Container top, Mediator md) {
		return new AWTBoardImage(top, md);
	}

	/**
	 * 上部コンテナを取得する。
	 *
	 * @param top コンテナ
	 * @param md メディエータ
	 * @return コントロール
	 */
	public Control getControl(Container top, Mediator md) {
		return new AWTControl(top, md);
	}

	/**
	 * 対戦コンテナを取得する。
	 *
	 * @param top コンテナ
	 * @param md メディエータ
	 * @return コンフロント・コントロール
	 */
	public ConfrontControl getConfrontControl(Container top, Mediator md) {
		return new AWTConfrontControl(top, md);
	}

	/**
	 * ダイアログボックスを取得する。
	 *
	 * @param main アプリケーション
	 * @param title タイトル
	 * @param message メッセージ
	 * @return ジャンプ・ダイアログ
	 */
	public JumpDialog getJumpDialog(OthelloMain main, String title, String message) {
		return new AWTJumpDialog(main, title, message);
	}

	/**
	 * パス用のダイアログボックスを取得する。
	 *
	 * @param main アプリケーション
	 * @param title タイトル
	 * @param message メッセージ
	 * @return ジャンプ・ダイアログ
	 */
	public JumpDialog getPassDialog(OthelloMain main, String title, String message) {
		return new AWTPassDialog(main, title, message);
	}

	// 以下今回は特にメリットがないが、別のゲームに転用する場合に、
	// 非常に有効な Abstract Factory 化。
	/**
	 * Meidator を取得する。
	 *
	 * @param main アプリケーション
	 * @return メディエータ
	 */
	public Mediator getMediator(OthelloMain main) {
		return new Mediator(main);
	}

	/**
	 * タイトルを取得する。
	 *
	 * @return タイトル
	 */
	public String getTitle() {
		return "対戦型オセロ(AWT)";
	}

	/**
	 * 画面サイズを取得する。
	 * これだけ各ツールキットに関係なく共通。
	 *
	 * @return 画面サイズ
	 */
	public Dimension getSize() {
		final int defWidth = 500, defHeight = 575;
		int width = prop.getInt("gui.size.width", defWidth);
		int height = prop.getInt("gui.size.height", defHeight);
		return new Dimension(width, height);
	}
}
