package it.unibo.deathnote.impl;

import java.util.LinkedHashMap;
import java.util.SequencedMap;

import it.unibo.deathnote.api.DeathNote;

/**
 * A DeathNote implementation.
 */
public class DeathNoteImplementation implements DeathNote {
    private final SequencedMap<String, DeathNoteItem> deathNote;

    /**
     * Constructs an empty DeathNote instance.
     */
    public DeathNoteImplementation() {
        this.deathNote = new LinkedHashMap<>();
    }

    private class DeathNoteItem {
        private static final String DEFAULT_CAUSE_OF_DEATH = "heart attack";
        private static final int MILLISECONDS_TO_WRITE_CAUSE_OF_DEATH = 40;
        private static final int MILLISECONDS_TO_WRITE_DETAILS_OF_DEATH = 6000 + MILLISECONDS_TO_WRITE_CAUSE_OF_DEATH;
        private final long creationTime;
        private String cause;
        private String details;

        DeathNoteItem() {
            this.creationTime = System.currentTimeMillis();
            this.cause = DEFAULT_CAUSE_OF_DEATH;
            this.details = "";
        }

        public boolean writeCause(final String cause) {
            final long now = System.currentTimeMillis();

            if (now > this.creationTime + MILLISECONDS_TO_WRITE_CAUSE_OF_DEATH) {
                return false;
            }

            this.cause = cause;

            return true;
        }

        public boolean writeDetails(final String details) {
            final long now = System.currentTimeMillis();

            if (now > this.creationTime + MILLISECONDS_TO_WRITE_DETAILS_OF_DEATH) {
                return false;
            }

            this.details = details;

            return true;
        }
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public String getRule(final int ruleNumber) {
        if (ruleNumber < 1 || ruleNumber > RULES.size()) {
            throw new IllegalArgumentException("Rule number " + ruleNumber + " does not exist");
        }

        return RULES.get(ruleNumber - 1);
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    @SuppressWarnings({ "PMD.AvoidThrowingNullPointerException" })
    public void writeName(final String name) {
        if (name == null) {
            throw new NullPointerException("Parameter name can not be null, empty or blank");
        }

        if (name.isEmpty() || name.isBlank()) {
            return;
        }

        this.deathNote.put(name, new DeathNoteItem());
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public boolean writeDeathCause(final String cause) {
        if (this.deathNote.lastEntry() == null || cause == null) {
            throw new IllegalStateException("The death cause can't be write in the note");
        }

        return this.deathNote.lastEntry().getValue().writeCause(cause);
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public boolean writeDetails(final String details) {
        if (this.deathNote.lastEntry() == null || details == null) {
            throw new IllegalStateException("The death cause can't be write in the note");
        }

        return this.deathNote.lastEntry().getValue().writeDetails(details);
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public String getDeathCause(final String name) {
        if (!this.deathNote.containsKey(name)) {
            throw new IllegalArgumentException("Name " + name + " is not written in the note");
        }

        return this.deathNote.get(name).cause;
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public String getDeathDetails(final String name) {
        if (!this.deathNote.containsKey(name)) {
            throw new IllegalArgumentException("Name " + name + " is not written in the note");
        }

        return this.deathNote.get(name).details;
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public boolean isNameWritten(final String name) {
        return this.deathNote.containsKey(name);
    }
}
