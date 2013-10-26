package raft

/**
 * Author: chris
 * Created: 10/26/13
 */
case class LogEntry(term: Term, command: Command)
