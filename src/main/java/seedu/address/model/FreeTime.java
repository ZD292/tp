package seedu.address.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.group.Group;

public class FreeTime implements Iterable<TimeInterval> {

    private final ObservableList<TimeInterval> internalList = FXCollections.observableArrayList();
    private final ObservableList<TimeInterval> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    public void addTime(TimeInterval timeInterval) {
        internalList.add(timeInterval);
    }

    /**
     * Converts the internal list to streams.
     *
     * @return Internal list into streams.
     */
    public Stream<TimeInterval> toStream() {
        return internalList.stream();
    }

    public void addTime(ArrayList<TimeInterval> timeIntervals) {
        internalList.addAll(timeIntervals);
    }

    /**
     * Check whether no time is stored
     * @return boolean representing whether no time is stored
     */
    public boolean isEmpty() {
        return internalList.isEmpty();
    }


    /**
     * Generate String representing list of intervals
     * @param br StringBuilder to store  message
     * @param format specify Message format
     */
    public void getMessage(StringBuilder br, String format) {
        int intervalCount = 1;
        for (TimeInterval t: this.internalList) {
            br.append(br.append(String.format(format, intervalCount, t.toString())));
            intervalCount++;
        }

    }

    public FreeTime findOverlap(FreeTime otherTime, Duration duration) {
        // 4 steps, sort by start time, min start min end 2 pointers, get overlap, interval >= duration check
        this.internalList.sort(TimeInterval::compareStart);
        otherTime.internalList.sort(TimeInterval::compareEnd);
        FreeTime newFreeTime = new FreeTime();

        int p1 = 0;
        int p2 = 0;

        while (p1 < this.internalList.size() && p2 < otherTime.internalList.size()) {
            TimeInterval firstListInterval = this.internalList.get(p1);
            TimeInterval secondListInterval = this.internalList.get(p2);

            boolean overLap = firstListInterval.isTimeIntervalOverlapWithTimeInterval(secondListInterval);
            // given 2 intervals, return the one with smaller end to increment pointer
            TimeInterval intervalWithMaxStart = TimeInterval.getMaxStart(firstListInterval, secondListInterval);
            TimeInterval intervalWithMinEnd = TimeInterval.getMinEnd(firstListInterval, secondListInterval);
            // store the intersect, 2 steps, get intersect, see if can fit duration
            if (overLap) {
                TimeInterval intersect = intervalWithMaxStart.getIntersect(intervalWithMinEnd);
                if (intersect.allows(duration)) {
                    newFreeTime.addTime(intersect);
                }
            }
            // increment pointers
            if (intervalWithMinEnd.equalStartAndEnd(firstListInterval)) {
                p1++;
            } else {
                p2++;
            }
        }

        return newFreeTime;
    }


    @Override
    public Iterator<TimeInterval> iterator() {
        return this.internalList.iterator();
    }

}
