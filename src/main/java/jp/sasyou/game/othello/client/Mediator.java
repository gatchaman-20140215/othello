package jp.sasyou.game.othello.client;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.List;

import jp.sasyou.game.othello.client.status.CPUStatus;
import jp.sasyou.game.othello.client.status.HumanStatus;
import jp.sasyou.game.othello.client.status.Status;
import jp.sasyou.game.othello.client.status.StatusFactory;
import jp.sasyou.game.othello.rule.Hand;
import jp.sasyou.game.othello.rule.OthelloRule;
import jp.sasyou.game.othello.shikou.Evaluator;
import jp.sasyou.game.othello.shikou.KyokumenEvaluator;
import jp.sasyou.game.othello.shikou.MidEvaluator;
import jp.sasyou.game.othello.shikou.NumberEvaluator;
import jp.sasyou.game.othello.shikou.PerfectEvaluator;
import jp.sasyou.game.othello.shikou.Shikou;
import jp.sasyou.game.othello.shikou.ShikouNegaMax;
import jp.sasyou.game.othello.shikou.ShikouMonteCarlo;
import jp.sasyou.game.othello.shikou.ShikouRandom;

/**
 * 本ゲームのメディエータ
 *
 * @author sasyou
 *
 */
public final class Mediator implements MouseListener, ActionListener, ItemListener {
	/** 今の状態 */
	private Status now = null;

	// GUI コンポーネント
	/** ボードイメージ */
	private BoardImage bi;
	/** コントロール */
	private Control cl;
	/** コンフロント・コントロール */
	private ConfrontControl cfcl;

	// ユーザ情報
	/** 名前 */
	private String[] name = new String[2];
	/** ハンドル */
	private String[] handle = new String[2];
	/** 手番 */
	private int teban = OthelloRule.BLACK;
	/** 「黒」「白」の色 */
	private Color selfColor, partnerColor;
	/** 思考エンジン */
	private Shikou shikou = new ShikouRandom();
	/** オセロルール */
	private OthelloRule othelloRule;

	/**
	 * コンストラクタ
	 *
	 * @param om メイン関数
	 */
	public Mediator(OthelloMain om) {
		othelloRule = createRule();
		shikou = new ShikouRandom(othelloRule.getBoard(), OthelloRule.WHITE);
		shikou.attach(this);
	}

	/**
	 * アプレットを開始する。
	 */
	public void start() {
		// 先手・後手の決定
		selfColor = Color.BLACK;
		partnerColor = Color.WHITE;

		// State オブジェクトの生成
		now = createStatus(teban);
		// 状態に応じたセットアップ
		//now.doFirst();
	}

	/**
	 * アプレットを終了させる。
	 * 何度呼ばれても大丈夫なようにしておく。
	 */
	public void appletEnd() {
		now = null;
		cfcl.getCheckboxSente().setEnabled(true);
		cfcl.getCheckboxGote().setEnabled(true);
		cfcl.getButtonTaikyoku().setEnabled(true);
	}

	/**
	 * 停止
	 */
	public synchronized void stopped() {

	}

	/**
	 * ボードイメージを設定する。
	 *
	 * @param b ボードイメージ
	 */
	public void setBoardImage(BoardImage b) {
		this.bi = b;
	}

	/**
	 * コントロールを設定する。
	 *
	 * @param c コントロール
	 */
	public void setControl(Control c) {
		this.cl = c;
	}

	/**
	 * コンフロント・コントロールを設定する。
	 *
	 * @param c コンフロント・コントロール
	 */
	public void setConfrontControl(ConfrontControl c) {
		this.cfcl = c;
	}

	/**
	 * 石を描画する。
	 *
	 * @param c 色
	 * @param at 場所
	 */
	public void drawPiece(Color c, Point at) {
		bi.draw(c, at);
	}

	/**
	 * 盤面を描画する。
	 *
	 * @param black 黒石の配列
	 * @param white 白石の配列
	 */
	public void drawBoard(List<Point> black, List<Point> white) {
		for (Point point : black) {
			bi.draw(Color.BLACK, point);
		}

		for (Point point : white) {
			bi.draw(Color.WHITE, point);
		}
	}

	// State クラスなどからユーザ情報を取得するためのインタフェース
	/**
	 * 相手の名前を取得する。
	 *
	 * @return 相手の名前
	 */
	public String getPartnerName() {
		return name[1];
	}

	/**
	 * 自分の名前を取得する。
	 *
	 * @return 自分の名前
	 */
	public String getSelfName() {
		return name[0];
	}

	/**
	 * 相手のハンドルを取得する。
	 *
	 * @return 相手のハンドル
	 */
	public String getPartnerHandle() {
		return handle[1];
	}

	/**
	 * 自分のハンドルを取得する。
	 *
	 * @return 自分のハンドル
	 */
	public String getSelfHandle() {
		return handle[0];
	}

	/**
	 * 相手の石の色を取得する。
	 *
	 * @return 相手の石の色
	 */
	public Color getPartnerColor() {
		return partnerColor;
	}

	/**
	 * 自分の石の色を取得する。
	 *
	 * @return 自分の石の色
	 */
	public Color getSelfColor() {
		return selfColor;
	}

	// 典型的な Mediator らしいインタフェース
	// 各種 State クラス → GUIクラス
	/**
	 * メッセージAを表示する。
	 *
	 * @param s メッセージ
	 * @param c 色
	 */
	public void showMessageA(String s, Color c) {
		cl.setLabelA(s, c);
	}

	/**
	 * メッセージBを表示する。
	 *
	 * @param s メッセージ
	 */
	public void showMessageB(String s) {
		cl.setLabelB(s);
	}

	// コールバックルーチン
	// Mediator はすべてのコールバックを一元的に処理する。
	// 状態変更はマルチスレッドなので synchronized でないと危険である。

	@Override
	public synchronized void mouseClicked(MouseEvent e) {
		if (now != null && now instanceof HumanStatus && !cfcl.getButtonTaikyoku().isEnabled()) {
			int x = e.getX();
			int y = e.getY();
			// 位置 → 手に変換
			Point at = bi.normalize(x, y);
			now = now.mouse(at);
			if (now instanceof CPUStatus) {
				final int number = 48;
				final int depth = 16;
				int enemy = cfcl.getCheckboxSente().getState() ? OthelloRule.WHITE : OthelloRule.BLACK;
				if (othelloRule.getBoard().getNumber() > number) {
					shikou = new ShikouNegaMax(new PerfectEvaluator(), depth, othelloRule.getBoard(), teban);
					shikou.setTeban(enemy);
					shikou.attach(this);
				} else {
					createShikou(cfcl.getChoice().getSelectedItem(), enemy);
				}
				doShikou();
			}
		}
	}

	/**
	 * 思考を開始する。
	 */
	public void doShikou() {
		Thread thread = new Thread(shikou);
		thread.start();
	}

	/**
	 * 次の手を通知する。
	 *
	 * @param hand 手
	 */
	public void notifyNextHand(Hand hand) {
		if (now != null && now instanceof CPUStatus && hand != null) {
			Point p = hand.getP();
			now = now.hand(p);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ConfrontControl.TAIKYOKU)) {
			bi.createImage();
			othelloRule = createRule();
			createShikou(cfcl.getChoice().getSelectedItem(), OthelloRule.WHITE);

			if (cfcl.getCheckboxSente().getState()) {
				name[0] = "あなた"; handle[0] = "あなた";
				name[1] = "コンピュータ"; handle[1] = "コンピュータ";
				shikou.setTeban(OthelloRule.WHITE);
				selfColor = Color.BLACK;
				partnerColor = Color.WHITE;
				// State オブジェクトの生成
				now = createStatus(OthelloRule.BLACK);
				// 状態に応じたセットアップ
				now.doFirst();
			} else if (cfcl.getCheckboxGote().getState()) {
				name[0] = "コンピュータ"; handle[0] = "コンピュータ";
				name[1] = "あなた"; handle[1] = "あなた";
				shikou.setTeban(OthelloRule.BLACK);
				selfColor = Color.WHITE;
				partnerColor = Color.BLACK;
				// State オブジェクトの生成
				now = createStatus(OthelloRule.WHITE);
				// 状態に応じたセットアップ
				now.doFirst();
			}

			if (shikou.getTeban() == OthelloRule.BLACK) {
				doShikou();
			}

			cfcl.getCheckboxSente().setEnabled(false);
			cfcl.getCheckboxGote().setEnabled(false);
			cfcl.getButtonTaikyoku().setEnabled(false);
		} else if (e.getActionCommand().equals(ConfrontControl.PASS)) {
			// パスボタンが押下された場合
			now = now.pass(new Point());
			cfcl.getButtonPass().setEnabled(false);
			if (now != null && now instanceof CPUStatus) {
				doShikou();
			}
		} else {
			if (now != null) {
				now = now.kosan();
			}
			cfcl.getCheckboxSente().setEnabled(true);
			cfcl.getCheckboxGote().setEnabled(true);
			cfcl.getButtonTaikyoku().setEnabled(true);
		}
	}

	/**
	 * パスボタンの使用可不可を設定する。
	 *
	 * @param flag 使用可 true、そうでない場合 false
	 */
	public void setButtonPassEnable(boolean flag) {
		cfcl.getButtonPass().setEnabled(flag);
	}



	// 下請け関数
	/**
	 * State オブジェクトを生成する。
	 *
	 * @param turn 手番
	 * @return ステータス
	 */
	private Status createStatus(int turn) {
		// State クラスのオブジェクト
		Status selfS, partnerS, endS;
		// 遷移先情報テーブル
		Hashtable<String, Status> h = new Hashtable<String, Status>();

		// Abstract Factory による３つの Status の生成
		StatusFactory factory = getStatusFactory();
		selfS = factory.getSelf();
		partnerS = factory.getPartner();
		endS = factory.getEnd();

		// 遷移先情報の格納
		h.put("selfS:end", endS);
		h.put("selfS:next", partnerS);
		h.put("partnerS:end", endS);
		h.put("partnerS:next", selfS);

		// オセロルールの生成
		//OthelloRule othelloRule = createRule();

		// State オブジェクトに必要な情報を渡す。
		selfS.init(this, othelloRule, h);
		partnerS.init(this, othelloRule, h);
		endS.init(this, othelloRule, h);

		// 最初の「状態」はどちらか
		if (turn == OthelloRule.BLACK) {
			return selfS;
		} else {
			return partnerS;
		}
	}

	/**
	 * StatusFactoryを取得する。
	 *
	 * @return StatusFactory
	 */
	private StatusFactory getStatusFactory() {
		return new StatusFactory();
	}

	/**
	 * OthelloRuleを生成する。
	 *
	 * @return OthelloRule
	 */
	private OthelloRule createRule() {
		return new OthelloRule();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		createShikou(e.getItem(), OthelloRule.WHITE);
	}

	/**
	 * 思考エンジンを生成する。
	 *
	 * @param item アイテム
	 * @param turn 手順
	 */
	private void createShikou(Object item, int turn) {
		Evaluator evaluator;
		if (item.equals(ConfrontControl.LEVEL1)) {
			shikou = new ShikouRandom(othelloRule.getBoard(), turn);
			shikou.attach(this);
		} else if (item.equals(ConfrontControl.LEVEL2)) {
			final int depth = 7;
			evaluator = new NumberEvaluator();
			shikou = new ShikouNegaMax(evaluator, depth, othelloRule.getBoard(), turn);
			shikou.attach(this);
		} else if (item.equals(ConfrontControl.LEVEL3)) {
			final int depth = 7;
			evaluator = new MidEvaluator();
			shikou = new ShikouNegaMax(evaluator, depth, othelloRule.getBoard(), turn);
			shikou.attach(this);
		} else if (item.equals(ConfrontControl.LEVEL4)) {
			final int depth = 9;
			evaluator = new MidEvaluator();
			shikou = new ShikouNegaMax(evaluator, depth, othelloRule.getBoard(), turn);
			shikou.attach(this);
		} else if (item.equals(ConfrontControl.LEVEL5)) {
			final int depth = 11;
			evaluator = new MidEvaluator();
			shikou = new ShikouNegaMax(evaluator, depth, othelloRule.getBoard(), turn);
			shikou.attach(this);
		} else if (item.equals(ConfrontControl.LEVEL6)) {
			final int matchCount = 100;
			shikou = new ShikouMonteCarlo(othelloRule.getBoard(), turn, matchCount);
			shikou.attach(this);
		} else if (item.equals(ConfrontControl.LEVEL7)) {
			final int matchCount = 10000;
			shikou = new ShikouMonteCarlo(othelloRule.getBoard(), turn, matchCount);
			shikou.attach(this);
		}
	}
}
