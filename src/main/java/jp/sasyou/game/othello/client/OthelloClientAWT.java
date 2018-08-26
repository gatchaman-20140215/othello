package jp.sasyou.game.othello.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * AWTを使用したオセロ
 *
 * @author sasyou
 *
 */
public final class OthelloClientAWT extends OthelloClient implements OthelloMain {
	/** メディエータ */
	private Mediator md;
	/** 盤面を表示するコンテナ */
	private ScrollPane sp;
	/** 盤面 */
	private BoardImage bi;
	/** 降参ボタンとメッセージ */
	private Control cl;
	/** 対戦コントロール */
	private ConfrontControl cfcl;
	/** トップレベル */
	private Frame top;
	/** 選択された GUI */
	protected GUIFactory factory;

	/**
	 * main関数
	 *
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		new OthelloClientAWT(args);
	}

	/**
	 * コンストラクタ
	 *
	 * @param args 引数
	 */
	public OthelloClientAWT(String[] args) {
		super(args);
		factory = getFactory();
		top = new Frame();
		top.setTitle(factory.getTitle());
		top.setSize(factory.getSize());
		top.setBackground(Color.LIGHT_GRAY);

		top.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images.jpg")));

		// 閉じるボタンで終われるようにする。
		top.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				stop();
			}
		});

		// メディエータと GUI オブジェクトの生成
		md = factory.getMediator(this);
		bi = factory.getBoardImage(top, md);

		sp = new ScrollPane();
		sp.add((Component) bi);
		cl = factory.getControl(top, md);
		top.setLayout(new BorderLayout());
		top.add((Component) cl, "North");
		top.add(sp, "Center");
		cfcl = factory.getConfrontControl(top, md);
		top.add((Component) cfcl, "South");

		top.setVisible(true);
		bi.createImage();
		md.start();
	}

	/**
	 * GUIFactory を取得する。
	 * 他ゲーム転用のための使用。上書きを可能にする Factory Method である。
	 *
	 * @return GUIFactory
	 */
	public GUIFactory getFactory() {
		return new GUIFactory();
	}

	@Override
	public Point getLocation() {
		return top.getLocation();
	}

	@Override
	public void showDialog(String message1, String message2) {
		JumpDialog jd = factory.getPassDialog(this, message1, message2);
		jd.show();
	}

	/**
	 * ゲームを終了する。
	 */
	@Override
	public void stop() {
		md.stopped();
		System.exit(0);
	}
}
