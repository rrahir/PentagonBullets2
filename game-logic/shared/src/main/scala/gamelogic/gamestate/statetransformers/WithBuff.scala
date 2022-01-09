package gamelogic.gamestate.statetransformers

import gamelogic.buffs.{Buff, PassiveBuff, TickerBuff}
import gamelogic.gamestate.GameState

final class WithBuff(buff: Buff) extends GameStateTransformer {
  def apply(gameState: GameState): GameState = buff match {
    case buff: TickerBuff =>
      val newBuffMap = gameState.tickerBuffs.getOrElse(buff.bearerId, Map()) + (buff.buffId -> buff)
      gameState.copy(
        newTime = buff.appearanceTime,
        tickerBuffs = gameState.tickerBuffs + (buff.bearerId -> newBuffMap)
      )

    case buff: PassiveBuff =>
      println(s"The buff!!! $buff")
      val newBuffMap = gameState.passiveBuffs.getOrElse(buff.bearerId, Map()) + (buff.buffId -> buff)
      gameState.copy(
        newTime = buff.appearanceTime,
        passiveBuffs = gameState.passiveBuffs + (buff.bearerId -> newBuffMap)
      )
  }
}
