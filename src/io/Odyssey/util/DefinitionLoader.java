package io.Odyssey.util;

import io.Odyssey.net.ChannelHandler;
import io.Odyssey.util.logging.Log;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DefinitionLoader implements Runnable {

   // private static final Log logger = LogManager.getLogger(com.ferox.game.world.definition.loader.DefinitionLoader.class);
    private static final Logger logger = LoggerFactory.getLogger(DefinitionLoader.class);
    public abstract void load() throws Exception;

    public abstract String file();

    @Override
    public void run() {
        String file = file();
        try {
            long start = System.currentTimeMillis();
            load();
            long elapsed = System.currentTimeMillis() - start;
            logger.info("Loaded definitions for {}. It took {}ms.", file, elapsed);
        } catch (Throwable e) {
      //      logger.fatal(new ParameterizedMessage("Could not load definition for {}", file), e);
        }
    }
}
