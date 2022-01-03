import zio.*
import zio.clock.Clock
import gamecommunication.ClientToServer.Ping
import gamecommunication.ServerToClient.Pong

import java.util.concurrent.TimeUnit

package object game {

  /** Computes the the delta difference between this system time and the server system time.
    */
  def synchronizeClock(pingPong: Ping => UIO[Pong], tries: Int = 10): ZIO[Clock, Nothing, Double] = {
    def accumulator(remaining: Int, deltas: List[Long]): ZIO[Clock, Nothing, Double] =
      for {
        now       <- zio.clock.currentTime(TimeUnit.MILLISECONDS)
        ping      <- UIO(Ping(now))
        fiber     <- pingPong(ping).fork
        pong      <- fiber.join
        nowAgain  <- zio.clock.currentTime(TimeUnit.MILLISECONDS)
        latency   <- UIO((nowAgain - pong.originalSendingTime) / 2)
        linkTime  <- UIO(latency + pong.midwayDistantTime)
        newDeltas <- UIO((linkTime - nowAgain) +: deltas)
        delta <-
          if remaining == 0 then UIO(newDeltas.sum.toDouble / newDeltas.length)
          else accumulator(remaining - 1, newDeltas)
      } yield delta

    accumulator(tries, Nil)
  }

}
