package jp.sasyou.game.othello.client.AWT;

import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Label;
import java.awt.Panel;

import jp.sasyou.game.othello.client.Control;
import jp.sasyou.game.othello.client.Mediator;

/**
 * 単純な GUI オブジェクト管理クラス。
 * 担当は「降参ボタン」と２つのメッセージ表示領域である。
 * このクラスは「受動的」にしか動かない。
 *
 * @author sasyou
 *
 */
public class AWTControl extends Panel implements Control {
	/**
	 *
	 */
	private static final long serialVersionUID = 5005164070475429492L;

	/** メディエータ */
	private Mediator md;
	/** 降参ボタン */
	private Button but;
	private Label lab0 = new Label("●");
	private Label lab1 = new Label("　　　　　　　　　　　");
	private Label lab2 = new Label("　　　　　　　　　　　");

	/**
	 * コンストラクタ
	 *
	 * @param top
	 * @param m
	 */
	public AWTControl(Container top, Mediator md) {
		this.md = md;
		this.md.setControl(this);

		but = new Button("降参");
		add(but);

		// Mediator パターンなので、コールバックはすべて
		// Mediator クラスで集中管理する。
		but.addActionListener(this.md);

		lab1.setForeground(Color.BLACK);
		lab2.setForeground(Color.BLACK);
		add(lab0);
		add(lab1);
		add(lab2);
	}

	/**
	 * ラベル表示のためのインタフェース
	 */
	@Override
	public void setLabelA(String s, Color c) {
		lab0.setForeground(c);
		lab1.setText(s);
		lab0.repaint();
		lab1.repaint();
	}

	@Override
	public void setLabelB(String s) {
		lab2.setText(s);
		lab2.repaint();
	}

}
