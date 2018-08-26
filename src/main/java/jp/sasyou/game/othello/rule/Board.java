package jp.sasyou.game.othello.rule;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static jp.sasyou.game.othello.rule.OthelloRule.*;

/**
 * オセロの盤
 *
 * @author sasyou
 *
 */
public final class Board implements Cloneable {
	/** 盤のサイズ */
	public static final int BOARD_SIZE = 8;

	/** 盤面表示用文字列 */
	public static final String[][] BOARD_STR = {
			{"a", "b", "c", "d", "e", "f", "g", "h"},
			{"1", "2", "3", "4", "5", "6", "7", "8"}
	};

	/** 黒石の初期配置 */
	private static final long INITIAL_BLACK = 0x0000000810000000L;
	/** 白石の初期配置 */
	private static final long INITIAL_WHITE = 0x0000001008000000L;

	/** ビットマスク（周囲） */
	private static final long MASK_EDGE = 0x007E7E7E7E7E7E00L;
	/** ビットマスク（垂直） */
	private static final long MASK_VERTICAL = 0x7E7E7E7E7E7E7E7EL;
	/** ビットマスク（水平） */
	private static final long MASK_HORIZONTAL = 0x00FFFFFFFFFFFF00L;
	/** 方向（左斜め） */
	private static final int DIR_LEFT_OBLIQUE = 9;
	/** 方向（右斜め） */
	private static final int DIR_RIGHT_OBLIQUE = 7;
	/** 方向（水平） */
	private static final int DIR_HORIZONTAL = 1;
	/** 方向（垂直） */
	private static final int DIR_VERTICAL = 8;

	/** 盤面 */
	private long[] banmen = new long[2];

	/** 盤面履歴 */
	private List<BoardData> history = new ArrayList<>();

	/**
	 * コンストラクタ
	 */
	public Board() {
		init();
	}

	/**
	 * コンストラクタ
	 *
	 * @param banmen 盤面
	 */
	public Board(long[] banmen) {
		this.banmen[BLACK] = banmen[BLACK];
		this.banmen[WHITE] = banmen[WHITE];

		history.add(new BoardData(banmen));
	}

	/**
	 * コンストラクタ
	 *
	 * @param black 黒石
	 * @param white 白石
	 */
	public Board(long black, long white) {
		this.banmen[BLACK] = black;
		this.banmen[WHITE] = white;

		history.add(new BoardData(banmen));
	}

	/**
	 * 盤を初期化する。
	 */
	public void init() {
		banmen[BLACK] = INITIAL_BLACK;
		banmen[WHITE] = INITIAL_WHITE;

		history.add(new BoardData(banmen));
	}

	/**
	 * 指定された場所に石を置く。
	 *
	 * @param p 場所
	 * @param teban 手番
	 * @return 裏返すことのできる場所のリスト
	 */
	public List<Point> putPiece(Point p, int teban) {
		return putPiece(convertPos(p), teban);
	}

	/**
	 * 指定された場所に石を置く。
	 *
	 * @param move 場所
	 * @param teban 手番
	 * @return 裏返すことのできる場所のリスト
	 */
	public List<Point> putPiece(long move, int teban) {
		long flipped = 0;

		flipped |= generateSomeFlipped(banmen[teban], banmen[teban ^ 1] & MASK_EDGE, move, DIR_LEFT_OBLIQUE);
		flipped |= generateSomeFlipped(banmen[teban], banmen[teban ^ 1] & MASK_EDGE, move, DIR_RIGHT_OBLIQUE);
		flipped |= generateSomeFlipped(banmen[teban], banmen[teban ^ 1] & MASK_VERTICAL, move, DIR_HORIZONTAL);
		flipped |= generateSomeFlipped(banmen[teban], banmen[teban ^ 1] & MASK_HORIZONTAL, move, DIR_VERTICAL);

		banmen[teban] = banmen[teban] | move | flipped;
		banmen[teban ^ 1] = banmen[teban ^ 1] ^ flipped;

		history.add(new BoardData(banmen));
		return convertPointList(flipped);
	}

	/**
	 * 一手戻す。
	 */
	public void undo() {
		history.remove(history.size() - 1);
		BoardData boardData = history.get(history.size() - 1);

		banmen[BLACK] = boardData.banmen[BLACK];
		banmen[WHITE] = boardData.banmen[WHITE];
	}

	/**
	 * ゲームが終了しているか、確認する。
	 *
	 * @return true：ゲーム終了、false：ゲーム終了ではない
	 */
	public boolean isGameOver() {
		if ((banmen[BLACK] & banmen[WHITE]) == 0xFFFFFFFFFFFFFFFFL) {
			return true;
		}

		if (canMove(BLACK) || canMove(WHITE)) {
			return false;
		}

		return true;
	}

	/**
	 * 置かれた石の数を取得する。
	 *
	 * @return 置かれた石の数
	 */
	public int getNumber() {
		return Long.bitCount(banmen[BLACK] | banmen[WHITE]);
	}

	/**
	 * 白の石がいくつあるか数える。
	 *
	 * @return 白石の数
	 */
	public int getNumberOfWhite() {
		return Long.bitCount(banmen[WHITE]);
	}

	/**
	 * 黒の石がいくつあるか数える。
	 *
	 * @return 黒石の数
	 */
	public int getNumberOfBlack() {
		return Long.bitCount(banmen[BLACK]);
	}

	/**
	 * 盤をコピーする。
	 *
	 * @return 盤
	 */
	public Object clone() {
		Board board = new Board();
		board.banmen[BLACK] = banmen[BLACK];
		board.banmen[WHITE] = banmen[WHITE];
		return board;
	}

	/**
	 * 盤を文字列化する。
	 *
	 * @param teban 手番
	 * @return 文字列化された盤
	 */
	public String toString(int teban) {
		StringBuffer sb = new StringBuffer();
		sb.append(teban == BLACK ? "手番：黒\n" : "手番：白\n");
		sb.append("   a b c d e f g h\n");

		for (int y = 0; y < BOARD_SIZE; y++) {
			sb.append("  " + (y + 1));
			for (int x = 0; x < BOARD_SIZE; x++) {
				long pos = 0x01L << (BOARD_SIZE - 1 - x) + (BOARD_SIZE - 1 - y) * BOARD_SIZE;
				if ((banmen[BLACK] & pos) != 0) {
					sb.append("●");
				} else if ((banmen[WHITE] & pos) != 0) {
					sb.append("○");
				} else {
					sb.append("  ");
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * 棋譜テキストを読み込む。
	 *
	 * @param path ファイルパス
	 */
	public void readKifuText(String path) {
		banmen[BLACK] = 0;
		banmen[WHITE] = 0;

		try {
			File file = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(file));

			String line, discStr;
			int x = 0;
			while ((line = br.readLine()) != null) {
				line = line.replaceFirst("^\\s+", "").replaceAll("  ", "　");
				if (line.substring(0, 1).equals("a")) {
					continue;
				}

				x++;
				for (int y = 1; y <= BOARD_SIZE; y++) {
					discStr = line.substring(y, y + 1);
					if (discStr.equals("●")) {
						banmen[BLACK] |= 1L << (BOARD_SIZE * (BOARD_SIZE - x)) + (BOARD_SIZE - y);
					} else if (discStr.equals("○")) {
						banmen[WHITE] |= 1L << (BOARD_SIZE * (BOARD_SIZE - x)) + (BOARD_SIZE - y);
					}
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		history.add(new BoardData(banmen));
	}

	/**
	 * 石を置けるかどうか、判定する。
	 *
	 * @param p 場所
	 * @param teban 手番
	 * @return 石を置ける場合 true、そうでない場合 false
	 */
	public boolean canPutPiece(Point p, int teban) {
		return (generateLegals(teban) & convertPos(p)) != 0;
	}

	/**
	 * 盤面を取得する。
	 * @return 盤面
	 */
	public long[] getBanmen() {
	    return banmen;
	}

	/**
	 * 指定した色の石の配置を返す。
	 *
	 * @param piece 石の色
	 * @return 石の配置
	 */
	public List<Point> getPointList(int piece) {
		return convertPointList(banmen[piece]);
	}

	/**
	 * 盤面が埋め尽くされているか判定する。
	 *
	 * @return true：埋め尽くされている、false：埋め尽くされていない
	 */
	public boolean isBoardFull() {
		return (banmen[BLACK] | banmen[WHITE]) == 0xFFFFFFFFFFFFFFFFL;
	}

	/**
	 * 合法手が存在するか、判定する。
	 *
	 * @param teban 手番
	 * @return true：存在する、false：存在しない。
	 */
	public boolean canMove(int teban) {
		long empty = ~(banmen[0] | banmen[1]);

		return ((generateSomeLeagals(banmen[teban], banmen[teban ^ 1] & MASK_EDGE, DIR_LEFT_OBLIQUE) & empty) != 0
				|| (generateSomeLeagals(banmen[teban], banmen[teban ^ 1] & MASK_EDGE, DIR_RIGHT_OBLIQUE) & empty) != 0
				|| (generateSomeLeagals(banmen[teban], banmen[teban ^ 1] & MASK_VERTICAL, DIR_HORIZONTAL) & empty) != 0
				|| (generateSomeLeagals(banmen[teban], banmen[teban ^ 1] & MASK_HORIZONTAL, DIR_VERTICAL) & empty) != 0
				);
	}

	/**
	 * 合法手を生成する。
	 *
	 * @param teban 手番
	 * @return 合法手
	 */
	public long generateLegals(int teban) {
		return ~(banmen[0] | banmen[1])
					& (generateSomeLeagals(banmen[teban], banmen[teban ^ 1] & MASK_EDGE, DIR_LEFT_OBLIQUE)
					 | generateSomeLeagals(banmen[teban], banmen[teban ^ 1] & MASK_EDGE, DIR_RIGHT_OBLIQUE)
					 | generateSomeLeagals(banmen[teban], banmen[teban ^ 1] & MASK_VERTICAL, DIR_HORIZONTAL)
					 | generateSomeLeagals(banmen[teban], banmen[teban ^ 1] & MASK_HORIZONTAL, DIR_VERTICAL));
	}

	/**
	 * 盤面左端の指定した石の並びを取得する。
	 *
	 * @param piece 石の色
	 * @return 石の並び
	 */
	public int getLeftEdge(int piece) {
		return reverseByte((byte) ((banmen[piece] & 0x8000000000000000L) >>> (56 + 7)))
				| reverseByte((byte) ((banmen[piece] & 0x0080000000000000L) >>> (48 + 6)))
				| reverseByte((byte) ((banmen[piece] & 0x0000800000000000L) >>> (40 + 5)))
				| reverseByte((byte) ((banmen[piece] & 0x0000008000000000L) >>> (32 + 4)))
				| reverseByte((byte) ((banmen[piece] & 0x0000000080000000L) >>> (24 + 3)))
				| reverseByte((byte) ((banmen[piece] & 0x0000000000800000L) >>> (16 + 2)))
				| reverseByte((byte) ((banmen[piece] & 0x0000000000008000L) >>> (8 + 1)))
				| reverseByte((byte) ((banmen[piece] & 0x0000000000000080L)));
	}

	/**
	 * 盤面右端の指定した石の並びを取得する。
	 *
	 * @param piece 石の色
	 * @return 石の並び
	 */
	public int getRightEdge(int piece) {
		return (int) (((banmen[piece] & 0x0100000000000000L) >>> (56 - 7))
				| ((banmen[piece] & 0x0001000000000000L) >>> (48 - 6))
				| ((banmen[piece] & 0x0000010000000000L) >>> (40 - 5))
				| ((banmen[piece] & 0x0000000100000000L) >>> (32 - 4))
				| ((banmen[piece] & 0x0000000001000000L) >>> (24 - 3))
				| ((banmen[piece] & 0x0000000000010000L) >>> (16 - 2))
				| ((banmen[piece] & 0x0000000000000100L) >>> (8 - 1))
				| ((banmen[piece] & 0x0000000000000001L)));
	}

	/**
	 * 盤面の上端の指定した石の並びを取得する。
	 *
	 * @param piece 石の色
	 * @return 石の並び
	 */
	public int getUpperEdge(int piece) {
		return (int) ((banmen[piece] & 0xFF00000000000000L) >>> 56);
	}

	/**
	 * 盤面の下端の指定した石の並びを取得する。
	 *
	 * @param piece 石の色
	 * @return 石の並び
	 */
	public int getLowerEdge(int piece) {
		return (int) (banmen[piece] & 0x00000000000000FFL);
	}

	/**
	 * 四隅の石を取得する。
	 *
	 * @param piece 石の色
	 * @return 四隅の石
	 */
	public long getCorner(int piece) {
		final long mask = 0x8100000000000081L;
		return banmen[piece] & mask;
	}

	/**
	 * byte型のビット配列を逆転させる。
	 *
	 * @param b バイト値
	 * @return 反転したバイト値
	 */
	private byte reverseByte(byte b) {
		b = (byte) ((b & 0x55) << 1 | (b >>> 1) & 0x55);
		b = (byte) ((b & 0x33) << 2 | (b >>> 2) & 0x33);
		b = (byte) ((b & 0x0F) << 4 | (b >>> 4) & 0x0f);
		return (byte) b;
	}

	/**
	 * 指定された方向に対する合法手を生成する。
	 *
	 * @param own 黒石
	 * @param maskedEnemy 白石
	 * @param dir 方向
	 * @return 合法手
	 */
	private long generateSomeLeagals(long own, long maskedEnemy, int dir) {
		long flipped = ((own << dir) | (own >>> dir)) & maskedEnemy;
		for (int i = 0; i < BOARD_SIZE - 2; i++) {
			flipped |= ((flipped << dir) | (flipped >>> dir)) & maskedEnemy;
		}
		return (flipped << dir) | (flipped >>> dir);
	}

	/**
	 * 指定された方向の石を裏返す。
	 *
	 * @param own 黒石
	 * @param maskedEnemy 白石
	 * @param move 手
	 * @param dir 方向
	 * @return 結果
	 */
	private long generateSomeFlipped(long own, long maskedEnemy, long move, int dir) {
		long flipped = 0L;
		long m2, m3, m4, m5, m6;
		long m1 = move >>> dir;
		if ((m1 & maskedEnemy) != 0) {
			if (((m2 = m1 >>> dir) & maskedEnemy) == 0) {
				if ((m2 & own) != 0) {
					flipped |= m1;
				}
			} else if (((m3 = m2 >>> dir) & maskedEnemy) == 0) {
				if ((m3 & own) != 0) {
					flipped |= m1 | m2;
				}
			} else if (((m4 = m3 >>> dir) & maskedEnemy) == 0) {
				if ((m4 & own) != 0) {
					flipped |= m1 | m2 | m3;
				}
			} else if (((m5 = m4 >>> dir) & maskedEnemy) == 0) {
				if ((m5 & own) != 0) {
					flipped |= m1 | m2 | m3 | m4;
				}
			} else if (((m6 = m5 >>> dir) & maskedEnemy) == 0) {
				if ((m6 & own) != 0) {
					flipped |= m1 | m2 | m3 | m4 | m5;
				}
			} else {
				if (((m6 >>> dir) & own) != 0) {
					flipped |= m1 | m2 | m3 | m4 | m5 | m6;
				}
			}
		}

		long m10, m11, m12, m13, m14;
		long m9 = move << dir;
		if ((m9 & maskedEnemy) != 0) {
			if (((m10 = m9 << dir) & maskedEnemy) == 0) {
				if ((m10 & own) != 0) {
					flipped |= m9;
				}
			} else if (((m11 = m10 << dir) & maskedEnemy) == 0) {
				if ((m11 & own) != 0) {
					flipped |= m9 | m10;
				}
			} else if (((m12 = m11 << dir) & maskedEnemy) == 0) {
				if ((m12 & own) != 0) {
					flipped |= m9 | m10 | m11;
				}
			} else if (((m13 = m12 << dir) & maskedEnemy) == 0) {
				if ((m13 & own) != 0) {
					flipped |= m9 | m10 | m11 | m12;
				}
			} else if (((m14 = m13 << dir) & maskedEnemy) == 0) {
				if ((m14 & own) != 0) {
					flipped |= m9 | m10 | m11 | m12 | m13;
				}
			} else {
				if (((m14 << dir) & own) != 0) {
					flipped |= m9 | m10 | m11 | m12 | m13 | m14;
				}
			}
		}

		return flipped;
	}

	/**
	 * Pointをビット形式に変換する。
	 *
	 * @param p Point
	 * @return ビット形式の手
	 */
	private long convertPos(Point p) {
		return 1L << ((BOARD_SIZE - 1 - p.x) + (BOARD_SIZE - 1 - p.y) * BOARD_SIZE);
	}

	/**
	 * 石の配置を示した値をPointのリストに変換する。
	 *
	 * @param discs 石の配置を示したlong値
	 * @return Pointのリスト
	 */
	private List<Point> convertPointList(long discs) {
		List<Point> ret = new ArrayList<>();
		final long mask = 0x8000000000000000L;
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				if ((discs & mask) != 0) {
					ret.add(new Point(x, y));
				}
				discs = discs << 1;
			}
		}

		return ret;
	}

	/**
	 * 盤面データ
	 *
	 * @author sashou
	 *
	 */
	class BoardData {
		/** 盤面 */
		private long[] banmen = new long[2];

		/**
		 * コンストラクタ
		 *
		 * @param board 盤面
		 */
		BoardData(long[] board) {
			this.banmen[BLACK] = board[BLACK];
			this.banmen[WHITE] = board[WHITE];
		}
	}
}
