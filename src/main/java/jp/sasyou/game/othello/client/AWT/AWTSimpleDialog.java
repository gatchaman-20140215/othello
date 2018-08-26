package jp.sasyou.game.othello.client.AWT;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jp.sasyou.game.othello.client.JumpDialog;

/**
 * 終了のダイアログを表示し、アプレットのページを切り替える。
 * 親クラスが Applet でもそうでなくても動作するようにする。
 *
 * @author sasyou
 *
 */
public class AWTSimpleDialog implements ActionListener, JumpDialog {
	/** GUI コンポーネント */
	private Dialog dialog;

	/** 引数によって固定される */
	private Label lab1;
	/** setLabel() によって後で変更可能 */
	private Label lab2;

	/**
	 * コンストラクタ
	 *
	 * @param title
	 * @param message
	 */
	public AWTSimpleDialog(String title, String message) {
		if (message == null) {
			message = "ゲームは終了しました。";
		}

		// ダイアログ自体の設定
		dialog = new Dialog(new Frame(), true);
		dialog.setBackground(Color.WHITE);
		dialog.setTitle(title);

		lab1 = new Label(message);
		lab2 = new Label();

		GridBagLayout gl = new GridBagLayout();
		dialog.setLayout(gl);

		GridBagConstraints gc1 = new GridBagConstraints();
		gc1.gridx = 0;
		gc1.gridy = 0;
		gc1.anchor = GridBagConstraints.CENTER;
		gl.setConstraints(lab1, gc1);
		dialog.add(lab1);

		GridBagConstraints gc2 = new GridBagConstraints();
		gc2.gridx = 0;
		gc2.gridy = 1;
		gc2.anchor = GridBagConstraints.CENTER;
		gl.setConstraints(lab2, gc2);
		dialog.add(lab2);

		Button but = new Button("OK");
		GridBagConstraints gc3 = new GridBagConstraints();
		gc3.gridx = 0;
		gc3.gridy = 2;
		gc3.anchor = GridBagConstraints.CENTER;
		gl.setConstraints(but, gc3);
		dialog.add(but);

		but.addActionListener(this);
	}

	/**
	 * 第２ラベルを設定する。
	 *
	 * @param message
	 */
	public void setLabel(String message) {
		lab2.setText(message);
	}

	/**
	 * 表示位置を設定する。
	 *
	 * @param x
	 * @param y
	 */
	protected void setLocation(int x, int y) {
		dialog.setLocation(x, y);
	}

	/**
	 * OK ボタンがクリックされたら
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		dispose();
	}

	/**
	 * ダイアログを閉じる。
	 */
	@Override
	public void dispose() {
		dialog.dispose();
	}

	/**
	 * ダイアログを表示する。
	 */
	@Override
	public void show() {
		dialog.pack();
		dialog.setVisible(true);
		dialog.toFront();
	}

}
