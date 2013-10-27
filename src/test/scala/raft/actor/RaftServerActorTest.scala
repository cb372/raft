package raft.actor

import akka.testkit.{ImplicitSender, TestKit, TestActorRef}
import akka.actor.{ActorRef, Props, ActorSystem}
import org.scalatest.{BeforeAndAfterAll, FunSpecLike, ShouldMatchers}
import raft.{PersistentState, Command, LogEntry, MockPersistentDataStore}
import raft.actor.Messages.{RequestVoteReply, RequestVote}

/**
 * Author: chris
 * Created: 10/27/13
 */
class RaftServerActorSpec(actorSystem: ActorSystem)
  extends TestKit(actorSystem)
  with ImplicitSender with FunSpecLike with ShouldMatchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("RaftServerActorSpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(actorSystem)
  }

  describe("Follower") {

    describe("Receiving a RequestVote from a candidate") {

      it ("should reject candidate if candidate's term is older than its own") {
        val dataStore = new MockPersistentDataStore
        dataStore.setCurrentTerm(10)
        val actorRef = TestActorRef(Props(classOf[RaftServerActor], new PersistentState(dataStore)))

        actorRef ! RequestVote(term = 9, 100, 100)
        expectMsg(RequestVoteReply(10, voteGranted = false))
      }

      it ("should update currentTerm if candidate's term is newer than its own") {
        val dataStore = new MockPersistentDataStore
        dataStore.setCurrentTerm(10)
        val actorRef = TestActorRef(Props(classOf[RaftServerActor], new PersistentState(dataStore)))

        actorRef ! RequestVote(term = 11, 100, 100)
        dataStore.getCurrentTerm.get should be(11)
        expectMsgAnyClassOf(classOf[RequestVoteReply])
      }

      it ("should reject candidate if it has already voted in this term") {
        val dataStore = new MockPersistentDataStore
        dataStore.setCurrentTerm(10)
        val otherCandidate = actorSystem.actorOf(Props.empty, "otherCandidate")
        dataStore.setVotedFor(10, otherCandidate)
        val actorRef = TestActorRef(Props(classOf[RaftServerActor], new PersistentState(dataStore)))

        actorRef ! RequestVote(term = 10, 100, 100)
        expectMsg(RequestVoteReply(10, voteGranted = false))
      }

      it ("should reject candidate if candidate's latest log term is older than its own") {
        val dataStore = new MockPersistentDataStore
        dataStore.setCurrentTerm(10)
        dataStore.appendLogEntries(Seq(LogEntry(5, Command("op", 123))))
        val actorRef = TestActorRef(Props(classOf[RaftServerActor], new PersistentState(dataStore)))

        actorRef ! RequestVote(term = 10, lastLogIndex = 100, lastLogTerm = 4)
        expectMsg(RequestVoteReply(10, voteGranted = false))
      }

      it ("should reject candidate if latest log terms are equal and candidate's log is shorter than its own") {
        val dataStore = new MockPersistentDataStore
        dataStore.setCurrentTerm(10)
        dataStore.appendLogEntries(Seq(LogEntry(5, Command("op1", 123)), LogEntry(5, Command("op2", 124))))
        val actorRef = TestActorRef(Props(classOf[RaftServerActor], new PersistentState(dataStore)))

        actorRef ! RequestVote(term = 10, lastLogIndex = 1, lastLogTerm = 5)
        expectMsg(RequestVoteReply(10, voteGranted = false))
      }

      it ("should vote for candidate if candidate's term == currentTerm and logs are equally up to date") {
        val dataStore = new MockPersistentDataStore
        dataStore.setCurrentTerm(10)
        dataStore.appendLogEntries(Seq(LogEntry(5, Command("op1", 123)), LogEntry(5, Command("op2", 124))))
        val actorRef = TestActorRef(Props(classOf[RaftServerActor], new PersistentState(dataStore)))

        actorRef ! RequestVote(term = 10, lastLogIndex = 2, lastLogTerm = 5)
        expectMsg(RequestVoteReply(10, voteGranted = true))
      }

      it ("should vote for candidate if candidate's term == currentTerm and candidate's log is more up to date than its own") {
        val dataStore = new MockPersistentDataStore
        dataStore.setCurrentTerm(10)
        dataStore.appendLogEntries(Seq(LogEntry(5, Command("op1", 123)), LogEntry(5, Command("op2", 124))))
        val actorRef = TestActorRef(Props(classOf[RaftServerActor], new PersistentState(dataStore)))

        actorRef ! RequestVote(term = 10, lastLogIndex = 3, lastLogTerm = 5)
        expectMsg(RequestVoteReply(10, voteGranted = true))
      }

    }

  }

}
