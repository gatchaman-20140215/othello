package jp.sasyou.game.othello.client;

import java.awt.Point;
import java.net.URL;

/**
 * メインウィンドウ
 *
 * @author sasyou
 *
 */
public interface OthelloMain {
	/**
	 * プログラムを停止する。
	 */
	void stop();

	/**
	 * ダイアログを表示する。
	 *
	 * @param title タイトル
	 * @param message メッセージ
	 */
	void showDialog(String title, String message);

	/**
	 * ウィンドウの画面上での位置を取得する。
	 *
	 * @return 位置
	 */
	Point getLocation();

	/**
	 * アプレット対応で、HTTP 上でのベース URL を取得する。
	 *
	 * @return URL
	 */
	URL getDocumentBase();
}
