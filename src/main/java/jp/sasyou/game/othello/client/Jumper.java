package jp.sasyou.game.othello.client;

import java.applet.Applet;
import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.configuration2.Configuration;

import jp.sasyou.game.util.PropertyUtil;

/**
 * アプレットのケースで、ダイアログのクリックするとともに、
 * ブラウザページで切り替わる処理。
 *
 * @author sasyou
 *
 */
public final class Jumper {
	/** アプレット */
	private Applet applet;
	/** ジャンプするページ */
	private String nextPage;
	/** ジャンプするページの URL */
	private URL jump;
	/** アプレットのベース URL */
	private URL base;

	/**
	 * コンストラクタ
	 *
	 * @param ap メインアプリケーション
	 */
	public Jumper(OthelloMain ap) {
		Configuration prop = PropertyUtil.getInstance();
		OthelloMain parent = ap;
		if (ap instanceof Applet) {
			applet = (Applet) ap;
		} else {
			applet = null;
		}
		nextPage = (String) prop.getProperty("nextPage");
		base = parent.getDocumentBase();
	}

	/**
	 * ジャンプするページの設定
	 *
	 * @return 文字列
	 */
	public String getOrientation() {
		if (nextPage == null || nextPage.equals("")) {
			jump = null;
			return "　";
		} else {
			try {
				// 相対 URL で作成
				jump = new URL(base, nextPage);
				return jump.toString() + " に戻ります。";
			} catch (MalformedURLException e) {
				jump = null;
				return "パラメータ nextPage(" + nextPage + ") は異常な URL です。";
			}
		}
	}

	/**
	 * ジャンプする。
	 *
	 * @return ジャンプに成功した場合 true、そうでない場合 false
	 */
	public boolean jump() {
		if (jump != null && applet != null) {
			// ジャンプ
			AppletContext ac = applet.getAppletContext();
			if (ac != null) {
				ac.showDocument(jump);
				return true;
			}
		}
		return false;
	}
}
