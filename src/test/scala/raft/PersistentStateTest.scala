package raft

import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers

/**
 * Author: chris
 * Created: 10/27/13
 */
class PersistentStateTest extends FlatSpec with ShouldMatchers {
  behavior of "PersistentState"

  it should "report currentTerm as 0 if it is not set" in {
    val dataStore = new MockPersistentDataStore
    val state = new PersistentState(dataStore)
    state.getCurrentTerm should be(0)
  }

  it should "report currentTerm correctly if it is not set" in {
    val dataStore = new MockPersistentDataStore
    dataStore.setCurrentTerm(10)
    val state = new PersistentState(dataStore)
    state.getCurrentTerm should be(10)
  }

}
