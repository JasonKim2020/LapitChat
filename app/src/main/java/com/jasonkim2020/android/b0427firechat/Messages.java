package com.jasonkim2020.android.b0427firechat;

public class Messages {
    private String thumb_image;
    private String message, type;
    private boolean seen;
    private long time;
    private String from;

    public String getFrom() {
        return from;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public Messages(String thumb_image, String message, String type, boolean seen, long time, String from) {
        this.thumb_image = thumb_image;
        this.message = message;
        this.type = type;
        this.seen = seen;
        this.time = time;
        this.from = from;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Messages(String message, String type, boolean seen, long time, String from) {
        this.message = message;
        this.type = type;
        this.seen = seen;
        this.time = time;
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Messages() {
    }

    public Messages(String message, String type, boolean seen, long time) {
        this.message = message;
        this.type = type;
        this.seen = seen;
        this.time = time;
    }
}
