package jp.sasyou.game.othello.client;

import java.awt.Point;
import java.net.URL;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jp.sasyou.game.util.PropertyUtil;

/**
 * スタンドアロンの抽象基底クラス
 *
 * @author sasyou
 *
 */
public abstract class OthelloClient implements OthelloMain {
	/** プロパティ */
	protected Configuration prop;
	/** ロガー */
	protected Logger logger;

	/**
	 * コンストラクタ
	 *
	 * @param args コマンドライン引数
	 */
	public OthelloClient(String[] args) {
		prop = PropertyUtil.getInstance();
		logger = LogManager.getLogger(this.getClass());
	}

	/**
	 * ウィンドウの画面上での位置を取得する。
	 *
	 * @return 位置
	 */
	@Override
	public abstract Point getLocation();

	/**
	 * ダイアログを表示する。
	 *
	 * @param title タイトル
	 * @param message メッセージ
	 */
	@Override
	public abstract void showDialog(String title, String message);

	/**
	 * プログラムを停止する。
	 */
	@Override
	public abstract void stop();

	/**
	 * アプレット対応で、HTTP 上でのベース URL を取得する。
	 *
	 * @return URL
	 */
	@Override
	public final URL getDocumentBase() {
		return null;
	}
}
