package jp.sasyou.game.othello.client.AWT;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.Panel;

import jp.sasyou.game.othello.client.ConfrontControl;
import jp.sasyou.game.othello.client.Mediator;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.GridBagConstraints;
import java.awt.Choice;
import java.awt.Button;

public class AWTConfrontControl extends Panel implements ConfrontControl {

	/**
	 *
	 */
	private static final long serialVersionUID = 4997620254177603680L;

	/** メディエータ */
	private Mediator md;

	private Checkbox checkboxSente = null;

	private Checkbox checkboxGote = null;

	private Choice choice = null;

	private Button buttonTaikyoku = null;

	private Button buttonPass = null;

	private CheckboxGroup cbg = null;

	/**
	 * This is the default constructor
	 */
	public AWTConfrontControl(Container top, Mediator md) {
		this.md = md;
		this.md.setConfrontControl(this);

		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		cbg = new CheckboxGroup();

		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 4;
		gridBagConstraints4.gridy = 0;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 3;
		gridBagConstraints3.gridy = 0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 2;
		gridBagConstraints2.gridy = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.add(getCheckboxSente(), gridBagConstraints);
		this.add(getCheckboxGote(), gridBagConstraints1);
		this.add(getChoice(), gridBagConstraints2);
		this.add(getButtonTaikyoku(), gridBagConstraints3);
		this.add(getButtonPass(), gridBagConstraints4);
	}

	/**
	 * This method initializes checkbox
	 *
	 * @return java.awt.Checkbox
	 */
	public Checkbox getCheckboxSente() {
		if (checkboxSente == null) {
			checkboxSente = new Checkbox();
			checkboxSente.setLabel(SENTE);
			checkboxSente.setState(true);
			checkboxSente.setCheckboxGroup(cbg);
		}
		return checkboxSente;
	}

	/**
	 * This method initializes checkbox1
	 *
	 * @return java.awt.Checkbox
	 */
	public Checkbox getCheckboxGote() {
		if (checkboxGote == null) {
			checkboxGote = new Checkbox();
			checkboxGote.setLabel(GOTE);
			checkboxGote.setCheckboxGroup(cbg);
		}
		return checkboxGote;
	}

	/**
	 * This method initializes choice
	 *
	 * @return java.awt.Choice
	 */
	public Choice getChoice() {
		if (choice == null) {
			choice = new Choice();
			choice.addItemListener(md);
			choice.addItem(LEVEL1);
			choice.addItem(LEVEL2);
			choice.addItem(LEVEL3);
			choice.addItem(LEVEL4);
			choice.addItem(LEVEL5);
			choice.addItem(LEVEL6);
			choice.addItem(LEVEL7);
		}
		return choice;
	}

	/**
	 * This method initializes button
	 *
	 * @return java.awt.Button
	 */
	public Button getButtonTaikyoku() {
		if (buttonTaikyoku == null) {
			buttonTaikyoku = new Button();
			buttonTaikyoku.setLabel(TAIKYOKU);
			buttonTaikyoku.addActionListener(md);
		}
		return buttonTaikyoku;
	}

	/**
	 * This method initializes button
	 *
	 * @return java.awt.Button
	 */
	public Button getButtonPass() {
		if (buttonPass == null) {
			buttonPass = new Button();
			buttonPass.setLabel(PASS);
			buttonPass.setEnabled(false);
			buttonPass.addActionListener(md);
		}
		return buttonPass;
	}
}
