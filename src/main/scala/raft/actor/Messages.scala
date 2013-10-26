package raft
package actor

/**
 * Author: chris
 * Created: 10/26/13
 */
object Messages {

  /* Server-Server interaction */

  /*
   * RequestVote RPC.
   * candidateId is the RequestVote message's sender.
   */
  case class RequestVote(term: Term, lastLogIndex: LogIndex, lastLogTerm: Term)
  case class RequestVoteReply(term: Term, voteGranted: Boolean)

  /*
   * AppendEntries RPC.
   * leaderId is the AppendEntries message's sender.
   */
  case class AppendEntries(term: Term, prevLogIndex: LogIndex, prevLogTerm: Term, entries: Seq[LogEntry[_]], leaderCommit: LogIndex)
  case class AppendEntriesReply(term: Term, success: Boolean)

  /* Client-Server interaction */

  case class ExecuteCommand[Op](command: Command[Op])
  case class ExecuteCommandReply(executed: Boolean, leaderAddress: Option[ServerId])

  // TODO messages for cluster configuration changes

}
