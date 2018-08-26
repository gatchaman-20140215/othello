package jp.sasyou.game.othello.client.status;

import java.awt.Point;
import java.util.Hashtable;

import jp.sasyou.game.othello.client.Mediator;
import jp.sasyou.game.othello.rule.OthelloRule;

/**
 * 「状態」を表す基底クラス。<br>
 * 終了状態も兼ねる。
 *
 * @author sasyou
 *
 */
public class Status {
	// 派生クラスで共通のインスタンスを参照する。
	/** オセロのルール */
	protected OthelloRule othelloRule;
	/** メディエータ */
	protected Mediator md;

	/** 遷移先を検索するための Hashtable */
	private Hashtable<String, Status> toGo;

	/**
	 * 必要なコンテキストをセットする。<br>
	 * 特に重要なのは遷移先を格納した Hashtable である。<br>
	 * 要するに、すべての状態のインスタンスを生成してから出ないと、<br>
	 * 遷移先テーブルを与えることができない。
	 *
	 * @param md メディエータ
	 * @param othelloRule オセロのルール
	 * @param toGo 遷移先
	 */
	public final void init(Mediator md, OthelloRule othelloRule, Hashtable<String, Status> toGo) {
		this.md = md;
		this.othelloRule = othelloRule;
		this.toGo = toGo;
	}

	/**
	 * 遷移先を取得する。<br>
	 * State パターンで遷移先を与える手段はいろいろあるが、Hashtable での<br>
	 * 文字列検索が一番穏当だろう。
	 *
	 * @param s 文字列
	 * @return ステータス
	 */
	protected final Status getNext(String s) {
		if (toGo == null) {
			return this;
		}
		Status ret = (Status) toGo.get(s);
		if (ret == null) {
			return this;
		} else {
			return ret;
		}
	}

	// 以下、派生クラスで実装すべきメソッド
	/**
	 * 初期化を行う。
	 */
	public void doFirst() {

	}

	/**
	 * 終了状態かどうかを返す。
	 *
	 * @return 終了状態の場合 true、そうでない場合 false
	 */
	public boolean isEnd() {
		return true;
	}

	// これらはクライアントのアクションに応じて、
	// 「状態」に応じた処理と遷移を担当する。
	/**
	 * 降参する。
	 *
	 * @return ステータス
	 */
	public Status kosan() {
		return this;
	}

	/**
	 * 指定した場所にマウスを当てる。
	 *
	 * @param at 場所
	 * @return ステータス
	 */
	public Status mouse(Point at) {
		return this;
	}

	/**
	 * パスする。
	 *
	 * @param at 場所
	 * @return ステータス
	 */
	public Status pass(Point at) {
		return this;

	}

	/**
	 * 手を打つ。
	 *
	 * @param at 場所
	 * @return ステータス
	 */
	public Status hand(Point at) {
		return this;
	}
}
