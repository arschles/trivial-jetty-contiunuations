package com.arschles.github.experiment

import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import org.eclipse.jetty.server.{Server => JettyServer}

/**
 * Created by IntelliJ IDEA.
 *
 * com.arschles.github.trivial-jetty-continuations
 *
 * User: aaron
 * Date: 4/19/13
 * Time: 6:35 PM
 */
object Server {
  private lazy val logger = LoggerFactory.getLogger(Server.getClass)
  def main(args: Array[String]) {
    val svr = new JettyServer(new InetSocketAddress("localhost", 8080))
    svr.setHandler(new Handler())
    logger.info("Starting server on port 8080")
    svr.start()
    svr.join()

  }

}
