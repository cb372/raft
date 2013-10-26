package raft

/**
 * Author: chris
 * Created: 10/26/13
 */
trait PersistentState {

  def getCurrentTerm: Option[Term]
  def setCurrentTerm(term: Term): Unit

  def getVotedFor(currentTerm: Term): Option[ServerId]
  def setVotedFor(term: Term, candidateId: ServerId): Unit

  def getLogEntry(index: LogIndex): Option[LogEntry]
  def getLastLogIndex: LogIndex
  def deleteLogEntryAndAllFollowing(index: LogIndex): Unit
  def appendLogEntries(entries: Seq[LogEntry]): Unit

}
