package raft

/**
 * Author: chris
 * Created: 10/26/13
 */
class MockPersistentState extends PersistentState {
  var currentTerm: Option[Term] = None
  val votedFor = collection.mutable.Map.empty[Term, ServerId]
  val logs = collection.mutable.ArrayBuffer.empty[LogEntry]

  logs(0) = null // logs are 1-indexed

  def getCurrentTerm: Option[Term] = currentTerm
  def setCurrentTerm(term: Term): Unit = { currentTerm = Some(term) }

  def getVotedFor(currentTerm: Term): Option[ServerId] = votedFor.get(currentTerm)
  def setVotedFor(term: Term, candidateId: ServerId): Unit = { votedFor.put(term, candidateId) }

  def getLogEntry(index: LogIndex): Option[LogEntry] = {
    if (index > Int.MaxValue) sys.error(s"MockPersistentState only supports indices up to ${Int.MaxValue}")
    if (index >= logs.size) None
    else Option(logs(index.toInt))
  }

  def getLastLogIndex: LogIndex = logs.size - 1

  def deleteLogEntryAndAllFollowing(index: LogIndex): Unit = {
    if (index > Int.MaxValue) sys.error(s"MockPersistentState only supports indices up to ${Int.MaxValue}")
    while (index < logs.size)
      logs.remove(index.toInt)
  }

  def appendLogEntries(entries: Seq[LogEntry]): Unit = { logs ++= entries }
}
