package jp.sasyou.game.othello.client;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;

/**
 * 対戦設定用コンテナ
 *
 * @author sasyou
 *
 */
public interface ConfrontControl {

	/** 先手 */
	String SENTE = "先手";
	/** 後手 */
	String GOTE = "後手";

	/** 対局 */
	String TAIKYOKU = "対局";
	/** パス */
	String PASS = "パス";

	/** レベル１ */
	String LEVEL1 = "レベル１(最弱)";
	/** レベル２ */
	String LEVEL2 = "レベル２(弱)";
	/** レベル３ */
	String LEVEL3 = "レベル３(普通)";
	/** レベル４ */
	String LEVEL4 = "レベル４(強)";
	/** レベル５ */
	String LEVEL5 = "レベル５(最強)";
	/** レベル６ */
	String LEVEL6 = "レベル６(モンテカルロ弱)";
	/** レベル７ */
	String LEVEL7 = "レベル７(モンテカルロ強)";

	/**
	 * 先手用チェックボックスを取得する。
	 *
	 * @return 先手用チェックボックス
	 */
	Checkbox getCheckboxSente();

	/**
	 * 後手用チェックボックスを取得する。
	 *
	 * @return 後手用チェックボックス
	 */
	Checkbox getCheckboxGote();

	/**
	 * 対局ボタンを取得する。
	 *
	 * @return 対局ボタン
	 */
	Button getButtonTaikyoku();

	/**
	 * 選択コンポーネントを取得する。
	 *
	 * @return 選択コンポーネント
	 */
	Choice getChoice();

	/**
	 * パスボタンを取得する。
	 *
	 * @return パスボタン
	 */
	Button getButtonPass();
}
