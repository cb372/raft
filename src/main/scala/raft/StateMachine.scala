package raft

/**
 * Author: chris
 * Created: 10/26/13
 */
trait StateMachine {

  def execute(operation: Op): Unit

}
