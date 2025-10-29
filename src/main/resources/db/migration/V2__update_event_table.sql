ALTER TABLE events DROP CONSTRAINT events_event_status_check;

ALTER TABLE events
    ADD CONSTRAINT events_event_status_check
        CHECK (event_status IN ('OPEN', 'CLOSED', 'UPCOMING'));