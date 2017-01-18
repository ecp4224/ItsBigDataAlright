package me.eddiep.bigdata.data.facebook;

import java.net.MalformedURLException;
import java.net.URL;

public class Paging {
    private String previous;
    private String next;

    private Paging() { }

    public String getPrevious() {
        return previous;
    }

    public URL getPreviousURL() {
        try {
            return new URL(previous);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNext() {
        return next;
    }

    public URL getNextURL() {
        try {
            return new URL(next);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Paging paging = (Paging) o;

        if (previous != null ? !previous.equals(paging.previous) : paging.previous != null) return false;
        return next != null ? next.equals(paging.next) : paging.next == null;

    }

    @Override
    public int hashCode() {
        int result = previous != null ? previous.hashCode() : 0;
        result = 31 * result + (next != null ? next.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Paging{" +
                "previous='" + previous + '\'' +
                ", next='" + next + '\'' +
                '}';
    }
}
