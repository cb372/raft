package raft

/**
 * Author: chris
 * Created: 10/27/13
 */
private[raft] class PersistentState(dataStore: PersistentDataStore) {

  def getCurrentTerm: Term = dataStore.getCurrentTerm.getOrElse(0)
  def setCurrentTerm(term: Term): Unit = dataStore.setCurrentTerm(term)

  def getVotedFor(currentTerm: Term): Option[ServerId] = dataStore.getVotedFor(currentTerm)
  def setVotedFor(term: Term, candidateId: ServerId): Unit = dataStore.setVotedFor(term, candidateId)

  def getLastLogIndex: LogIndex = dataStore.getLastLogIndex
  def getLastLogTerm: Term = dataStore.getLogEntry(dataStore.getLastLogIndex).map(_.term).getOrElse(0L)

}
