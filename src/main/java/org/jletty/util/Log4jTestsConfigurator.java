/*
 * Created on 13-mar-2005
 *
 */
package org.jletty.util;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author Ruben
 * 
 */
public class Log4jTestsConfigurator {
	public static void configure() {
		Properties props = new Properties();

		props.put("log4j.appender.Console", "org.apache.log4j.ConsoleAppender");
		props.put("log4j.appender.Console.layout",
				"org.apache.log4j.PatternLayout");
		props.put("log4j.appender.Console.layout.ConversionPattern",
				"%-4r [%t] %-5p %c %x - %m%n");
		props.put("log4j.logger.SchemaParsing", "OFF,Console");
		props.put("log3j.additivity.SchemaParsing", "false");
		props.put("log4j.rootLogger", "OFF,Console");

		PropertyConfigurator.configure(props);
	}
}
