package jp.sasyou.game.util;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * プロパティを操作するためのユーティリティ
 *
 * @author sasyou
 *
 */
public final class PropertyUtil {
	/** インスタンス */
	private static Configuration instance;

	/**
	 * コンストラクタ
	 */
	private PropertyUtil() {
		Parameters param = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
			    new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
			    .configure(param.properties()
			        .setFileName("othello.properties"));
		try {
			instance = builder.getConfiguration();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * インスタンスを取得する。
	 *
	 * @return インスタンス
	 */
	public static Configuration getInstance() {
		if (instance == null) {
			new PropertyUtil();
		}
		return instance;
	}
}
