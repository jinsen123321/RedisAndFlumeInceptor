package com.datasw.flume;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author jinsen
 * @create 2020-04-01 18:12
 */
public class test {
    public static void main(String[] args) {
//        Ipconfig.setIp();
        System.out.println("xx");
        Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
        logger.trace("trace level");
        logger.debug("debug level");
        logger.info("info level");
        logger.warn("warn level");
        logger.error("error level");
        logger.fatal("fatal level");
    }
}
