package jp.sasyou.game.othello.client;

import java.awt.Color;
import java.awt.Point;

/**
 * 盤面
 *
 * @author sasyou
 *
 */
public interface BoardImage {
	/** グリッドの大きさ */
	int GRID_SIZE = 50;
	/** 盤の大きさ */
	int BOARD_SIZE = GRID_SIZE * 9;
	/** 点の大きさ */
	int DOT_SIZE = 3;

	/**
	 * 適当なタイミングで実イメージを生成する。
	 * アプレットの場合はいきなり実イメージを生成しても問題がないが、
	 * スタンドアロンの場合には、インスタンス生成時にはまだウィジットが
	 * リアライズされていないので、実イメージの生成に失敗する。
	 * だから、リアライズ後の適当なタイミングでこれを呼び出して、
	 * 盤面の実イメージを生成する。
	 */
	void createImage();

	/**
	 * ウィンドウ上のクリック位置から、盤面の場所の値へ変換する。
	 *
	 * @param x X座標
	 * @param y Y座標
	 * @return 場所
	 */
	Point normalize(int x, int y);

	/**
	 * 盤面の指定の位置に指定の色で石を表示する。
	 *
	 * @param c 色
	 * @param at 場所
	 */
	void draw(Color c, Point at);
}
