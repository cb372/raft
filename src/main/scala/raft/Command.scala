package raft

/**
 * Author: chris
 * Created: 10/26/13
 */
case class Command[Op](operation: Op, serialNumber: SerialNumber)


