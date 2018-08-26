package jp.sasyou.game.othello.client.status;

/**
 * Status クラスの実装を返す。<br>
 * 典型的な Abstract Factory
 *
 * @author sasyou
 *
 */
public final class StatusFactory {
	/**
	 * 「自分の番」の Status
	 *
	 * @return ステータス
	 */
	public Status getSelf() {
		return new HumanStatus();
	}

	/**
	 * 「相手の番」の Status
	 *
	 * @return ステータス
	 */
	public Status getPartner() {
		return new CPUStatus();
	}

	/**
	 * 「終了状態」の Status
	 *
	 * @return ステータス
	 */
	public Status getEnd() {
		return new Status();
	}
}
