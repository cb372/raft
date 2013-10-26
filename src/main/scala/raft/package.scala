import akka.actor.ActorRef

/**
 * Author: chris
 * Created: 10/26/13
 */
package object raft {

  /**
   * The unique ID of a Raft server
   */
  type ServerId = ActorRef

  /**
   * The unique ID of a Raft client.
   * This is used to track the latest command serial number processed for each client,
   * so that the same command is not executed multiple times.
   */
  type ClientId = ActorRef

  /**
   * The term, approximately equivalent to the number of leader elections that have taken place.
   */
  type Term = Long

  /**
   * The index of a log entry in an array.
   */
  type LogIndex = Long

  /**
   * A per-client unique serial number assigned to a command by a client.
   * The serial number must be strictly greatly than all previously assigned serial numbers.
   */
  type SerialNumber = Long

  /**
   * A string representing some operation on the state machine.
   * The state machine is responsible for parsing this string into a meaningful operation.
   */
  type Op = String
}
