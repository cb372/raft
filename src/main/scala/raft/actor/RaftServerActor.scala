package raft
package actor

import akka.actor.Actor
import raft.actor.Messages.{RequestVoteReply, AppendEntries, RequestVote}

/**
 * Author: chris
 * Created: 10/26/13
 */
class RaftServerActor(persistentState: PersistentState) extends Actor {
  import context._

  /** Index of highest log entry known to be committed */
  var commitIndex = 0
  
  /** Index of highest log entry applied to state machine */
  var lastApplied = 0
  
  // Everybody starts out as a follower
  def receive = follower

  def follower: Receive = {
    case RequestVote(term, lastLogIndex, lastLogTerm) => {
      handleVoteRequest(term, lastLogIndex, lastLogTerm)
    }
    case AppendEntries(term, prevLogIndex, prevLogTerm, entries, leaderCommit) => {
      // TODO
    }
    // TODO start election timeout
  }

  def candidate: Receive = ???

  def leader: Receive = ???

  def handleVoteRequest(term: Term, lastLogIndex: LogIndex, lastLogTerm: Term) {
    var currentTerm = persistentState.getCurrentTerm
    if (term < currentTerm) {
      // Reject because candidate is out of date
      sender ! RequestVoteReply(currentTerm, voteGranted = false)
    } else {
      // "If RPC request ... contains term T > currentTerm: set currentTerm = T and convert to follower"
      if (term > currentTerm) {
        currentTerm = term
        persistentState.setCurrentTerm(currentTerm)
      }
      val votedFor = persistentState.getVotedFor(currentTerm).getOrElse(sender)
      if (votedFor != sender) {
        // Reject because we have already voted for another candidate in this term
        sender ! RequestVoteReply(currentTerm, voteGranted = false)
      } else if (isLessUpToDateThanMe(lastLogIndex, lastLogTerm)) {
        // Reject because candidate's log is less up-to-date than my own
        sender ! RequestVoteReply(currentTerm, voteGranted = false)
      } else {
        // Vote for candidate
        persistentState.setVotedFor(currentTerm, sender)
        sender ! RequestVoteReply(currentTerm, voteGranted = true)
      }
    }
  }

  def isLessUpToDateThanMe(theirLastLogIndex: LogIndex, theirLastLogTerm: Term): Boolean = {
    val myLastLogIndex = persistentState.getLastLogIndex
    val myLastLogTerm = persistentState.getLastLogTerm
    // If the logs have last entries with different terms, then the log with the later term is more up-to-date.
    // If the logs end with the same term, then whichever log is longer is more up-to-date.
    theirLastLogTerm < myLastLogTerm || (theirLastLogTerm == myLastLogTerm && theirLastLogIndex < myLastLogIndex)
  }

}
