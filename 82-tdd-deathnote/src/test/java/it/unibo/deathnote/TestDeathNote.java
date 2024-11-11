package it.unibo.deathnote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.deathnote.impl.DeathNoteImplementation;
import it.unibo.deathnote.api.DeathNote;

class TestDeathNote {
    private static final int INVALID_MIN_RULES_INDEX = -5;
    private static final int INVALID_MAX_RULES_INDEX = 14;
    private static final String DEFAULT_CAUSE_OF_DEATH = "heart attack";
    private static final String HUMAN_NAME = "Mario";
    private static final String HUMAN_NAME_02 = "Lucia";
    private DeathNote deathNote;

    @BeforeEach
    void init() {
        this.deathNote = new DeathNoteImplementation();
    }

    /*
     * [1]
     */
    @ParameterizedTest(name = "Rule number {0}")
    @ValueSource(ints = { 0, INVALID_MIN_RULES_INDEX, INVALID_MAX_RULES_INDEX })
    void ruleShouldNotExist(final int ruleNumber) {
        final Exception illegalArgumentException = assertThrows(
                IllegalArgumentException.class,
                () -> this.deathNote.getRule(ruleNumber));
        // validate exception message
        final String exceptionMessage = illegalArgumentException.getMessage();
        assertNotNull(exceptionMessage);
        assertFalse(exceptionMessage.isBlank());
        assertFalse(exceptionMessage.isEmpty());
    }

    /*
     * [2]
     */
    @Test
    void ruleShouldNotBeNullOrBlankOrEmpty() {
        for (final String rule : DeathNote.RULES) {
            assertNotNull(rule);
            assertFalse(rule.isBlank());
            assertFalse(rule.isEmpty());
        }
    }

    /*
     * [3]
     */
    @Test
    void shouldWriteName() {
        // check the Mario will be write to the DeathNote
        assertFalse(this.deathNote.isNameWritten(HUMAN_NAME));
        this.deathNote.writeName(HUMAN_NAME);
        assertTrue(this.deathNote.isNameWritten(HUMAN_NAME));
    }

    @Test
    void shouldThrowExceptionWhenWritingNullName() {
        // null name should throw exception
        assertThrows(
                NullPointerException.class,
                () -> this.deathNote.writeName(null));
    }

    @Test
    void shouldRejectBlankName() {
        final String blankHumanName = "  ";
        this.deathNote.writeName(blankHumanName);
        assertFalse(this.deathNote.isNameWritten(blankHumanName));
    }

    @Test
    void shouldRejectEmptyName() {
        final String emptyHumanName = "";
        this.deathNote.writeName(emptyHumanName);
        assertFalse(this.deathNote.isNameWritten(emptyHumanName));
    }

    /*
     * [4]
     */
    @Test
    void shouldThrowExceptionWhenWritingCauseOfDeath() {
        // null cause and missing human name should throw exception
        assertThrows(
                IllegalStateException.class,
                () -> this.deathNote.writeDeathCause(null));
        assertThrows(
                IllegalStateException.class,
                () -> this.deathNote.writeDeathCause("fake cause"));
    }

    @Test
    void shouldDefaultCauseOfDeathBeHeartAttack() {
        this.deathNote.writeName(HUMAN_NAME);
        assertEquals(DEFAULT_CAUSE_OF_DEATH, this.deathNote.getDeathCause(HUMAN_NAME));
    }

    @Test
    void shouldWriteCauseOfDeath() {
        final String causeOfDeath = "karting accident";

        this.deathNote.writeName(HUMAN_NAME);
        assertTrue(this.deathNote.writeDeathCause(causeOfDeath));
        assertEquals(causeOfDeath, this.deathNote.getDeathCause(HUMAN_NAME));
    }

    @Test
    void shouldCauseOfDeathBeUnmodifiable() throws InterruptedException {
        final int millisecondsWaitingForWriteFail = 100;
        final String causeOfDeath = "karting accident";

        // add first human
        this.deathNote.writeName(HUMAN_NAME);
        this.deathNote.writeDeathCause(causeOfDeath);
        // add new human waiting too much
        this.deathNote.writeName(HUMAN_NAME_02);
        Thread.sleep(millisecondsWaitingForWriteFail);
        assertFalse(this.deathNote.writeDeathCause(causeOfDeath));
        assertEquals(DEFAULT_CAUSE_OF_DEATH, this.deathNote.getDeathCause(HUMAN_NAME_02));
    }

    /*
     * [5]
     */
    @Test
    void shouldThrowExceptionWhenWritingDetailsOfDeath() {
        // null details and missing human name should throw exception
        assertThrows(
                IllegalStateException.class,
                () -> this.deathNote.writeDetails(null));
        assertThrows(
                IllegalStateException.class,
                () -> this.deathNote.writeDetails("fake cause"));
    }

    @Test
    void shouldWriteDetailsOfDeath() {
        final String detailsOfDeath = "ran for too long";

        this.deathNote.writeName(HUMAN_NAME);
        assertTrue(this.deathNote.writeDetails(detailsOfDeath));
        assertEquals(detailsOfDeath, this.deathNote.getDeathDetails(HUMAN_NAME));
    }

    @Test
    void shouldDetailsOfDeathBeUnmodifiable() throws InterruptedException {
        final int millisecondsWaitingForWriteFail = 6100;
        final String detailsOfDeath = "ran for too long";

        // add first human
        this.deathNote.writeName(HUMAN_NAME);
        this.deathNote.writeDetails(detailsOfDeath);
        // add new human waiting too much
        this.deathNote.writeName(HUMAN_NAME_02);
        Thread.sleep(millisecondsWaitingForWriteFail);
        assertFalse(this.deathNote.writeDetails(detailsOfDeath));
        assertEquals("", this.deathNote.getDeathDetails(HUMAN_NAME_02));
    }
}
