webcrank-clientsession
----------------------

[![Build Status](https://travis-ci.org/webcrank/webcrank-clientsession.scala.png)](https://travis-ci.org/webcrank/webcrank-clientsession.scala)

A toolkit for secure client sessions. Provides encryption and authentication
for session data.

The library relies on AES with CBC and PKCS5 padding for encryption to ensure nobody can READ the session.

The library relies on HMAC/SHA512 for authentication to ensure nobody can MODIFY the session.

Note that this library has _no_ dependencies on the rest of webcrank and can be used independently.

Getting webcrank-clientsession
------------------------------

If you're using SBT, add the following dependency to your build file:

    "io.webcrank" %% "webcrank-clientsession" % "0.1"


Using webcrank-clientsession
----------------------------

The first think to note is the `ClientSessionKey`. The `ClientSesssionKey`
wraps the encryption and HMAC keys.

The `ClientSessionKey` is __secret__ data. It is _your_ responsibility
to make sure that no else has access to this key.

If you want your sessions to be persistant across restarts, it is _your_
responsibility to store the `ClientSessionKey`. The key has a convenient
string form that can be accessed via `ClientSession#token` and can be
recreated with `ClientSession.fromToken`.

Normally you will just provide how to `store` and `restore` the
key.

A standard usage would be:


```scala
import webcrank.clientsession._

def store: String => Unit =
  token => ??? // write to file or db

def restore: Option[String] =
  ???          // read from file or db, None if not created yet

val sessions = ClientSession.manage(store, restore)



// on receiving a request (in pseudo code)

sessions.verify(cookie.get("_SESSION")) match {
  case -\/(err) => ???       // do something when the cookie didn't verify
  case \/-(session) => ???   // do something with the session
}

// on sending a request (in pseudo code)

val secured = sessions.secure("user1")
cookie.set("_SESSION", secured)


```


Security Considerations
-----------------------

There are too many things that can go wrong when using any type of
session store, so I will be prescriptive:

 1. You must keep your `ClientSessionKey` secret.
 2. You must use HTTPS.
 3. You must set HttpOnly flag on cookie.
 4. You must set Secure flag on cookie.
 5. You must not store sensitive data in cookie (passwords, secret keys etc...),
    use it for identity only.
 6. You should carefully control expiry dates.


All of the above except the encryption key part of the ClientSessionKey
are all relevant if you were using a server side store, so don't assume
that these requirements are just because it is sent client-side.


Why would you use a client session store?
-----------------------------------------

 - No db lookup required for each request.
 - Free distribution, each request is self contained, so we can
   add a servers without concern for bottlenecking session store.


Why would you _not_ use a session store?
-----------------------------------------

 - Additional bandwidth. This would be come an issue if you were
   storing lots of data in your session (but just don't).
 - Hard to invalidate a specific session. You can invalidate everyone
   by just changing your key, but if you want to pick on a single
   session you are back to having a data store to track that and you
   are (prossibly) better off just storing everything.


Future
------

Things that are planned:

 - Switch to ByteString implementation and remove Array[Byte]
 - Add simple key/value support.
 - Add scalaz.effect.IO to track some of the uglies.
 - Add support for a file backed implementation.


Contributing
------------

Any contributions welcome, in particular any security reviews on this
library, its recommendations or the underlying crypto libraries would
be very helpful.

The [issue tracker](https://github.com/webcrank/webcrank-clientsession.scala/issues)
is up to date with anything that is planned / desired.
