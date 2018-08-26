package jp.sasyou.game.othello.client.AWT;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import jp.sasyou.game.othello.client.BoardImage;
import jp.sasyou.game.othello.client.Mediator;

/**
 * 単純な GUI オブジェクト管理クラス。
 * 担当はオセロの盤面である。
 * このクラスは「受動的」にしか動かない。
 *
 * @author sasyou
 *
 */
public class AWTBoardImage extends Canvas implements BoardImage {
	/**
	 *
	 */
	private static final long serialVersionUID = -5606145883828989923L;

	// Canvas 拡張
	/** メディエータ */
	private Mediator md;
	/** 盤面イメージ */
	private Image img;
	private Graphics gi;

	private int start = GRID_SIZE / 2;

	public static final Color boardColor = new Color(0x00, 0xc0, 0x00);
	private static final Dimension size = new Dimension(BOARD_SIZE, BOARD_SIZE);

	public AWTBoardImage(Container top, Mediator md) {
		// 名刺交換
		this.md = md;
		this.md.setBoardImage(this);

		// Mediator パターンなので、コールバックはすべて
		// Mediator クラスで集中管理する。
		addMouseListener(this.md);
		top.add(this);
	}

	@Override
	public void createImage() {
		int end = start + GRID_SIZE * 8;

		if (img == null) {
			img = createImage(BOARD_SIZE, BOARD_SIZE);
		}
		// 以下盤面の準備
		gi = img.getGraphics();
		gi.setColor(boardColor);
		gi.fillRect(0, 0, BOARD_SIZE, BOARD_SIZE);

		gi.setColor(Color.BLACK);
		for (int i = 0; i < 9; i++) {
			gi.drawLine(start + GRID_SIZE * i, start, start + GRID_SIZE * i, end);
			gi.drawLine(start, start + GRID_SIZE * i, end, start + GRID_SIZE * i);
		}
		drawDot(2, 2);
		drawDot(2, 6);
		drawDot(6, 2);
		drawDot(6, 6);

		// 最初の石
		draw(Color.WHITE, new Point(3, 3));
		draw(Color.WHITE, new Point(4, 4));
		draw(Color.BLACK, new Point(3, 4));
		draw(Color.BLACK, new Point(4, 3));

		repaint();
	}

	private void drawDot(int i, int j) {
		int x = GRID_SIZE / 2 + GRID_SIZE * i;
		int y = GRID_SIZE / 2 + GRID_SIZE * j;
		gi.fillArc(x - DOT_SIZE, y - DOT_SIZE, DOT_SIZE * 2, DOT_SIZE * 2, 0, 360);
	}

	/**
	 * 受動的に「手」の位置を石に描く。
	 *
	 * @param c
	 * @param at
	 */
	@Override
	public void draw(Color c, Point at) {
		int x = start + at.x * GRID_SIZE;
		int y = start + at.y * GRID_SIZE;
		gi.setColor(c);
		gi.fillArc(x, y, GRID_SIZE, GRID_SIZE, 0, 360);
		repaint(10);
	}

	/**
	 * 座標値から「手」に変換する。
	 *
	 * @param x
	 * @param y
	 */
	@Override
	public Point normalize(int x, int y) {
		if (x < start || y < start) {
			return null;
		}
		x = (x - start) / GRID_SIZE;
		y = (y - start) / GRID_SIZE;
		if (x < 0 || y < 0 || x >= 8 || y >= 8) {
			return null;
		}
		return new Point(x, y);
	}

	// 以降は Canvas 拡張の常識
	public void paint(Graphics g) {
		if (img != null) {
			g.drawImage(img, 0, 0, this);
		}
	}

	public void update(Graphics g) {
		paint(g);
	}

	public Dimension getPreferredSize() {
		return size;
	}
}
