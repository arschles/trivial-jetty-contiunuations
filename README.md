trivial-jetty-contiunuations
============================

This Jetty server is a non-blocking proxy to http://httpbin.org/get. It uses [Newman](https://github.com/stackmob/newman) to do the HTTP proxying.

Some possible improvements:

* write a Newman client that uses the finagle HTTP client (https://github.com/twitter/finagle#Simple%20HTTP%20Client), and use it here. Doing so lets us dispatch non-blocking HTTP requests
* add a connection timeout callback (see http://download.eclipse.org/jetty/stable-7/apidocs/org/eclipse/jetty/continuation/Continuation.html#addContinuationListener(org.eclipse.jetty.continuation.ContinuationListener)) and cancel the operation in-progress for that timed out request. also possibly add counters for completed and timed out requests
* reject requests when the Executor queue is over a certain size (ie: backpressure). check out http://wiki.eclipse.org/Jetty/Feature/Continuations#Quality_of_Service_Filter for a possible example
