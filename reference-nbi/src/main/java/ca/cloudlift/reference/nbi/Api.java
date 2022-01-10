package ca.cloudlift.reference.nbi;

import java.util.concurrent.atomic.AtomicLong;

public class Api {

    private AtomicLong id;
    private String content;

    public Api(AtomicLong id, String content) {
        this.id = id;
        this.content = content;
    }

    public AtomicLong getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
