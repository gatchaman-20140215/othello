package jp.sasyou.game.othello.client;

import java.awt.Color;

/**
 * 上部コンテナ
 *
 * @author sasyou
 *
 */
public interface Control {
	/**
	 * ラベルの左側を表示する。
	 * これは先頭の●の色を指定することができる。
	 *
	 * @param s 文字列
	 * @param c 色
	 */
	void setLabelA(String s, Color c);

	/**
	 * ラベルの右側を表示する。
	 *
	 * @param s 文字列
	 */
	void setLabelB(String s);
}
