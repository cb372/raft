package raft

/**
 * Author: chris
 * Created: 10/26/13
 */
case class LogEntry[Op](term: Term, command: Command[Op])
