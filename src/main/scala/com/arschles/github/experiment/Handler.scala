package com.arschles.github.experiment

import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.Request
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.eclipse.jetty.continuation.ContinuationSupport
import com.stackmob.newman._
import com.stackmob.newman.dsl._
import java.net.URL
import scalaz.concurrent.Strategy
import java.util.concurrent.Executors
import com.stackmob.newman.caching.{Milliseconds, InMemoryHttpResponseCacher}

/**
 * Created by IntelliJ IDEA.
 *
 * com.arschles.github.trivial-jetty-continuations
 *
 * User: aaron
 * Date: 4/19/13
 * Time: 6:35 PM
 */
class Handler extends AbstractHandler {

  private implicit val executorSvc = Executors.newCachedThreadPool() //newFixedThreadPool(1000)
  private implicit val strategy = Strategy.Executor(executorSvc)
  private val apacheHttpClient = new ApacheHttpClient(strategy = strategy)
  private val cacher = new InMemoryHttpResponseCacher
  private val ttl = Milliseconds(1000)
  private val cachingHttpClient = new ReadCachingHttpClient(apacheHttpClient, cacher, ttl)

  override def handle(target: String,
                      request: Request,
                      servletRequest: HttpServletRequest,
                      response: HttpServletResponse) {
    val continuation = ContinuationSupport.getContinuation(request)
    continuation.setTimeout(500)
    continuation.suspend()
    implicit val httpClient = cachingHttpClient

    GET(new URL("http://httpbin.org/get")).executeAsyncUnsafe.map { resp =>
      request.setHandled(true)
      continuation.getServletResponse.getWriter.print(resp.bodyString)
      continuation.complete()
    }
  }
}